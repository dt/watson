package game

sealed trait Choice { def i: Int}
case object A extends Choice { val i = 0 }
case object B extends Choice { val i = 1 }
case object C extends Choice { val i = 2 }
case object D extends Choice { val i = 3 }
case object E extends Choice { val i = 4 }
case object F extends Choice { val i = 5 }

case class RowChoice(row: Int, choice: Choice)

object Choice {
  val all: IndexedSeq[Choice] = IndexedSeq(A, B, C, D, E, F)

  val R0A = RowChoice(0, A)
  val R0B = RowChoice(0, B)
  val R0C = RowChoice(0, C)
  val R0D = RowChoice(0, D)
  val R0E = RowChoice(0, E)
  val R0F = RowChoice(0, F)

  val R1A = RowChoice(1, A)
  val R1B = RowChoice(1, B)
  val R1C = RowChoice(1, C)
  val R1D = RowChoice(1, D)
  val R1E = RowChoice(1, E)
  val R1F = RowChoice(1, F)

  val R2A = RowChoice(2, A)
  val R2B = RowChoice(2, B)
  val R2C = RowChoice(2, C)
  val R2D = RowChoice(2, D)
  val R2E = RowChoice(2, E)
  val R2F = RowChoice(2, F)

  val R3A = RowChoice(3, A)
  val R3B = RowChoice(3, B)
  val R3C = RowChoice(3, C)
  val R3D = RowChoice(3, D)
  val R3E = RowChoice(3, E)
  val R3F = RowChoice(3, F)

  val R4A = RowChoice(4, A)
  val R4B = RowChoice(4, B)
  val R4C = RowChoice(4, C)
  val R4D = RowChoice(4, D)
  val R4E = RowChoice(4, E)
  val R4F = RowChoice(4, F)

  val R5A = RowChoice(5, A)
  val R5B = RowChoice(5, B)
  val R5C = RowChoice(5, C)
  val R5D = RowChoice(5, D)
  val R5E = RowChoice(5, E)
  val R5F = RowChoice(5, F)
}

