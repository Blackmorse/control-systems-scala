package services.dao

import com.google.inject.Inject
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import slick.jdbc.MySQLProfile.api._
import slick.lifted.Tag

import scala.concurrent.{ExecutionContext, Future}

class ParametersDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
                             (implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {

  val parameters = TableQuery[ParametersTableDef]

  def getAllParameters: Future[Seq[ParameterEntity]] = dbConfig.db.run(parameters.result)
}

case class ParameterEntity(id: Int, name: String, unit: String, defaultValue: String)

class ParametersTableDef(tag: Tag) extends Table[ParameterEntity](tag, "parameters") {
  def id = column[Int]("id", O.PrimaryKey)
  def name = column[String]("name")
  def unit = column[String]("unit")
  def defaultValue = column[String]("default_value")

  override def * = (id, name, unit, defaultValue) <> (ParameterEntity.tupled, ParameterEntity.unapply)
}
