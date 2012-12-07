package game

case class Board(rows: IndexedSeq[Row]) {
  def apply(i: Int) = rows(i)
  def apply(x: Int, y: Int) = rows(x)(y)
  def columns = IndexedSeq(0 to 5:_*)
  def isSolved = rows.forall(_.isSolved)
  def unsolved = rows.map(_.unsolved).sum
  def cells = rows.flatMap(_.cells)

  def answer(row: Int, col: Int, choice: Choice) = Board(rows.updated(row, rows(row).answer(col, choice)))
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
  def six(f: => Row) = Board((0 until 6).map(_ => f))
  def full = six(Row.full)
  def empty = six(Row.empty)
  def random = six(Row.random)
}