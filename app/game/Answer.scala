package game

class IncorrectAnswer(row: Int, col: Int, said: Choice, shouldBe: Choice) extends IllegalStateException({
  "Tried to put %s at row %d, col %d; should be %s".format(said, row, col, shouldBe)
})

sealed trait Choice { def i: Int}
case object A extends Choice { val i = 0 }
case object B extends Choice { val i = 1 }
case object C extends Choice { val i = 2 }
case object D extends Choice { val i = 3 }
case object E extends Choice { val i = 4 }
case object F extends Choice { val i = 5 }

trait RowChoice {
  def row: Int
  def choice: Choice
  def name: String = row + choice.toString
  def <(other: RowChoice): Boolean = row < other.row
}

object RowChoice {
  def apply(row: Int, choice: Int): RowChoice = apply(row, Choice.all(choice))
  def apply(row: Int, choice: Choice): RowChoice = {
    row match {
      case 0 => choice match {
        case A => R0A
        case B => R0B
        case C => R0C
        case D => R0D
        case E => R0E
        case F => R0F
      }
      case 1 => choice match {
        case A => R1A
        case B => R1B
        case C => R1C
        case D => R1D
        case E => R1E
        case F => R1F
      }
      case 2 => choice match {
        case A => R2A
        case B => R2B
        case C => R2C
        case D => R2D
        case E => R2E
        case F => R2F
      }
      case 3 => choice match {
        case A => R3A
        case B => R3B
        case C => R3C
        case D => R3D
        case E => R3E
        case F => R3F
      }
      case 4 => choice match {
        case A => R4A
        case B => R4B
        case C => R4C
        case D => R4D
        case E => R4E
        case F => R4F
      }
      case 5 => choice match {
        case A => R5A
        case B => R5B
        case C => R5C
        case D => R5D
        case E => R5E
        case F => R5F
      }
    }
  }
}

object Choice {
  implicit def ord: Ordering[Choice] = Ordering.by(_.i)
  val all: IndexedSeq[Choice] = IndexedSeq(A, B, C, D, E, F)
}
case object R0A extends RowChoice { def row = 0; def choice = A; override def name = "Ⓐ" }
case object R0B extends RowChoice { def row = 0; def choice = B; override def name = "Ⓑ" }
case object R0C extends RowChoice { def row = 0; def choice = C; override def name = "Ⓒ" }
case object R0D extends RowChoice { def row = 0; def choice = D; override def name = "Ⓓ" }
case object R0E extends RowChoice { def row = 0; def choice = E; override def name = "Ⓔ" }
case object R0F extends RowChoice { def row = 0; def choice = F; override def name = "Ⓕ" }

case object R1A extends RowChoice { def row = 1; def choice = A; override def name = "①" }
case object R1B extends RowChoice { def row = 1; def choice = B; override def name = "②" }
case object R1C extends RowChoice { def row = 1; def choice = C; override def name = "③" }
case object R1D extends RowChoice { def row = 1; def choice = D; override def name = "④" }
case object R1E extends RowChoice { def row = 1; def choice = E; override def name = "⑤" }
case object R1F extends RowChoice { def row = 1; def choice = F; override def name = "⑥" }

case object R2A extends RowChoice { def row = 2; def choice = A; override def name = "☪" }
case object R2B extends RowChoice { def row = 2; def choice = B; override def name = "✝" }
case object R2C extends RowChoice { def row = 2; def choice = C; override def name = "✡" }
case object R2D extends RowChoice { def row = 2; def choice = D; override def name = "☯" }
case object R2E extends RowChoice { def row = 2; def choice = E; override def name = "☮" }
case object R2F extends RowChoice { def row = 2; def choice = F; override def name = "☭" }

case object R3A extends RowChoice { def row = 3; def choice = A; override def name = "☾" }
case object R3B extends RowChoice { def row = 3; def choice = B; override def name = "☃" }
case object R3C extends RowChoice { def row = 3; def choice = C; override def name = "☁" }
case object R3D extends RowChoice { def row = 3; def choice = D; override def name = "☂" }
case object R3E extends RowChoice { def row = 3; def choice = E; override def name = "☀" }
case object R3F extends RowChoice { def row = 3; def choice = F; override def name = "⚡" }

case object R4A extends RowChoice { def row = 4; def choice = A; override def name = "⚀" }
case object R4B extends RowChoice { def row = 4; def choice = B; override def name = "⚁" }
case object R4C extends RowChoice { def row = 4; def choice = C; override def name = "⚂" }
case object R4D extends RowChoice { def row = 4; def choice = D; override def name = "⚃" }
case object R4E extends RowChoice { def row = 4; def choice = E; override def name = "⚄" }
case object R4F extends RowChoice { def row = 4; def choice = F; override def name = "⚅" }

case object R5A extends RowChoice { def row = 5; def choice = A; override def name = "♔" }
case object R5B extends RowChoice { def row = 5; def choice = B; override def name = "♕" }
case object R5C extends RowChoice { def row = 5; def choice = C; override def name = "♖" }
case object R5D extends RowChoice { def row = 5; def choice = D; override def name = "♗" }
case object R5E extends RowChoice { def row = 5; def choice = E; override def name = "♘" }
case object R5F extends RowChoice { def row = 5; def choice = F; override def name = "♙" }


