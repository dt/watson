package test

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._
import game._

class BoardSpec extends Specification {

  "Row" should {
    "dismiss A from 0" in {
      Row.empty(0) must beLike { case Unanswered(l) => l must contain(A)}
      Row.empty.dismiss(0, A)(0) must beLike { case Unanswered(l) => l must not contain(A)}
    }
  }

  "Board" should {
    "have 6 columns" in {
      Board.full.columns must have size(6)
      Board.full.columns must have not contain(6)
      Board.empty.columns must have size(6)
      Board.empty.columns must have not contain(6)
    }

    "be solved when all cells are answered" in {
      Board.full.isSolved must beTrue
    }

    "be unsolved when all cells are unanswered" in {
      Board.empty.isSolved must beFalse
    }

    "be unsolved when a cell is unanswered" in {
      Board.full.set(1, 1, List(A)).isSolved must beFalse
    }

    "not contain a dismissed choice" in {
      Board.empty(0)(0) must beLike { case Unanswered(l) => l must contain(A)}
      Board.empty.dismiss(0, 0, A)(0)(0) must beLike { case Unanswered(l) => l must not contain(A)}
    }

    "not contain a picked choice" in {
      Board.empty(0)(0) must beLike { case Unanswered(l) => l must contain(A)}
      Board.empty.answer(0, 1, A)(0)(0) must beLike { case Unanswered(l) => l must not contain(A)}
    }
  }

  "Random board" should {
    "be solved" in {
      Board.random.isSolved must beTrue
    }

    "not conain duplicates" in {
      forall(Board.full.rows)(_.cells.toSet must have size(6))
      forall(Board.random.rows)(_.cells.toSet must have size(6))
    }

    "(probably) not be the same as another one" in {
      val r = Board.random
      r.cells mustEqual r.cells
      r.cells mustNotEqual Board.random.cells
    }
  }

}