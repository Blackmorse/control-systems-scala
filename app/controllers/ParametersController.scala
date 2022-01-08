package controllers

import javax.inject.Inject
import play.api.libs.json.Json
import play.api.mvc.{BaseController, ControllerComponents}
import services.ParametersService
import services.dao.ParameterEntity

import scala.concurrent.{ExecutionContext, Future}

class ParametersController @Inject()(val controllerComponents: ControllerComponents,
                            val parametersService: ParametersService,
                             val loggedInAction: LoggedInAction)
                            (implicit executionContext: ExecutionContext)
                      extends BaseController {
  implicit val writes = Json.writes[ParameterEntity]

  def allParameters() = Action.async  { implicit request =>
    parametersService.getAllParameters.map(allKeys =>
    Ok(Json.toJson(allKeys)))
  }

  def updateParameter(id: Int, name: String, unit: String, defaultValue: String) = loggedInAction.async{
    if (id <= 0) throw new IllegalArgumentException(s"Нельзя изменить параметр $id")

    val parameterEntity = ParameterEntity(id, name, unit, defaultValue)
    parametersService.updateParameter(parameterEntity)
      .map(_ => Ok(s"$id updated"))
  }

  def addParameter(id: Int, name: String, unit: String, defaultValue: String) = loggedInAction.async {
    if (id <= 0) throw new IllegalArgumentException(s"Нельзя добавить отрицательный параметр $id")
    val parameterEntity = ParameterEntity(id, name, unit, defaultValue)
    parametersService.addParameter(parameterEntity)
      .map(_ => Ok(s"$parameterEntity added"))
  }
}
