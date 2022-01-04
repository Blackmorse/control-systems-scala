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

case class DocumentChart(document: Document, percents: Seq[Double], temperatures: Seq[Int])

object DocumentChart {
  implicit val writer = Json.writes[DocumentChart]
}

case class CharacteristicChart(documentCharts: Seq[DocumentChart])

object CharacteristicChart {
  implicit val writes = Json.writes[CharacteristicChart]
}

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


  def searchDocumentsWithCharts(searchParams: Seq[String]) = Action.async{ implicit  request =>
    val params = searchParams.map(searchParam => {
      val split = searchParam.split("=")
      (split(0).toInt, if(split.length > 1) split(1) else "")
    })

//    implicit val writes = Json.writes[CharacteristicChart]

    parametersService.getDocumentsByParameters(params.head._1, params.head._2, params(1)._1, params(1)._2)
      .map(documents => Ok(Json.toJson(chart(documents))))
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
