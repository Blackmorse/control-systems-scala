package services

import akka.actor.{Actor, Props}
import play.api.Logging
import services.UsersService.{CheckToken, Logout, TokenRequest}
import services.dao.UsersDAO

import java.util.UUID
import javax.inject.Inject
import scala.collection.mutable
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

object UsersService {
  def props = Props[UsersService]

  case class TokenRequest(username: String, password: String)
  case class Logout(token: String)
  case class CheckToken(token: String)
}

class UsersService @Inject() (val usersDAO: UsersDAO)
                             (implicit ec: ExecutionContext) extends Actor with Logging {
  val SALT = "$2a$16$1PbTExsEJZ6Va3XykV6Pne"

  val tokensMap = mutable.Map[String, String]()

  override def receive: Receive = {
    case TokenRequest(username, password) =>
      val sender = this.sender()
      checkCredentialsAndGenerateToken(username, password).onComplete{
        case Success(value) => sender ! value
        case Failure(exception) =>
          logger.error(exception.toString)
          sender ! Left("Unable to connect to DB probably")
      }
    case Logout(token) =>
      tokensMap -= usernameFromToken(token)
      this.sender() ! Right("Successfully logged out")
    case CheckToken(token) =>
      val username = usernameFromToken(token)
      tokensMap.get(username).filter(_ == token) match {
        case None => this.sender() ! Left("InvalidToken")
        case Some(_) => this.sender() ! Right("Success")
      }
  }

  private def usernameFromToken(token: String): String = {
    val index = token.indexOf("-token-")
    token.substring(0, index)
  }

  def checkCredentialsAndGenerateToken(username: String, password: String): Future[Either[String, String]] = {
    import com.github.t3hnar.bcrypt._
    usersDAO.findUserByName(username)
      .map{
        case None => Left("No such user")
        case Some(user) =>
          val passwordHash = s"$username$password".bcryptSafe(SALT)
            .getOrElse(throw new Exception("Unable to encrypt"))
          if (passwordHash == user.passwordHash) {
            val token = s"$username-token-${UUID.randomUUID().toString}"
            tokensMap(username) = token
            Right(token)
          } else {
            Left("Wrong password!")
          }
      }

  }
}
