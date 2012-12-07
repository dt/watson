package game

trait Cell
case class Answered(value: Choice) extends Cell {
  override def toString = {
    "%s%s%s".format(("-" * value.i) + Console.BOLD, value, Console.RESET + ("-" * (5 - value.i)))
  }
}
case class Unanswered(possible: List[Choice]) extends Cell {
  override def toString = (
    (if (possible.contains(A)) "a" else "-") +
    (if (possible.contains(B)) "b" else "-") +
    (if (possible.contains(C)) "c" else "-") +
    (if (possible.contains(D)) "d" else "-") +
    (if (possible.contains(E)) "e" else "-") +
    (if (possible.contains(F)) "f" else "-")
  )
}
object Unanswered { val all: Unanswered = Unanswered(Choice.all.toList) }
