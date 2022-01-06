package controllers

import play.api.mvc.Results.Forbidden
import java.util.concurrent.TimeUnit
import services.UsersService.CheckToken
import play.api.mvc.{ActionBuilderImpl, BodyParsers, Request, Result}
import services.UsersService
import akka.pattern.ask

import javax.inject.{Inject, Named}
import scala.concurrent.{ExecutionContext, Future}
import akka.actor.ActorRef
import scala.util.Success
import akka.util.Timeout
import java.util.concurrent.TimeUnit

class LoggedInAction @Inject()(parser: BodyParsers.Default,
                              @Named("users-service") usersService: ActorRef)
                              (implicit ec: ExecutionContext)
  extends ActionBuilderImpl(parser) {

  override def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]): Future[Result] = {
    request.headers.get("lg-token") match {
      case None => Future(Forbidden("No token!"))
      case Some(token) =>
        implicit val timeout: Timeout = Timeout(15, TimeUnit.SECONDS)
        (usersService ? CheckToken(token)).mapTo[Either[String, String]]
          .flatMap{
            case Right(_) => block(request)
            case _ => Future(Forbidden("Unauthorized"))
          }
    }
  }
}
