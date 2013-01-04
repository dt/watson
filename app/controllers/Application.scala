package controllers

import play.api._
import play.api.mvc._
import game._

object Application extends Controller {

  def index = Action {
    val answered = Game.random
    try {
      val game = Desolver(50)(answered)
      Ok(views.html.index(game))
    } catch {
      case c: ContradictionCreated => { Logger.error("original: \n" + answered.board); throw c }
    }
  }

  def howto = Action {
    Ok(views.html.howto())
  }

  def about = TODO
}
