package services.dao

import com.blackmorse.controlsystem.model.Document
import com.google.inject.Inject
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.json.Json
import slick.jdbc.JdbcProfile
import slick.jdbc.MySQLProfile.api._
import slick.lifted.Tag

import scala.concurrent.{ExecutionContext, Future}

class DocumentsDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
                            (implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {
  val documents = TableQuery[DocumentsTableDef]

  def insertDocument(document: Document) = {
    val documentEntity = DocumentEntity(0, document.number, document.name, document.date)
    dbConfig.db.run((documents returning documents.map(_.id) ) += documentEntity)
  }

  def getDocumentByName(name: String): Future[Option[DocumentEntity]] =
    dbConfig.db.run( documents.filter(_.name === name).take(1).result )
      .map(_.headOption)

  def getDocumentById(id: Int): Future[Option[DocumentEntity]] =
    dbConfig.db.run( documents.filter(_.id === id).result ).map(_.headOption)

  def deleteDocumentById(id: Int) =
    dbConfig.db.run( documents.filter(_.id === id).delete )

  def getAllDocuments(): Future[Seq[DocumentEntity]] =
    dbConfig.db.run(documents.result)
}

case class DocumentEntity(id: Int, number: Int, name: String, date:String)

object DocumentEntity {
  implicit val writes = Json.writes[DocumentEntity]
  def tupled = (DocumentEntity.apply _).tupled
}

class DocumentsTableDef(tag: Tag) extends Table[DocumentEntity](tag, "documents") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def number = column[Int]("number")
  def name = column[String]("name")
  def date = column[String]("document_date")

  override def * = (id, number, name, date) <> (DocumentEntity.tupled, DocumentEntity.unapply)
}