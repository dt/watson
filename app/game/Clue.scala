package game

class Contradiction(val board: Board, val clue: Clue)
  extends IllegalStateException("clue:" + clue.toString + "\n board" + board.toString)

class UnsolvableGameCreated(before: Game, after: Game) extends IllegalStateException({
  val oldClues = Set(before.clues:_*)
  "Unsolvable Game Created: \nbefore: \n%s \nafter: \n%s \n new clues: \n %s\n"
  .format(before.board, after.board, after.clues.filter(oldClues.contains))
})

class ContradictionCreated(c: Contradiction, before: Game, after: Game) extends IllegalStateException({
  val oldClues = Set(before.clues:_*)
  "Contradiction Created: %s \nbefore: \n%s \nafter: \n%s \n newClues: %s"
    .format(c.clue, before.board, after.board, after.clues.filter(oldClues.contains))
}, c)

class IncorrectClue(c: Clue, correct: Board, current: Board) extends IllegalStateException({
  "Clue %s is incorrect: \n%s \ncurrent: \n%s".format(c, correct, current)
})

trait Clue { def id: String; def name: String }
case class SameCol(above: RowChoice, below: RowChoice) extends Clue {
  override def toString = above + " ↓ " + below
  override def name = above.name + " ↓ " + below.name
  def id = "sc"+above+below
}
case class AdjCol(x: RowChoice, y: RowChoice) extends Clue {
  override def toString = x + " ↔ " + y
  override def name = x.name + " ↔ " + y.name
  def id = "adj"+x+y
}
case class LeftToRight(left: RowChoice, right: RowChoice) extends Clue {
  override def toString = left + " → " + right
  override def name = left.name + " → " + right.name
  def id = "ltr"+left+right
}