package game

case class Row(cells: IndexedSeq[Cell]) {
  val cols: IndexedSeq[Int] = IndexedSeq((0 to 5):_*)

  def apply(i: Int) = cells(i)

  def isSolved = cells.forall(_.isInstanceOf[Answered])

  def unsolved = cells.collect{case Unanswered(l) => l.size; case _ => 0}.sum

  def col(x: Choice): Option[Int] = cells.indexWhere(_ == Answered(x)) match {
    case -1 => None
    case x => Some(x)
  }

  def set(col: Int, choice: Cell) = Row(cells.updated(col, choice))

  def answer(col: Int, choice: Choice) = {
    Row(cells.updated(col, Answered(choice)).map{
      case Answered(x) => Answered(x)
      case Unanswered(l) => Unanswered(l.filterNot(_ == choice))
    })
  }

  def dismiss(col: Int, choice: Choice*) = Row(cells.updated(col, cells(col) match {
    case Unanswered(l) if l.exists(choice.contains) => Unanswered(l.filterNot(choice.contains))
    case i => throw new IllegalArgumentException("can't dismiss from answered cell")
  }))

  def dismissNot(not: Set[Int], choice: Choice) = Row(cols.map(i => cells(i) match {
    case Unanswered(l) if !not.contains(i) && l.contains(choice) => Unanswered(l.filterNot(_ == choice))
    case c => c
  }))

  override def toString = cells.mkString(" | ")
}

object Row {
  val full = Row(Choice.all.map(Answered.apply))
  val empty = Row((0 until 6).map(_ => Unanswered.all))
  private def shuffled = scala.util.Random.shuffle(Choice.all)
  def random = Row(shuffled.map(Answered.apply))
}
