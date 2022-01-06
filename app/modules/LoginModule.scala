package modules

import com.google.inject.AbstractModule
import play.api.libs.concurrent.AkkaGuiceSupport
//import play.libs.akka.AkkaGuiceSupport
import services.UsersService

class LoginModule extends AbstractModule with AkkaGuiceSupport {
  override def configure(): Unit = {
    bindActor[UsersService]("users-service")
  }
}
