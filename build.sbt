name := "control-systems-scala"

version := "0.1"

scalaVersion in ThisBuild := "2.12.0"

lazy val commonDependencies = Seq(
  "org.scalatest" %% "scalatest" % "3.1.0" % Test
)

lazy val model = project
  .settings(
    assemblySettings,
    libraryDependencies ++= Seq(
      "com.typesafe.play" % "play-json_2.12" % "2.8.1"
    )
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
  .enablePlugins(PlayScala)
  .settings(
//    assembleArtifact := false
    libraryDependencies ++= Seq(guice,
      "com.typesafe.play" %% "play-slick" % "4.0.0",
      "com.typesafe.play" %% "play-slick-evolutions" % "4.0.0",
      "org.mariadb.jdbc" % "mariadb-java-client" % "2.4.1",
      "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test)
  )
  .aggregate(
    model,
    pdf,
    web
  )
  .dependsOn(pdf)

lazy val assemblySettings = Seq(
  assemblyJarName in assembly := name.value + ".jar",
  assemblyMergeStrategy in assembly := {
    case PathList("META-INF", xs @ _*) => MergeStrategy.discard
    case _                             => MergeStrategy.first
  }
)