package com.blackmorse.controlsystem.model

case class Document(number: Int, parameters: Map[ControlKey, String], name: String, date: String)