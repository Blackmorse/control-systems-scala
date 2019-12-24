package com.blackmorse.controlsystem.pdf

import com.blackmorse.controlsystem.model.{ControlKey, Document}
import org.apache.pdfbox.pdmodel.PDDocument
import resource.managed
import org.apache.pdfbox.text.PDFTextStripperByArea
import java.awt.geom.Rectangle2D
import java.util.regex.Pattern

import scala.collection.mutable

class PdfParser(val parameters: Map[Int, ControlKey]) {
  private val pattern = Pattern.compile("^\\d{5}")

  def parse(inputArray: Array[Byte]) : Document = {


    (for (doc <- managed(PDDocument.load(inputArray))) yield {
      val data = mutable.Map[ControlKey, String]()
      val region = new Rectangle2D.Double(0, 270, 500, 500)
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
    }).acquireAndGet{case(docNumber, parameters) => Document(docNumber, parameters.toMap)}
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

}
