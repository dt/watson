package game

import scala.util.Random

object Desolver {
  def ifSolvable[T](g: Game, f: Board => Board, c: Clue = null) = {
    val x = g(f)

    if (Solver(1000)(x).isSolved)
      x
    else
    try {
      Option(c)
        .map(x(_))
        .filter(Solver(1000)(_).isSolved)
        .getOrElse(g)
    } catch {
      case c: Contradiction => throw new ContradictionCreated(c, g, x)
    }
  }

  def randAnswered(b: Board): Option[(RowChoice, Int)] =
    Random.shuffle(b.rows.zipWithIndex.flatMap{ case (row, i) =>
      Random.shuffle(row.cells.zipWithIndex.collect{ case (Answered(x), j) => RowChoice(i, x) -> j}).headOption
    }).headOption


  def randAnswerCol(b: Board, col: Int, exceptRow: Int): Option[RowChoice] = {
    val options: IndexedSeq[RowChoice] =
      six.map(i => i -> b(i, col)).collect{ case (i, Answered(x)) if i != exceptRow => RowChoice(i, x)}
    Random.shuffle(options).headOption
  }

  def randAnswerCols(b: Board, cols: Set[Int]): Option[RowChoice] = {
    Random.shuffle(b.rows.zipWithIndex.collect{ case (row, i)  =>
      Random.shuffle(row.cells.zipWithIndex.collect{ case (Answered(x), j) if cols.contains(j) => RowChoice(i, x)}).headOption
    }).flatten.headOption
  }


  def apply(maxSteps: Int, finishPasses: Int = 10)(g: Game): Game = {
    if (maxSteps > 0) {
      assert(Solver(1000)(g).isSolved)
      val stepped = all(g)
      try {
        if (!Solver(1000)(stepped).isSolved) {
          throw new UnsolvableGameCreated(g, stepped)
        }
      } catch {
        case c: Contradiction => throw new ContradictionCreated(c, g, stepped)
      }
      if (makingProgress(g, stepped))
        apply(maxSteps - 1, finishPasses)(stepped)
      else if (finishPasses > 0)
        apply(maxSteps, finishPasses - 1)(stepped)
      else
        stepped
    } else g
  }

  def makingProgress(before: Game, after: Game): Boolean =
    before.board.unsolved < after.board.unsolved

  def weight(f: Game => Game, i: Double): Game => Game =
    if (Random.nextDouble() < i) f else identity[Game]

  def all: Game => Game = (
    identity[Game] _
      andThen fullRow
      andThen uselessAnswer
      andThen uselessAnswer
      andThen weight(adjCol, 1)
      andThen weight(sameCol, 0.7)
      andThen weight(leftToRight, .8)
  )

  def fullRow(g: Game): Game = {
    g(b => Board(b.rows.map{ case r if r.isSolved => r.unanswer(Random.nextInt(6)); case r => r}))
  }

  def uselessAnswer(g: Game): Game = {
    randAnswered(g.board).map(x => ifSolvable(g, _.unanswer(x._1.row, x._2))).getOrElse(g)
  }

  def adjCol(g: Game): Game = {
    randAnswered(g.board).map { case (r1, c1) =>
      randAnswerCols(g.board, Set(c1 + 1, c1 - 1)).map { case r2 =>
        ifSolvable(g, _.unanswer(r1.row, c1), AdjCol(r1, r2))
      }.getOrElse(g)
    }.getOrElse(g)
  }

  def sameCol(g: Game): Game = {
    randAnswered(g.board).map { case (r1, c1) =>
      randAnswerCol(g.board, col = c1, exceptRow = r1.row).map { r2 =>
        g(_.unanswer(r1.row, c1), sameCol = (if (r1 < r2) SameCol(r1, r2) else SameCol(r2, r1)) :: g.sameCol)
      }.getOrElse(g)
    }.getOrElse(g)
  }

  def leftToRight(g: Game): Game = {
    (randAnswered(g.board), randAnswered(g.board)) match {
      case (Some((r1, c1)), Some((r2, c2))) if c1 != c2 =>
        ifSolvable(g, _.unanswer(r1.row, c1), if (c1 < c2) LeftToRight(r1, r2) else LeftToRight(r2, r1))
      case _ => g
    }
  }
}