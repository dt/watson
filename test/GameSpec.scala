package test

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._
import game._
import game.Choice._

class GameSpec extends Specification {
  val full = Game(Board.full, Nil, Nil, Nil)
  val empty = Game(Board.empty, Nil, Nil, Nil)
  val two = Board.full.set(1, 1, List(A, B))


  "Game" should {
    "reflect board solved state" in {
      full.isSolved mustEqual Board.full.isSolved
      empty.isSolved mustEqual empty.isSolved
    }
  }

  "EliminationSolver" should {
    "pick single-option-remaining in cell" in {
      EliminationSolver.onlyOptionLeftInCell(full(_.set(0, 0, List(A)))).board(0,0) mustEqual Answered(A)
    }

    "pick a choice appearing in only one col" in {
      val notF = List(A, B, C, D, E)
      val game = empty(_.set(1, 0, notF).set(1, 1, notF).set(1, 2, notF).set(1, 3, notF).set(1, 4, notF))
      EliminationSolver.onlyCellLeftForChoice(game).board(1)(5) mustEqual Answered(F)
    }
  }

  "ClueSolver" should {
    "pick second part of a same-col clue" in {
      val solved = ClueSolver.sameCol(empty(_.answer(1, 1, A), List(SameCol(R1A, R2B))))
      solved.board(2, 1) mustEqual Answered(B)
      solved.sameCol must beEmpty
    }

    "eliminate same-col conflicts" in {
      val game = empty(sameCol = List(SameCol(R0D, R2A)))
      ClueSolver.sameCol(game(_.answer(0, 5, B))).board(2, 5) must beLike { case Unanswered(l) => l must not contain(A)}
      ClueSolver.sameCol(game(_.answer(2, 5, B))).board(0, 5) must beLike { case Unanswered(l) => l must not contain(D)}
    }

    "eliminate edges with an LtR clue" in {
      val game = empty(leftToRight = List(LeftToRight(R0C, R1D)))
      val solved = ClueSolver.toTheRightOf(game)
      solved.board(0, 5) must beLike {case Unanswered(l) => l must not contain(C)}
      solved.board(1, 0) must beLike {case Unanswered(l) => l must not contain(D)}
      solved.leftToRight must contain(LeftToRight(R0C, R1D))
    }

    "eliminate choices to the left of an LtR clue" in {
      val game = empty(_.answer(0, 2, C), leftToRight = List(LeftToRight(R0C, R1D)))
      val solved = ClueSolver.toTheRightOf(game)
      solved.board(1, 0) must beLike {case Unanswered(l) => l must not contain(D)}
      solved.board(1, 1) must beLike {case Unanswered(l) => l must not contain(D)}
      solved.board(1, 2) must beLike {case Unanswered(l) => l must not contain(D)}
      solved.leftToRight must beEmpty
    }

    "eliminate to all but 1st col given an LtR with right 2nd col" in {
      val game = empty(_.answer(1, 1, B), leftToRight = List(LeftToRight(R1A, R1B)))
      val solved = ClueSolver.toTheRightOf(game)
      solved.board(1).cells.count({case Unanswered(l) if l.contains(A) => true; case _ => false}) mustEqual 1
    }

    "eliminate choices to the right of an LtR clue" in {
      val game = empty(_.answer(1, 2, D), leftToRight = List(LeftToRight(R0C, R1D)))
      val solved = ClueSolver.toTheRightOf(game)
      solved.board(0, 2) must beLike {case Unanswered(l) => l must not contain(C)}
      solved.board(0, 3) must beLike {case Unanswered(l) => l must not contain(C)}
      solved.board(0, 4) must beLike {case Unanswered(l) => l must not contain(C)}
      solved.board(0, 5) must beLike {case Unanswered(l) => l must not contain(C)}
      solved.leftToRight must beEmpty
    }

    "pick adjacent column given adjCol clue" in {
      val game = empty(_.answer(1, 1, A), adjCol = List(AdjCol(R1A, R0B)))
      val solved = ClueSolver.adjacentCol(game)
      solved.board(0, 0) must beLike {case Unanswered(l) => l must contain(B)}
      solved.board(0, 2) must beLike {case Unanswered(l) => l must contain(B)}
      solved.board(0, 1) must beLike {case Unanswered(l) => l must not contain(B)}
      solved.board(0, 3) must beLike {case Unanswered(l) => l must not contain(B)}
      solved.board(0, 4) must beLike {case Unanswered(l) => l must not contain(B)}
      solved.board(0, 5) must beLike {case Unanswered(l) => l must not contain(B)}


      val solved2 = ClueSolver.adjacentCol(empty(_.answer(1, 1, A), adjCol = List(AdjCol(R0B, R1A))))
      solved2.board(0, 0) must beLike {case Unanswered(l) => l must contain(B)}
      solved2.board(0, 2) must beLike {case Unanswered(l) => l must contain(B)}
      solved2.board(0, 1) must beLike {case Unanswered(l) => l must not contain(B)}
    }

    "eliminate adjacent column given adjCol clue" in {
      val game = empty(_.dismiss(0, 0, A).dismiss(0, 1, A).dismiss(0, 2, A), adjCol = List(AdjCol(R0A, R1B)))
      val solved = ClueSolver.adjacentCol(game)
      solved.board(1, 0) must beLike {case Unanswered(l) => l must not contain(B)}
      solved.board(1, 1) must beLike {case Unanswered(l) => l must not contain(B)}
      solved.board(1, 2) must beLike {case Unanswered(l) => l must contain(B)}

    }
  }

  "Solver" should {
    "solve single-option-remaining board" in {
      Solver(1)(full(_.set(0, 0, List(A)))).isSolved must beTrue
    }

    "eliminate edges for 4 cells left of LtR and pick single remaining cell" in {
      Solver(2)(empty(_.answer(1, 4, A), leftToRight = List(LeftToRight(R1A, R0F)))).board(0, 5) mustEqual Answered(F)
    }

    "eliminate same-col conflicts, pick single-remaining, and then solve same-col" in {
      val g = empty(
        _.answer(0, 1, A).dismiss(1, 1, C, D, E, F),
        sameCol = List(SameCol(R0D, R1A)),
        leftToRight = List(LeftToRight(R1A, R1B)),
        adjCol = List(AdjCol(R0D, R2F))
      )

      Solver(4)(g).board(2, 1) mustEqual Answered(F)
    }


  }
}