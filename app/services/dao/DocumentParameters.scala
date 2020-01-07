package services.dao

import com.blackmorse.controlsystem.model.ControlKey
import com.google.inject.Inject
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import slick.jdbc.MySQLProfile.api._
import slick.lifted.Tag

import scala.concurrent.ExecutionContext
class DocumentParametersDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
                                     (implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {
  val documentParameters = TableQuery[DocumentParametersTableDef]

  def addParametersToDocument(parameters: Map[ControlKey, String], documentId: Int) = {
    val documentParameterEntities = parameters.map{case(key, value) => DocumentParameterEntity(0, documentId, key.code, value)}
    dbConfig.db.run(documentParameters ++= documentParameterEntities)
  }

  def deleteDocumentParameters(documentId: Int) =
    dbConfig.db.run(documentParameters.filter(_.documentId === documentId).delete)
}

case class DocumentParameterEntity(id: Long, documentId: Int, parameterId: Int, value: String)

class DocumentParametersTableDef(tag: Tag) extends Table[DocumentParameterEntity](tag, "document_parameters") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def documentId = column[Int]("document_id")
  def parameterId = column[Int]("parameter_id")
  def parameterValue = column[String]("parameter_value")

  override def * = (id, documentId, parameterId, parameterValue) <> (DocumentParameterEntity.tupled, DocumentParameterEntity.unapply)
}
