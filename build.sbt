name := "Kasimo"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies +=  "com.typesafe.akka" %% "akka-actor" % "2.4.12"

resolvers += "Spray" at "http://repo.spray.io"

libraryDependencies += "com.wandoulabs.akka" %% "spray-websocket" % "0.1.4"

//libraryDependencies += "pl.project13.scala" %% "rainbow" % "0.2"