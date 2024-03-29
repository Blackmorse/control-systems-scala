package services

import com.blackmorse.controlsystem.model.{ControlKey, Document, FileNameParameters}

import javax.inject.Inject
import services.dao.{DocumentEntity, DocumentParametersDAO, DocumentsDAO, ParameterEntity, ParametersDAO}
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.{ExecutionContext, Future}

class ParametersService @Inject() (val parametersDAO: ParametersDAO,
                                   val documentsDAO: DocumentsDAO,
                                   val documentParametersDAO: DocumentParametersDAO)
                                  (implicit executionContext: ExecutionContext) {

  def getAllParameters = parametersDAO.getAllParameters

  lazy val getAllControlKeys = getAllParameters
    .map(seq => seq.map(el => el.id -> ControlKey(el.id, el.name)))
    .map(_.toMap)

  def deleteDocument(id: Int) = {
    documentParametersDAO.deleteDocumentParameters(id)
    documentsDAO.deleteDocumentById(id)
  }

  def updateDocument(document: Document): Future[(Int, String)] = {
    val oldDocumentFuture = documentsDAO.getDocumentByParameters(document.fileNameParameters)

    oldDocumentFuture.flatMap({
      case Some(oldDocument) => for(_ <- documentParametersDAO.deleteDocumentParameters(oldDocument.id);
                                    _ <- documentsDAO.deleteDocumentById(oldDocument.id);
                                    newDocumentId <- documentsDAO.insertDocument(document);
                                    _ <- documentParametersDAO.addParametersToDocument(document.parameters, newDocumentId)
      ) yield (newDocumentId, s"Заменен старый документ ${oldDocument}")
      case None => for(newDocumentId <- documentsDAO.insertDocument(document);
                       _ <- documentParametersDAO.addParametersToDocument(document.parameters, newDocumentId) )
        yield (newDocumentId, "Загружен новый документ")
    })
  }

  def convert(documentId: Int, documentEntity: DocumentEntity): Future[Document] =
    (for(controlKeys <- getAllControlKeys;
         documentParameters <- documentParametersDAO.getDocumentParameters(documentId))
      yield {
        documentParameters.map(dpe => controlKeys(dpe.parameterId) -> dpe.value)
      }).map(_.toMap)
      .map(parametersMap => {
        val fileNameParameters = FileNameParameters(documentEntity.engineNumber,
          documentEntity.objectName, documentEntity.objectEngineNumber,
          documentEntity.lang, documentEntity.date, documentEntity.revision)
        Document(documentEntity.id, documentEntity.number, parametersMap, fileNameParameters)
      })
      
  def getDocumentsByIds(documentIds: Seq[Int]): Future[Seq[Document]] = {
    documentParametersDAO.getDbConfig.db.run(
      documentsDAO.documents.filter(_.id inSet documentIds).result
    )
      .map(documents => documents.map(document => convert(document.id, document)))
          .map(fsd => Future.sequence(fsd)).flatten
  }

  def getDocumentsByParameters(firstParameterId: Int, firstParameterValue: String,
                               secondParameterId: Int, secondParameterValue: String): Future[Seq[Document]] = {
    documentParametersDAO.getDbConfig.db.run(
      documentsDAO.documents.filter(_.id in {
        if(secondParameterValue.trim.isEmpty)
          documentParametersDAO.documentParameters
        else documentParametersDAO.documentParameters.filter(_.documentId in
          documentParametersDAO.documentParameters.filter(_.parameterId === secondParameterId).filter(_.parameterValue === secondParameterValue).map(_.documentId))
      }
         .filter(_.parameterId === firstParameterId).filter(_.parameterValue === firstParameterValue).map(_.documentId)
      ).result

    ).map(documents => documents.map(document => convert(document.id, document)))
      .flatMap(fsd => Future.sequence(fsd))
  }

  def getDocumentsParameters(documentIds: Seq[Int], parameterIds: Seq[Int]) = {
    documentParametersDAO.getDocumentsParameters(documentIds, parameterIds)
  }

  def updateParameter(parameterEntity: ParameterEntity) = {
    parametersDAO.updateParameter(parameterEntity)
  }

  def addParameter(parameterEntity: ParameterEntity) = {
    parametersDAO.addParameter(parameterEntity)
  }
}
