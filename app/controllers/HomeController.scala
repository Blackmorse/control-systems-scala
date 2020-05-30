package controllers

import java.nio.file.{Files, Paths}

import com.blackmorse.controlsystem.pdf.PdfParser
import javax.inject._
import play.api.mvc._
import services.ParametersService

import scala.concurrent.{ExecutionContext, Future}

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents,
                               val parametersService: ParametersService)
                              (implicit executionContext: ExecutionContext) extends BaseController {

  private val pdfParser = parametersService.getAllControlKeys.map(new PdfParser(_))

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

        parametersService.getAllControlKeys.flatMap(allControlKeys =>
          pdfParser.flatMap(parser => {
            val document = parser.parse(bytes, filename.toString)

            parametersService.updateDocument(document)
              .map{case(id, msg) => Ok(views.html.documentsCompare(Seq(document), allControlKeys, Some(msg)))}
          })
        )
      }
      .getOrElse(Future(InternalServerError("Some error")))
  }
}
