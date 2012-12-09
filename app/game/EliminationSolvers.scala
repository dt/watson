package game
object EliminationSolver {
  def onlyOptionLeftInCell(g: Game): Game = {
    g.copy(board = Board(g.board.rows.map(r =>
      g.board.columns.foldLeft(r){case (row, i) => row(i) match {
        case Unanswered(List(x)) => row.answer(i, x)
        case _ => row
      }}
    )))
  }

  def onlyCellLeftForChoice(g: Game): Game = {
    trait Seen { def seen(i: Int): Seen }
    case object Zero extends Seen { def seen(i: Int) = Once(i) }
    case class Once(i: Int) extends Seen { def seen(i: Int) = More }
    case object More extends Seen { def seen(i: Int) =  More }

    g.copy(board = Board(g.board.rows.map(r => {
      val seen = g.board.columns.foldLeft(Map.empty[Choice, Seen]){ case (seen, i) =>
        r(i) match {
          case Unanswered(l) => l.foldLeft(seen){case (s, c) => (s.updated(c, s.getOrElse(c, Zero).seen(i)))}
          case Answered(x) => seen.updated(x, More)
        }
      }.collect{ case (c, Once(i)) => i -> Answered(c)}
      Row(g.board.columns.map(i => seen.getOrElse(i, r(i))))
    })))
  }
}