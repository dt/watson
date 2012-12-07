package game

object Solver {
  type Step = Game => Game

  def apply(maxSteps: Int)(g: Game): Game = {
    g.board.checkValid
    if (maxSteps > 0) {
      val stepped = all(g)
      stepped.board.checkValid
      //println(stepped.board)
      if (makingProgress(g, stepped))
        apply(maxSteps -1)(stepped)
      else
        stepped
    } else g
  }

  def makingProgress(before: Game, after: Game): Boolean =
    before.board.unsolved > after.board.unsolved

  def all: Step = (
    identity[Game] _
      andThen EliminationSolver.onlyOptionLeftInCell
      andThen EliminationSolver.onlyCellLeftForChoice
      andThen ClueSolver.sameCol
      andThen ClueSolver.toTheRightOf
      andThen ClueSolver.adjacentCol
  )


}