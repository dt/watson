package game

object Desolver {

  def apply(maxSteps: Int)(g: Game): Game = {
    if (maxSteps > 0) {
      val stepped = all(g)
      //println(stepped.board)
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
  )


}