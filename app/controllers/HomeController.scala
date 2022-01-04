package controllers

import java.nio.file.{Files, Paths}

import com.blackmorse.controlsystem.pdf.PdfParser
import javax.inject._
import play.api.libs.json.Json
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

  case class DocumentIdResponse(id: Int, msg: String)

  object DocumentIdResponse {
    implicit val writesFileNameParameter = Json.writes[DocumentIdResponse]
  }

  def uploadRest = Action.async(parse.multipartFormData) { request =>
    println("ttest")
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
              .map{case(id, msg) => Ok(Json.toJson(DocumentIdResponse(id, msg)))}
          })
        )
      }
      .getOrElse(Future(InternalServerError("Some error")))
  }
}
