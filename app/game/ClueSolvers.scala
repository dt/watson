package game

import scala.collection.mutable

object ClueSolver {

  def sameCol(game: Game): Game = {
    val buff: mutable.Set[SameCol] = mutable.Set(game.sameCol: _*)

    def consume(board: Board, c: SameCol): Board = {
      def killClue(wrapped: => Board) = { buff.remove(c); wrapped}
      board.columns.foldLeft(board){ case (b, col) =>
        (b(c.above.row)(col), b(c.below.row)(col)) match {
          case (Answered(x), Answered(y)) if x == c.above.choice && y == c.below.choice =>
            { println("killing "+c); killClue(b) }
          case (Answered(x), Answered(y)) if x == c.above.choice || y == c.below.choice =>
            Contradiction(b, c)
          case (Answered(p), _) if p == c.above.choice =>
            killClue(b.answer(c.below.row, col, c.below.choice))
          case (_, Answered(p)) if p == c.below.choice =>
            killClue(b.answer(c.above.row, col, c.above.choice))
          case (Answered(p), Unanswered(l)) if l.contains(c.below.choice) =>
            b.dismiss(c.below.row, col, c.below.choice)
          case (Unanswered(l), Answered(p)) if l.contains(c.above.choice) =>
            b.dismiss(c.above.row, col, c.above.choice)
          case (Unanswered(l1), Unanswered(l2)) if l1.contains(c.above.choice) && !l2.contains(c.below.choice) =>
            b.dismiss(c.above.row, col, c.above.choice)
          case (Unanswered(l1), Unanswered(l2)) if l2.contains(c.below.choice) && !l1.contains(c.above.choice) =>
            b.dismiss(c.below.row, col, c.below.choice)
          case (_, _) => b
        }
      }
    }
    val newBoard = game.sameCol.foldLeft(game.board)(consume)
    game.copy(board = newBoard, sameCol = buff.toList)
  }

  def toTheRightOf(game: Game): Game = {
    val buff: mutable.Set[LeftToRight] = mutable.Set(game.leftToRight: _*)

    def consume(board: Board, c: LeftToRight): Board = {
      def killClue() = {buff.remove(c); true}
      val leftIdx = board(c.left.row).cells.indexWhere {
          case Answered(i) if i == c.left.choice => killClue()
          case Unanswered(l) if l.contains(c.left.choice) => true
          case _ => false
      }

      val rightIdx = board(c.right.row).cells.lastIndexWhere {
          case Answered(i) if i == c.right.choice => killClue()
          case Unanswered(l) if l.contains(c.right.choice) => true
          case _ => false
      }

      val leftRow = board.columns.foldLeft(board(c.left.row)){ case (row, col) => row(col) match {
        case Unanswered(l) if l.contains(c.left.choice) && col >= rightIdx => row.dismiss(col, c.left.choice)
        case _ => row
      }}

      val rightRowSrc = if (c.left.row == c.right.row) leftRow else board(c.right.row)

      val rightRow = board.columns.foldLeft(rightRowSrc){ case (row, col) => row(col) match {
        case Unanswered(l) if l.contains(c.right.choice) && col <= leftIdx => row.dismiss(col, c.right.choice)
        case _ => row
      }}

      Board(board.rows.updated(c.left.row, leftRow).updated(c.right.row, rightRow))
    }
    val newBoard = game.leftToRight.foldLeft(game.board)(consume)
    game.copy(board = newBoard, leftToRight = buff.toList)
  }

  def adjacentCol(game: Game): Game = {
    val buff: mutable.Set[AdjCol] = mutable.Set(game.adjCol: _*)

    def consume(board: Board, c: AdjCol): Board = {
      def killClue[T](t: T) = {buff.remove(c); t}

      (board.col(c.x), board.col(c.y)) match {
        case (Some(x), Some(y)) if math.abs(x - y) != 1 => Contradiction(board, c)
        case (Some(x), Some(y)) => killClue(board)
        case (Some(x), _) => killClue(board.dismissNot(c.y.row, Set(x+1, x-1), c.y.choice))
        case (_, Some(y)) => killClue(board.dismissNot(c.x.row, Set(y+1, y-1), c.x.choice))
        case _ => {
          val x = mutable.Set.empty[Int]
          val y = mutable.Set.empty[Int]
          board.columns.foreach(i => {
            board(c.x.row, i) match {
              case Unanswered(l) if l.contains(c.x.choice) => y += (i+1, i-1)
              case _ =>
            }
            board(c.y.row, i) match {
              case Unanswered(l) if l.contains(c.y.choice) => x += (i+1, i-1)
              case _ =>
            }
          })

          board
            .dismissNot(c.x.row, x.toSet, c.x.choice)
            .dismissNot(c.y.row, y.toSet, c.y.choice)
        }
      }
    }
    val newBoard = game.adjCol.foldLeft(game.board)(consume)
    game.copy(board = newBoard, adjCol = buff.toList)
  }
}
