package game

case class Board(rows: IndexedSeq[Row]) {
  def apply(i: Int) = rows(i)
  def apply(x: Int, y: Int) = rows(x)(y)
  def columns = six
  def isSolved = rows.forall(_.isSolved)
  def unsolved = rows.map(_.unsolved).sum
  def cells = rows.flatMap(_.cells)

  def checkValid = if (debugging){ rows.foreach(_.checkValid); this} else this

  def answer(row: Int, col: Int, choice: Choice) =
    Board(rows.updated(row, rows(row).answer(col, choice))).checkValid

  def unanswer(row: Int, col: Int) = Board(rows.updated(row, rows(row).unanswer(col)))
  def dismiss(row: Int, col: Int, choice: Choice*) = Board(rows.updated(row, rows(row).dismiss(col, choice:_*)))
  def dismissNot(row: Int, not: Set[Int], choice: Choice) = Board(rows.updated(row, rows(row).dismissNot(not, choice)))

  def set(row: Int, col: Int, choice: Choice): Board = set(row, col, Answered(choice))
  def set(row: Int, col: Int, choices: List[Choice]): Board = set(row, col, Unanswered(choices))
  def set(row: Int, col: Int, choice: Cell): Board = Board(rows.updated(row, rows(row).set(col, choice)))

  def col(x: RowChoice): Option[Int] = Some(rows(x.row).cells.indexWhere(_ == Answered(x.choice))).filterNot(_ == -1)

  override def toString = {
    val s = "+"
    (s * 55) + "\n" + rows.mkString(s+" ", " %s\n%s ".format(s, s), " "+s+"\n") + (s * 55) + "\n"
  }
}

object Board {
  private def sixTimes(f: => Row) = Board(six.map(_ => f))
  def full = sixTimes(Row.full)
  def empty = sixTimes(Row.empty)
  def random = sixTimes(Row.random)
}