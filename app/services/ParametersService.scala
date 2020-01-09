package services

import com.blackmorse.controlsystem.model.{ControlKey, Document}
import javax.inject.Inject
import services.dao.{DocumentEntity, DocumentParametersDAO, DocumentsDAO, ParametersDAO}
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.{ExecutionContext, Future}

class ParametersService @Inject() (val parametersDAO: ParametersDAO,
                                   val documentsDAO: DocumentsDAO,
                                   val documentParametersDAO: DocumentParametersDAO)
                                  (implicit executionContext: ExecutionContext) {

  lazy val getAllParameters = parametersDAO.getAllParameters

  lazy val getAllControlKeys = getAllParameters
    .map(seq => seq.map(el => el.id -> ControlKey(el.id, el.name)))
    .map(_.toMap)

  def updateDocument(document: Document): Future[(Int, String)] = {
    val oldDocumentFuture = documentsDAO.getDocumentByName(document.name)

    oldDocumentFuture.flatMap({
      case Some(oldDocument) => for(_ <- documentParametersDAO.deleteDocumentParameters(oldDocument.id);
                                    _ <- documentsDAO.deleteDocumentById(oldDocument.id);
                                    newDocumentId <- documentsDAO.insertDocument(document);
                                    _ <- documentParametersDAO.addParametersToDocument(document.parameters, newDocumentId)
      ) yield (newDocumentId, s"Заменен старый документ ${oldDocument.name} с датой ${oldDocument.date}")
      case None => for(newDocumentId <- documentsDAO.insertDocument(document);
                       _ <- documentParametersDAO.addParametersToDocument(document.parameters, newDocumentId) )
        yield (newDocumentId, "New Document Uploaded")
    })
  }

  def getDocument(documentId: Int): Future[Document] = {
      documentsDAO.getDocumentById(documentId).flatMap{case Some(documentEntity) =>
        (for(controlKeys <- getAllControlKeys;
          documentParameters <- documentParametersDAO.getDocumentParameters(documentId))
          yield {
            documentParameters.map(dpe => controlKeys(dpe.parameterId) -> dpe.value)
        }).map(_.toMap)
          .map(parametersMap => Document(documentEntity.number, parametersMap, documentEntity.name, documentEntity.date))
      }
  }

  def getAllDocuments(): Future[Seq[DocumentEntity]] = documentsDAO.getAllDocuments()

  def getDocumentsByParameters(firstParameterId: Int, firstParameterValue: String,
                               secondParameterId: Int, secondParameterValue: String): Future[Seq[DocumentEntity]] = {
    documentParametersDAO.getDbConfig.db.run(
      documentsDAO.documents.filter(_.id in {
        if(secondParameterValue.trim.isEmpty)
          documentParametersDAO.documentParameters
        else documentParametersDAO.documentParameters.filter(_.documentId in
          documentParametersDAO.documentParameters.filter(_.parameterId === secondParameterId).filter(_.parameterValue === secondParameterValue).map(_.documentId))
      }
         .filter(_.parameterId === firstParameterId).filter(_.parameterValue === firstParameterValue).map(_.documentId)
      ).result
    )
  }

  def getDocumentsParameters(documentIds: Seq[Int], parameterIds: Seq[Int]) = {
    documentParametersDAO.getDocumentsParameters(documentIds, parameterIds)
  }
}
