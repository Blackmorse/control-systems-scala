package controllers

import akka.actor.ActorRef
import akka.http.scaladsl.model.HttpHeader.ParsingResult.Ok
import play.api.libs.json.{JsSuccess, JsValue, Json, OWrites, Reads}
import play.api.mvc.{Action, BaseController, ControllerComponents}
import services.UsersService
import akka.pattern.ask
import akka.util.Timeout
import services.UsersService.TokenRequest
import views.html.defaultpages.error

import java.util.concurrent.TimeUnit
import javax.inject.{Inject, Named, Singleton}
import scala.concurrent.{ExecutionContext, Future}

case class LoginForm(username: String, password: String)

object LoginForm {
  implicit val reads: Reads[LoginForm] = Json.reads[LoginForm]
}

@Singleton
class LoginController @Inject() (val controllerComponents: ControllerComponents,
                                @Named("users-service") usersService: ActorRef)
                                (implicit executionContext: ExecutionContext) extends BaseController{

  def login(): Action[JsValue] = Action(parse.json).async{ implicit request =>
    val loginForm = request.body.validate[LoginForm] match {
      case JsSuccess(lf, _) => lf
    }
    implicit val timeout: Timeout = Timeout(15, TimeUnit.SECONDS)
    (usersService ? TokenRequest(loginForm.username, loginForm.password))
      .mapTo[Either[String, String]]
      .map {
        case Right(token) => Ok(token)
        case Left(error) => Forbidden(error)
      }
  }

}
