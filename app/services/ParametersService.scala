package services

import com.blackmorse.controlsystem.model.Document
import javax.inject.Inject
import services.dao.{DocumentsDAO, ParametersDAO}

import scala.concurrent.ExecutionContext

class ParametersService @Inject() (val parametersDAO: ParametersDAO,
                                   val documentsDAO: DocumentsDAO) {

  def getAllParameters = parametersDAO.getAllParameters

  def addNewDocument(document: Document)(implicit executionContext: ExecutionContext) = {
    documentsDAO.deleteDocumentByName(document.name)
      .map(
        oldDocument =>
          oldDocument.map(doc => s"replaced old document with date ${doc.date}").getOrElse("New document uploaded")
      )
  }
}
