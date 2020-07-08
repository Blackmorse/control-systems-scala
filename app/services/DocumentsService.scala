package services

import com.blackmorse.controlsystem.model.Document
import javax.inject.Inject
import services.dao.DocumentsDAO

import scala.concurrent.{ExecutionContext, Future}

class DocumentsService @Inject() (val documentsDAO: DocumentsDAO,
                                  val parametersService: ParametersService)
                                 (implicit executionContext: ExecutionContext){
  def getAllDocuments(): Future[Seq[Document]] = documentsDAO.getAllDocuments()
    .map(documentsEntities => documentsEntities.map(documentsEntity => parametersService.convert(documentsEntity.id, documentsEntity)))
    .flatMap(fsd => Future.sequence(fsd))
}
