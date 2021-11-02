package services.dao

import com.blackmorse.controlsystem.model.{Document, FileNameParameters}
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
    val documentEntity = DocumentEntity(0,
      document.number,
      document.fileNameParameters.engineNumber, document.fileNameParameters.objectName,
      document.fileNameParameters.objectEngineNumber, document.fileNameParameters.lang,
      document.fileNameParameters.date, document.fileNameParameters.revision)
    dbConfig.db.run((documents returning documents.map(_.id) ) += documentEntity)
  }

//  def getDocumentByName(name: String): Future[Option[DocumentEntity]] =
//    dbConfig.db.run( documents.filter(_.name === name).take(1).result )
//      .map(_.headOption)

  def getDocumentByParameters(fileNameParameters: FileNameParameters) =
    dbConfig.db.run(
      documents
        .filter(_.engineNumber === fileNameParameters.engineNumber )
        .filter(_.objectName === fileNameParameters.objectName)
        .filter(_.objectEngineNumber === fileNameParameters.objectEngineNumber)
        .filter(_.lang === fileNameParameters.lang)
      .take(1).result
    ).map(_.headOption)

  def getDocumentById(id: Int): Future[Option[DocumentEntity]] =
    dbConfig.db.run( documents.filter(_.id === id).result ).map(_.headOption)

  def deleteDocumentById(id: Int) =
    dbConfig.db.run( documents.filter(_.id === id).delete )

  def getAllDocuments(): Future[Seq[DocumentEntity]] =
    dbConfig.db.run(documents.result)
}

//case class DocumentEntity(id: Int, number: Int, name: String, date:String)
case class DocumentEntity(id: Int, number: Int, engineNumber: Int, objectName: String,
                          objectEngineNumber: Int, lang: String, date:String, revision: Int) {
  override def toString: String = {
    s"""номер $id, движок номер ${engineNumber}, объект ${objectName}, дата ${date}, ревизия ${revision}"""
  }
}

object DocumentEntity {
  def tupled = (DocumentEntity.apply _).tupled
}

class DocumentsTableDef(tag: Tag) extends Table[DocumentEntity](tag, "documents") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def number = column[Int]("number")
  def engineNumber = column[Int]("engine_number")
  def objectName = column[String]("object_name")
  def objectEngineNumber = column[Int]("object_engine_number")
  def lang = column[String]("lang")
  def date = column[String]("document_date")
  def revision = column[Int]("revision")

  override def * =
    (id, number, engineNumber, objectName, objectEngineNumber, lang, date, revision) <> (DocumentEntity.tupled, DocumentEntity.unapply)
}