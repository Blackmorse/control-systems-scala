package com.blackmorse.controlsystem.pdf

import java.nio.file.{Files, Paths}

import com.blackmorse.controlsystem.model.ControlKey

import org.scalatest.funsuite.AnyFunSuiteLike

class PdfParserTest extends AnyFunSuiteLike {
  test("test parse document") {
    val file = getClass.getResource("/2214692_Penza_6_M6_Parametrs_RU_190509.pdf").getPath
    val arr = Files.readAllBytes(Paths.get(file))

    val aggregateKey = ControlKey(10000, "Агрегат No.")
    val sensetiveKey = ControlKey(23005, "Чувствительность")
    val powerKey = ControlKey(13101, "Номинальная мощность")
    val maxAngleKey = ControlKey(11508, "угол зажигания")

    val map = Map(10000 -> aggregateKey,
      23005 -> sensetiveKey,
      13101 -> powerKey,
      11508 -> maxAngleKey)
    val result = new PdfParser(map).parse(arr)

    println(result)

    assert(result.parameters(aggregateKey) == "6")
    assert(result.parameters(sensetiveKey) == "4,0 U/min/s")
    assert(result.parameters(powerKey) == "2000 kW")
    assert(result.parameters(maxAngleKey) == "-10,000 °")

    assert(result.number == 1579028)
  }

  test("test extract value") {
    assert(PdfParser.extractValue("Some text:     1700 h") == "1700 h")
    assert(PdfParser.extractValue("comma test   90,0 %") == "90,0 %")
    assert(PdfParser.extractValue("Minus test -10,000 °") == "-10,000 °")
    assert(PdfParser.extractValue("long value :   4,0 U/min/s") == "4,0 U/min/s")
  }
}
