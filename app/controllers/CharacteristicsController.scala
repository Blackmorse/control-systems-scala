package controllers

import javax.inject.{Inject, Singleton}
import play.api.Configuration
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.Json
import play.api.mvc.{BaseController, ControllerComponents}
import services.ParametersService
import services.dao.DocumentEntity

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
      _ => Future.successful(Ok("Error")),
      data => Future.successful(Redirect(routes.CharacteristicsController.characteristicsResult(
        data.firstParameterId, data.firstParameterValue, data.secondParameterId, data.secondParameterValue)))
    )
  }

  def characteristicsResult(firstParameterId: Int, firstParameterValue: String,
                            secondParameterId: Int, secondParameterValue: String) = Action.async { implicit request =>

    for (documents <- parametersService.getDocumentsByParameters(firstParameterId, firstParameterValue, secondParameterId, secondParameterValue);
         characteristicChart <- chart(documents))
      yield Ok(views.html.characteristic.charasteristicsResult(documents, characteristicChart))
  }

//  private def chartsData(documentEntities: Seq[DocumentEntity]) /*: Future[Map[DocumentEntity, (CharacteristicChart, CharacteristicChart)]]*/ = {
  //    val allParameterIds = firstPercents ++ firstTemperatures ++ secondPercents ++ secondTemperatures ++ thirdPercents ++ thirdTemperatures ++ Seq(characteristicsParameter)
  //
  //    val documentEntitiesMap = documentEntities.map(de => de.id -> de).toMap
  //
  //    val allParameters = parametersService.getDocumentsParameters(documentEntities.map(_.id), allParameterIds)
  //
  //    allParameters.map(documentParameterEntities => {
  //      documentParameterEntities.groupBy(_.documentId).map { case (docId, listParameters) => {
  //        val charParameter = listParameters.find(_.parameterId == characteristicsParameter).getOrElse(throw new RuntimeException("No charactiristics parameter at the document"))
  //
  //        val (temperatureParameters, percentsParameters) =
  //          if (charParameter.value == "1") {
  //            (firstTemperatures, firstPercents)
  //          } else if (charParameter.value == "2") {
  //            (secondTemperatures, secondPercents)
  //          } else if (charParameter.value == "3") {
  //            (thirdTemperatures, thirdPercents)
  //          } else {
  //            throw new RuntimeException(s"Unknown characteristic number ${charParameter.value}")
  //          }
  //
  //        val listParametersMap = listParameters.map(lp => lp.parameterId -> lp.value).toMap
  //
  //        documentEntitiesMap(docId) -> (CharacteristicChart(temperatureParameters.map(temperatureId => listParametersMap(temperatureId))),
  //          CharacteristicChart(percentsParameters.map(percentId => listParametersMap(percentId))))
  //      }
  //      }
  //    })
  //  }

    def chart(documentEntities: Seq[DocumentEntity]) = {

      val allParameterIds = firstPercents ++ firstTemperatures ++ secondPercents ++ secondTemperatures ++ thirdPercents ++ thirdTemperatures ++ Seq(characteristicsParameter)
      val allParameters = parametersService.getDocumentsParameters(documentEntities.map(_.id), allParameterIds)

      val documentEntitiesMap = documentEntities.map(de => de.id -> de).toMap

      allParameters.map(_.groupBy(_.documentId).map { case (docId, listParameters) => {
        val document = documentEntitiesMap(docId)
        val characteristicParameter = listParameters.find(_.parameterId == characteristicsParameter).getOrElse(throw new RuntimeException("No characteristics parameter at the document"))

        val (temperatureParameters, percentsParameters) = getActualParameters(characteristicParameter.value)

        val listParametersMap = listParameters.map(lp => lp.parameterId -> lp.value).toMap

        val temperatures = temperatureParameters.map(temperatureId => convertTemperature(listParametersMap(temperatureId)))
        val percents = percentsParameters.map(percentId => convertPercent(listParametersMap(percentId)))

        DocumentChart(document, percents, temperatures)
      }
      }).map(documentCharts => CharacteristicChart(documentCharts.toSeq))
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

case class DocumentChart(documentEntity: DocumentEntity, percents: Seq[Double], temperatures: Seq[Int])

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
      "firstParameterValue" -> text,
      "secondParameterId" -> number,
      "secondParameterValue" -> text
    )(CharacteristicsForm.apply)(CharacteristicsForm.unapply)
  )
}
