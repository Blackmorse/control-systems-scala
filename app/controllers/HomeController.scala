package controllers

import java.io.{FileInputStream, FileOutputStream}
import java.nio.file.{Files, Paths}

import com.blackmorse.controlsystem.model.ControlKey
import com.blackmorse.controlsystem.pdf.PdfParser
import javax.inject._
import play.api._
import play.api.mvc._
import services.ParametersService
import services.dao.{DocumentParametersDAO, DocumentsDAO}

import scala.concurrent.{ExecutionContext, Future}

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents,
                               val parametersService: ParametersService,
                               val documentsDAO: DocumentsDAO,
                               val documentParametersDAO: DocumentParametersDAO)
                              (implicit executionContext: ExecutionContext) extends BaseController {

  val pdfParser = parametersService.getAllParameters
    .map(seq => seq.map(el => el.id -> ControlKey(el.id, el.name))).map(_.toMap).map(new PdfParser(_))


  /**
    * Create an Action to render an HTML page.
    *
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */
  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def parameters() = Action.async { implicit request: Request[AnyContent] =>
    parametersService.getAllParameters.map(seq => Ok(views.html.parameters(seq)))
  }

  def upload = Action.async(parse.multipartFormData) { request =>
    request.body
      .file("file")
      .map { file =>
        val filename = Paths.get(file.filename).getFileName
        //TODO in-memory
        val path = Paths.get(s"/tmp/tmp.file")
        file.ref.copyTo(path, replace = true)

        val bytes = Files.readAllBytes(path)

        pdfParser.flatMap(parser => {
          val document = parser.parse(bytes, filename.toString)
          val oldDocumentFuture = documentsDAO.getDocumentByName(document.name)

          oldDocumentFuture.flatMap({
            case Some(oldDocument) => for(_ <- documentParametersDAO.deleteDocumentParameters(oldDocument.id);
                                          _ <- documentsDAO.deleteDocumentById(oldDocument.id);
                                          newDocumentId <- documentsDAO.insertDocument(document);
                                          _ <- documentParametersDAO.addParametersToDocument(document.parameters, newDocumentId)
                                      ) yield s"Replaces old document ${oldDocument.name} with date ${oldDocument.date}"
            case None => for(newDocumentId <- documentsDAO.insertDocument(document);
                             _ <- documentParametersDAO.addParametersToDocument(document.parameters, newDocumentId) )
                            yield "New Document Uploaded"
          }).map(Ok(_))
        })
      }
      .getOrElse(Future(Ok("Some error")))
  }
}
