package game

object Desolver {

  def apply(maxSteps: Int)(g: Game): Game = {
    if (maxSteps > 0) {
      val stepped = all(g)
      if (!Solver(1000)(stepped).isSolved) {
        val oldClues = Set(g.clues:_*)
        println("before:")
        println(g.board)
        println("after:")
        println(stepped.board)
        println("new clues:" + stepped.clues.filter(oldClues.contains))
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
  )



}