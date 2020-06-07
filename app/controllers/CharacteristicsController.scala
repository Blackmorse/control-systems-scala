package controllers

import com.blackmorse.controlsystem.model.{ControlKey, Document, FileNameParameters}
import javax.inject.{Inject, Singleton}
import play.api.Configuration
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.Json
import play.api.mvc.{BaseController, ControllerComponents}
import services.ParametersService
import play.api.data.validation.Constraints._

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CharacteristicsController @Inject()(val controllerComponents: ControllerComponents,
                                          val parametersService: ParametersService,
                                          val config: Configuration)
                                         (implicit executionContext: ExecutionContext)
  extends BaseController with play.api.i18n.I18nSupport {

  private val firstPercents = config.get[Seq[Int]]("control.system.characteristics.first.percents")
  private val firstTemperatures = config.get[Seq[Int]]("control.system.characteristics.first.temperatures")
  private val secondPercents = config.get[Seq[Int]]("control.system.characteristics.second.percents")
  private val secondTemperatures = config.get[Seq[Int]]("control.system.characteristics.second.temperatures")
  private val thirdPercents = config.get[Seq[Int]]("control.system.characteristics.third.percents")
  private val thirdTemperatures = config.get[Seq[Int]]("control.system.characteristics.third.temperatures")

  private val characteristicsParameter = config.get[Int]("control.system.characteristics.parameter")

  def characteristics() = Action.async { implicit request =>

    parametersService.getAllParameters.map(parameters =>
      Ok(views.html.characteristic.characteristics(parameters, CharacteristicsForm.form)))
  }

  def searchCharacteristics = Action.async { implicit request =>
    CharacteristicsForm.form.bindFromRequest().fold(
      formWithErrors => parametersService.getAllParameters.map(parameters =>
        BadRequest(views.html.characteristic.characteristics(parameters, formWithErrors))),//Future.successful(Ok("Error")),
      data => Future.successful(Redirect(routes.CharacteristicsController.characteristicsResult(
        data.firstParameterId, data.firstParameterValue, data.secondParameterId, data.secondParameterValue)))
    )
  }

  def characteristicsResult(firstParameterId: Int, firstParameterValue: String,
                            secondParameterId: Int, secondParameterValue: String) = Action.async { implicit request =>

    for (documents <- parametersService.getDocumentsByParameters(firstParameterId, firstParameterValue, secondParameterId, secondParameterValue))
      yield Ok(views.html.characteristic.charasteristicsResult(documents, chart(documents)))
  }

  def chart(documents: Seq[Document]): CharacteristicChart = {
    val documentCharts = documents.map(document => {
      val characteristicParameter = document.parameters.find(_._1.code == characteristicsParameter).getOrElse(throw new RuntimeException("No characteristics parameter at the document"))
      val (temperatureParameters, percentsParameters) = getActualParameters(characteristicParameter._2)

      val listParametersMap = document.parameters.map{case (controlKey, value) => controlKey.code -> value}.toMap

      val temperatures = temperatureParameters.map(temperatureId => convertTemperature(listParametersMap(temperatureId)))
      val percents = percentsParameters.map(percentId => convertPercent(listParametersMap(percentId)))

      DocumentChart(document, percents, temperatures)
    })
    CharacteristicChart(documentCharts)
  }


  private def getActualParameters(value: String) =
    if (value == "1") {
      (firstTemperatures, firstPercents)
    } else if (value == "2") {
      (secondTemperatures, secondPercents)
    } else if (value == "3") {
      (thirdTemperatures, thirdPercents)
    } else {
      throw new RuntimeException(s"Unknown characteristic number $value")
    }

  private def convertTemperature(temperature: String): Int = temperature.replace("°C", "").trim.toInt

  private def convertPercent(percent: String): Double = percent.replace("%", "").replace(",", ".").trim.toDouble
}

case class CharacteristicChart(documentCharts: Seq[DocumentChart])

case class DocumentChart(document: Document, percents: Seq[Double], temperatures: Seq[Int])

object DocumentChart {
  implicit val writer = Json.writes[DocumentChart]
}


object CharacteristicChart {
  implicit val userImplicitWrites = Json.writes[CharacteristicChart]
}

case class CharacteristicsForm(firstParameterId: Int, firstParameterValue: String,
                               secondParameterId: Int, secondParameterValue: String)

object CharacteristicsForm {
  val form: Form[CharacteristicsForm] = Form(
    mapping(
      "firstParameterId" -> number,
      "firstParameterValue" -> text.verifying(nonEmpty),
      "secondParameterId" -> number,
      "secondParameterValue" -> text
    )(CharacteristicsForm.apply)(CharacteristicsForm.unapply)
  )
}
