package controllers

import javax.inject.{Inject, Singleton}
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{BaseController, ControllerComponents}
import services.ParametersService

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CharacteristicsController @Inject()(val controllerComponents: ControllerComponents,
                                          val parametersService: ParametersService)
                                         (implicit executionContext: ExecutionContext)
                      extends BaseController with play.api.i18n.I18nSupport {

  def characteristics() = Action.async { implicit  request =>

    parametersService.getAllParameters.map(parameters =>
    Ok(views.html.characteristic.characteristics(parameters, CharacteristicsForm.form)))
  }

  def searchCharacteristics = Action.async { implicit request =>
    CharacteristicsForm.form.bindFromRequest().fold(
      _ => Future.successful(Ok("Error")),
      data =>  Future.successful(Redirect(routes.CharacteristicsController.characteristicsResult(
        data.firstParameterId, data.firstParameterValue, data.secondParameterId, data.secondParameterValue)))
    )
  }

  def characteristicsResult(firstParameterId: Int, firstParameterValue: String,
    secondParameterId: Int, secondParameterValue: String) = Action.async{ implicit request =>

    parametersService.getDocumentsByParameters(firstParameterId, firstParameterValue,
      secondParameterId, secondParameterValue).map(seq => Ok(views.html.characteristic.charasteristicsResult(seq)))
  }
}

case class CharacteristicsForm(firstParameterId: Int, firstParameterValue: String,
                               secondParameterId: Int, secondParameterValue: String)

object CharacteristicsForm {


  val form: Form[CharacteristicsForm] = Form (
    mapping (
      "firstParameterId" -> number,
      "firstParameterValue" -> text,
      "secondParameterId" -> number,
      "secondParameterValue" -> text
    )(CharacteristicsForm.apply)(CharacteristicsForm.unapply)
  )
}
