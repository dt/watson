package game

case class Game(board: Board,
  sameCol: List[SameCol] = Nil,
  adjCol: List[AdjCol] = Nil,
  leftToRight: List[LeftToRight] = Nil,
  answers: Option[Board] = None) {

  def clues = sameCol ++ adjCol ++ leftToRight

  def isSolved = board.isSolved

  def apply(f: Board => Board = identity[Board],
      sameCol: List[SameCol] = this.sameCol,
      adjCol: List[AdjCol] = this.adjCol,
      leftToRight: List[LeftToRight] = this.leftToRight): Game =
    this.copy(board = f(board), sameCol = sameCol, adjCol = adjCol, leftToRight = leftToRight)

  def apply(c: Clue): Game = c match {
    case c: SameCol => apply(c)
    case c: AdjCol => apply(c)
    case c: LeftToRight => apply(c)
  }

  def apply(col: SameCol) = this.copy(sameCol = col :: this.sameCol)
  def apply(adj: AdjCol) = this.copy(adjCol = adj :: this.adjCol)
  def apply(ltr: LeftToRight) = this.copy(leftToRight = ltr :: this.leftToRight)

  def check(c: AdjCol, g: => Game) = {
    answers.foreach(correct => {
      if (math.abs(correct.col(c.x).get - correct.col(c.y).get) != 1)
        throw new IncorrectClue(c, correct, this.board)
    })
    g
  }

   def check(c: SameCol, g: => Game) = {
    answers.foreach(correct => {
      if (correct.col(c.above).get != correct.col(c.below).get)
        throw new IncorrectClue(c, correct, this.board)
    })
    g
  }

  def check(c: LeftToRight, g: => Game) = {
    answers.foreach(correct => {
      if (correct.col(c.left).get >= correct.col(c.right).get)
        throw new IncorrectClue(c, correct, this.board)
    })
    g
  }

}

object Game {
  def random = {
    val board = Board.random
    Game(board = board, answers = Some(board))
  }
}