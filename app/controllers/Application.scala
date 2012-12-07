package controllers

import play.api._
import play.api.mvc._
import game._

object Application extends Controller {

  def index = Action {
    val answered = Game(Board.random)
    val game = Desolver(10)(answered)
    Ok(views.html.index(game))
  }
}
