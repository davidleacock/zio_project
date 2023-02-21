ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

lazy val zioVersion = "2.0.0"

//lazy val root = (project in file("."))
//  .settings(
//    name := "zio_project"
//  )

libraryDependencies ++= Seq(
  "dev.zio" %% "zio" % zioVersion,
  "dev.zio" %% "zio-test" % zioVersion,
  "io.d11" %% "zhttp" % "2.0.0-RC10",
  "dev.zio" %% "zio-test-sbt" % zioVersion,
  "dev.zio" %% "zio-json" % "0.3.0-RC8",
  "dev.zio" %% "zio-streams" % zioVersion,
  "dev.zio" %% "zio-test-junit" % zioVersion,
)

testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
