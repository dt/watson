package game

class Contradiction(b: Board, c: Clue) extends Exception
object Contradiction { def apply(b: Board, c: Clue) = throw new Contradiction(b, c)}

trait Clue
case class SameCol(above: RowChoice, below: RowChoice) extends Clue
case class AdjCol(x: RowChoice, y: RowChoice) extends Clue
case class LeftToRight(left: RowChoice, right: RowChoice) extends Clue