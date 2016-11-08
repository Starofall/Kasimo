name := "Kasimo"

version := "1.0"

scalaVersion := "2.11.8"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
resolvers += "Typesafe Snapshots" at "http://repo.typesafe.com/typesafe/snapshots/"

libraryDependencies +=  "com.typesafe.akka" %% "akka-actor" % "2.4.12"
libraryDependencies += "com.typesafe.akka" %% "akka-slf4j" % "2.4.12"

resolvers += "Spray" at "http://repo.spray.io"

libraryDependencies += "com.wandoulabs.akka" %% "spray-websocket" % "0.1.4"


libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.1.2"
libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.7"

libraryDependencies += "pl.project13.scala" %% "rainbow" % "0.2"