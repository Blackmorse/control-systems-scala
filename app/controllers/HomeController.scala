package controllers

import java.io.{FileInputStream, FileOutputStream}
import java.nio.file.{Files, Paths}

import com.blackmorse.controlsystem.pdf.PdfParser
import javax.inject._
import play.api._
import play.api.mvc._
import services.ParametersService

import scala.concurrent.ExecutionContext

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents,
                               val parametersService: ParametersService)
                              (implicit executionContext: ExecutionContext)extends BaseController {

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

  def upload = Action(parse.multipartFormData) { request =>
    request.body
      .file("picture")
      .map { picture =>
        // only get the last part of the filename
        // otherwise someone can send a path like ../../home/foo/bar.txt to write to other files on the system
        val filename    = Paths.get(picture.filename).getFileName
        val fileSize    = picture.fileSize
        val contentType = picture.contentType

        val path = Paths.get(s"/tmp/picture/tmp.file")
        picture.ref.copyTo(path, replace = true)

//        new FileInputStream(picture.ref).r

        println(Files.readAllBytes(path))


        Ok("File uploaded")
      }
      .getOrElse {
        Redirect(routes.HomeController.index).flashing("error" -> "Missing file")
      }
  }
}
