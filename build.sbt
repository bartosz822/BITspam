name := """bit-spam"""
organization := "pl.edu.knbit"
version := "0.1"
scalaVersion := "2.11.11"

val playDependencies = {

  val scalaTestVersion = "2.0.0"

  Seq(
    "org.scalatestplus.play" %% "scalatestplus-play" % scalaTestVersion % Test,
    ws
  )
}

val levelDBDependencies = {

  Seq(
    "org.iq80.leveldb" % "leveldb" % "0.7",
    "org.fusesource.leveldbjni" % "leveldbjni-all" % "1.8"
  )
}


val akkaDependencies = {

  val akkaVersion = "2.5.2"

  Seq(
    "com.typesafe.akka" %% "akka-persistence" % akkaVersion
  )

}

libraryDependencies += filters
libraryDependencies ++= playDependencies
libraryDependencies ++= akkaDependencies
libraryDependencies ++= levelDBDependencies


lazy val root = (project in file(".")).enablePlugins(PlayScala)