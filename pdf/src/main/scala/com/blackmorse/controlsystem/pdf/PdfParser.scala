package com.blackmorse.controlsystem.pdf

import com.blackmorse.controlsystem.model.{ControlKey, Document, FileNameParameters}
import org.apache.pdfbox.pdmodel.PDDocument
import resource.managed
import org.apache.pdfbox.text.PDFTextStripperByArea
import java.awt.geom.Rectangle2D
import java.util.regex.Pattern

import scala.collection.mutable

class PdfParser(val parameters: Map[Int, ControlKey]) {
  private val pattern = Pattern.compile("^\\d{5}")



  def parse(inputArray: Array[Byte], fileName: String) : Document = {
    val fileNameParameters = PdfParser.parseName(fileName)
    (for (doc <- managed(PDDocument.load(inputArray))) yield {
      val data = mutable.Map[ControlKey, String]()
      val region = new Rectangle2D.Double(0, 200, 500, 700)
      val regionName = "region"

      val stripper = new PDFTextStripperByArea
      stripper.addRegion("header", new Rectangle2D.Double(0, 0, 500, 270))
      stripper.extractRegions(doc.getPage(0))

      val header = stripper.getTextForRegion("header")

      val substr = header.substring(header.indexOf("Номер:"))
      val docNumber = """\d+""".r.findFirstMatchIn(substr)
        .map(matc => substr.substring(matc.start, matc.end).toInt)
        .getOrElse(throw new Exception("No document number"))

      for(i <- 0 until doc.getNumberOfPages) {

        val page = doc.getPage(i)
        val stripper = new PDFTextStripperByArea
        stripper.addRegion(regionName, region)
        stripper.extractRegions(page)
        val text = stripper.getTextForRegion(regionName)
        val entries = text.split(System.lineSeparator())

        for (entry <- entries) {
          println(entry)
          val matcher = pattern.matcher(entry)
          if (matcher.find) {
            val code = Integer.valueOf(entry.substring(0, 5).trim)
            parameters.get(code).foreach(controlKey => {
              val value = PdfParser.extractValue(PdfParser.trimWhitespaces(entry))
              data.put(controlKey, value)
            })
          }
        }
      }
      (docNumber, data)
    }).acquireAndGet{case(docNumber, documentParameters) => {
      val extendedParameters = documentParameters ++ Seq(
        parameters(-1) -> fileNameParameters.engineNumber.toString, //Номер двигателя
        parameters(-2) -> fileNameParameters.objectName, //Имя объекта
        parameters(-3) -> fileNameParameters.objectEngineNumber.toString, //Номер двигателя на объекте
        parameters(-4) -> fileNameParameters.lang,//Язык
        parameters(-5) -> fileNameParameters.date,//Дата
        parameters(-6) -> fileNameParameters.revision.toString//Ревизия
      )
      Document(0, docNumber, extendedParameters.toMap, fileNameParameters)
    }}
  }
}

object PdfParser {
  def trimWhitespaces(value: String) = value.replaceAll("(^\\h*)|(\\h*$)", "")

  def extractValue(s: String): String = {
    val numberPattern = """[-]{0,1}\d+""".r
    val lastNumberStart = numberPattern.findAllMatchIn(s).toList.last.start
    val valueIndex = if (s(lastNumberStart - 1) == '.' || s(lastNumberStart - 1) == ',') {

      numberPattern.findAllMatchIn(s.substring(0, lastNumberStart - 1)).toList.last.start
    } else lastNumberStart
    s.substring(valueIndex, s.length)
  }

  def parseName(fileName: String): FileNameParameters = {
    val filenameWithoutExtension = fileName.substring(0, fileName.length - 4)

    val engineSplit = filenameWithoutExtension.split("_", 2)



    val engineNumber = engineSplit(0).toInt

    val objectNameMatch = "_M[0-9]+_".r.findFirstMatchIn(engineSplit(1))
      .getOrElse(throw new RuntimeException("Illegal name of file"))

    val objectName = engineSplit(1).substring(0, objectNameMatch.start)
    val objectEngineNumber = engineSplit(1)
      .substring(objectNameMatch.start + 2, objectNameMatch.end - 1)
      .toInt

    val split = engineSplit(1).substring(objectNameMatch.end).split("_")

    val lang = split(1)
    val yearAndRevision = split(2).split("#")

    val (date, revision) =if(yearAndRevision.length == 1) (yearAndRevision(0), 1) else (yearAndRevision(0), yearAndRevision(1).toInt)

    FileNameParameters(engineNumber, objectName, objectEngineNumber, lang, date, revision)
  }
}
