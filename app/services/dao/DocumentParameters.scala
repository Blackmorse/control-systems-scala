package services.dao

import com.google.inject.Inject
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import slick.jdbc.MySQLProfile.api._
import slick.lifted.Tag

import scala.concurrent.ExecutionContext
class DocumentParametersDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
                                     (implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {
  val documentParameters = TableQuery[DocumentParametersTableDef]
}

case class DocumentParameterEntity(id: Int, documentId: Int, parameterId: Int, value: String)

class DocumentParametersTableDef(tag: Tag) extends Table[DocumentParameterEntity](tag, "document_parameters") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def documentId = column[Int]("document_id")
  def parameterId = column[Int]("parameter_id")
  def parameterValue = column[String]("parameter_value")

  override def * = (id, documentId, parameterId, parameterValue) <> (DocumentParameterEntity.tupled, DocumentParameterEntity.unapply)
}
