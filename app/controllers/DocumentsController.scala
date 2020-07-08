package controllers

import com.google.inject.Inject
import play.api.libs.json.Json
import play.api.mvc.{BaseController, ControllerComponents}
import services.{DocumentsService, ParametersService}

import scala.concurrent.ExecutionContext

class DocumentsController @Inject()(val controllerComponents: ControllerComponents,
                                    val parametersService: ParametersService,
                                    val documentsService: DocumentsService)
                                   (implicit executionContext: ExecutionContext)
    extends BaseController {

  def documents(ids: Seq[Int]) = Action.async { implicit request =>

    parametersService.getAllControlKeys.flatMap(allControlKeys =>
      parametersService.getDocumentsByIds(ids)
        .map(document => Ok(views.html.documentsCompare(document, allControlKeys, None)))
    )
  }

  def deleteDocument(id: Int) = Action.async { implicit request =>
    parametersService.deleteDocument(id).map(r => Ok(""))
  }

  def allDocuments() = Action.async { implicit request =>
    documentsService.getAllDocuments().map(seq => Ok(views.html.allDocuments(seq)))
  }

  def allDocumentsRest() = Action.async { implicit request =>
    documentsService.getAllDocuments().map(seq => Ok(Json.toJson(seq)))
  }
}
