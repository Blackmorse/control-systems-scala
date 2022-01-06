package services.dao

import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import slick.lifted.Tag
import slick.jdbc.MySQLProfile.api._

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class UsersDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
              (implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {
  val users = TableQuery[UsersTableDef]

  def findUserByName(username: String): Future[Option[UserEntity]] = {
    dbConfig.db.run(
    users
      .filter(_.username === username)
      .take(1)
      .result
    ).map(_.headOption)
  }
}

case class UserEntity(id: Int, username: String, passwordHash: String)

object UserEntity {
  def tupled = (UserEntity.apply _).tupled
}

class UsersTableDef(tag: Tag) extends Table[UserEntity](tag, "users") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def username = column[String]("username", O.Unique)
  def passwordHash = column[String]("password_hash")

  override def * =
    (id, username, passwordHash) <> (UserEntity.tupled, UserEntity.unapply)

}