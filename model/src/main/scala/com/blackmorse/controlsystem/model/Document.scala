package com.blackmorse.controlsystem.model

import play.api.libs.json.Json

case class FileNameParameters(engineNumber: Int, objectName: String, objectEngineNumber: Int,
                              lang: String, date: String, revision: Int) {
  override def toString: String = {
    s"${engineNumber}_${objectName}_M${objectEngineNumber}_${lang}_$date#$revision"
  }
}

object Document {
  implicit val writesFileNameParameter = Json.writes[FileNameParameters]
  implicit val writesControlKey = Json.writes[ControlKey]
  implicit val writesDocument = Json.writes[Document]
}

case class Document(id: Int, number: Int, parameters: Map[ControlKey, String],
                    fileNameParameters: FileNameParameters)