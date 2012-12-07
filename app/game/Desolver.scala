package game

import scala.util.Random

object Desolver {

  def randAnswered(b: Board): Option[(RowChoice, Int)] =
    Random.shuffle(b.rows.zipWithIndex.flatMap{ case (row, i) =>
      Random.shuffle(row.cells.zipWithIndex.collect{ case (Answered(x), j) => RowChoice(i, x) -> j}).headOption
    }).headOption


  def randAnswerCol(b: Board, col: Int, exceptRow: Int): Option[RowChoice] = {
    val options: IndexedSeq[RowChoice] =
      six.map(i => i -> b(i, col)).collect{ case (i, Answered(x)) if i != exceptRow => RowChoice(i, x)}
    Random.shuffle(options).headOption
  }

  def apply(maxSteps: Int)(g: Game): Game = {
    if (maxSteps > 0) {
      g.board.checkValid
      val stepped = all(g)
      stepped.board.checkValid
      try {
        if (!Solver(1000)(stepped).isSolved) {
          val oldClues = Set(g.clues:_*)
          val newClues = stepped.clues.filter(oldClues.contains)
          throw new IllegalStateException(
            "before: \n " + g.board + "\n after: \n" + stepped.board + "\n new clues: \n" + newClues
          )
        }
      } catch {
        case c: Contradiction => {
          val oldClues = Set(g.clues:_*)
          val newClues = stepped.clues.filter(oldClues.contains)
          println("CONTRADICTION")
          println("before: ")
          println(g.board)
          println("after: ")
          println(stepped.board)
          println("new clues: ")
          println(newClues)
          throw c
        }
      }
      if (makingProgress(g, stepped))
        apply(maxSteps -1)(stepped)
      else
        stepped
    } else g
  }

  def makingProgress(before: Game, after: Game): Boolean =
    before.board.unsolved < after.board.unsolved

  def all: Game => Game = (
    identity[Game] _
      andThen ifAllSolved
      andThen sameCol
  )

  def ifAllSolved(g: Game): Game = {
    g(b => Board(b.rows.map{ case r if r.isSolved => r.unanswer(Random.nextInt(6)); case r => r}))
  }

  def sameCol(g: Game): Game = {
    randAnswered(g.board).map { case (r1, c1) =>
      randAnswerCol(g.board, col = c1, exceptRow = r1.row).map { r2 =>
        g(_.unanswer(r1.row, c1), sameCol = SameCol(r1, r2) :: g.sameCol)
      }.getOrElse(g)
    }.getOrElse(g)
  }

}