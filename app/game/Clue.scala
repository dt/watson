package game

class Contradiction(b: Board, c: Clue) extends Exception
object Contradiction { def apply(b: Board, c: Clue) = throw new Contradiction(b, c)}

trait Clue { def id: String }
case class SameCol(above: RowChoice, below: RowChoice) extends Clue {
  override def toString = above + " \\/ " + below
  def id = "sc"+above+below
}
case class AdjCol(x: RowChoice, y: RowChoice) extends Clue {
  override def toString = x + " <-> " + y
  def id = "adj"+x+y
}
case class LeftToRight(left: RowChoice, right: RowChoice) extends Clue {
  override def toString = left + " --> " + right
  def id = "ltr"+left+right
}