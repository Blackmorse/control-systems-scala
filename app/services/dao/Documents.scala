package services.dao

import com.blackmorse.controlsystem.model.Document
import com.google.inject.Inject
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import slick.jdbc.MySQLProfile.api._
import slick.lifted.Tag

import scala.concurrent.ExecutionContext

class DocumentsDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
                            (implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {
  val documents = TableQuery[DocumentsTableDef]

  def insertDocument(document: Document) = {
    val documentEntity = DocumentEntity(0, document.number, document.name, document.date)
    dbConfig.db.run(documents += documentEntity)
  }
}

case class DocumentEntity(id: Int, number: Int, name: String, date:String)

class DocumentsTableDef(tag: Tag) extends Table[DocumentEntity](tag, "documents") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def number = column[Int]("number")
  def name = column[String]("name")
  def date = column[String]("document_date")

  override def * = (id, number, name, date) <> (DocumentEntity.tupled, DocumentEntity.unapply)
}