name := "control-systems-scala"

version := "0.1"

scalaVersion in ThisBuild := "2.12.0"

lazy val pdf = project
  .settings(
    assemblySettings,
    libraryDependencies ++= Seq(
      "org.apache.pdfbox" % "pdfbox" % "2.0.16"
    )
  )

lazy val web = project
  .settings(
    assemblySettings
  )
  .dependsOn(pdf)

lazy val global = project
  .in(file("."))
  .settings(
    assembleArtifact := false
  )
  .aggregate(
    pdf,
    web
  )

lazy val assemblySettings = Seq(
  assemblyJarName in assembly := name.value + ".jar",
  assemblyMergeStrategy in assembly := {
    case PathList("META-INF", xs @ _*) => MergeStrategy.discard
    case _                             => MergeStrategy.first
  }
)