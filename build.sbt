name := "control-systems-scala"

version := "0.1"

scalaVersion in ThisBuild := "2.12.0"

lazy val commonDependencies = Seq(
  "org.scalatest" %% "scalatest" % "3.1.0" % Test
)

lazy val model = project
  .settings(
    assemblySettings
  )

lazy val pdf = project
  .settings(
    assemblySettings,
    libraryDependencies ++= commonDependencies ++ Seq(
      "org.apache.pdfbox" % "pdfbox" % "2.0.16",
      "com.jsuereth" % "scala-arm_2.12" % "2.0" % Compile
    )
  )
  .dependsOn(model)
lazy val web = project
  .settings(
    assemblySettings
  )
  .dependsOn(
    pdf,
    model
  )

lazy val global = project
  .in(file("."))
  .settings(
    assembleArtifact := false
  )
  .aggregate(
    model,
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