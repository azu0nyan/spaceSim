ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.4"

lazy val utilsLib2 = RootProject(file("../scalaUtils"))
//val main = Project(id = "application", base = file(".")).dependsOn(utilsLib)
lazy val utilsLib = RootProject(file("../scalaAwtDrawing"))
lazy val root = (project in file("."))
  .settings(
    name := "spaceSim"
  )
  .dependsOn(utilsLib)
  .dependsOn(utilsLib2)


libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.14" % "test"