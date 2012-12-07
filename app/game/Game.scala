package game

case class Game(board: Board, sameCol: List[SameCol] = Nil, adjCol: List[AdjCol] = Nil, leftToRight: List[LeftToRight] = Nil) {
  def clues = sameCol ++ adjCol ++ leftToRight
  def isSolved = board.isSolved
  def apply(f: Board => Board = identity[Board],
      sameCol: List[SameCol] = this.sameCol,
      adjCol: List[AdjCol] = this.adjCol,
      leftToRight: List[LeftToRight] = this.leftToRight): Game = Game(board = f(board), sameCol, adjCol, leftToRight)

}