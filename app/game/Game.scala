package game

case class Game(board: Board, sameCol: List[SameCol], adjCol: List[AdjCol], leftToRight: List[LeftToRight]) {
  def isSolved = board.isSolved
  def apply(f: Board => Board = identity[Board],
      sameCol: List[SameCol] = this.sameCol,
      adjCol: List[AdjCol] = this.adjCol,
      leftToRight: List[LeftToRight] = this.leftToRight): Game = Game(board = f(board), sameCol, adjCol, leftToRight)

}