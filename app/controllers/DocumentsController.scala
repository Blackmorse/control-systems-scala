package controllers

import com.google.inject.Inject
import play.api.mvc.{BaseController, ControllerComponents}
import play.mvc.Action
import services.ParametersService

import scala.concurrent.{ExecutionContext, Future}

class DocumentsController @Inject()(val controllerComponents: ControllerComponents,
                                    val parametersService: ParametersService)
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
    parametersService.getAllDocuments().map(seq => Ok(views.html.allDocuments(seq)))
  }
}
