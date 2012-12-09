package game
import scala.collection.mutable

object Solver {
  type Step = Game => Game
  val cache = mutable.Map.empty[Game, Game]

  def apply(maxSteps: Int)(g: Game): Game = cache.getOrElseUpdate(g, solve(maxSteps)(g))

  def solve(maxSteps: Int)(g: Game): Game = {
    if (maxSteps > 0) {
      val stepped = cache.getOrElseUpdate(g,
        try { all(g) } catch {
        case c: Contradiction => throw new ContradictionCreated(c, g, g(_ => c.board))
      })
      if (makingProgress(g, stepped))
        solve(maxSteps -1)(stepped)
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