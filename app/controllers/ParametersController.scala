package controllers

import javax.inject.Inject
import play.api.libs.json.Json
import play.api.mvc.{BaseController, ControllerComponents}
import services.ParametersService
import services.dao.ParameterEntity

import scala.concurrent.ExecutionContext

class ParametersController @Inject()(val controllerComponents: ControllerComponents,
                            val parametersService: ParametersService)
                            (implicit executionContext: ExecutionContext)
                      extends BaseController {
  implicit val writes = Json.writes[ParameterEntity]

  def allParameters() = Action.async  { implicit request =>
    parametersService.getAllParameters.map(allKeys =>
    Ok(Json.toJson(allKeys)))
  }
}
