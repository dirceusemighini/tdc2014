name := "SprayTDC-2014"

scalaVersion := "2.10.4"

version := "1.0" 

libraryDependencies += "io.spray" % "spray-routing" % "1.3.1" 

libraryDependencies += "io.spray" % "spray-caching" % "1.3.1" 

libraryDependencies += "io.spray" % "spray-can" % "1.3.1"

libraryDependencies += "com.typesafe.akka"                       %%  "akka-actor"                  % "2.3.4"

libraryDependencies += "com.typesafe.akka"                       %%  "akka-slf4j"                  % "2.3.4"

libraryDependencies += "com.typesafe.akka"                       %%  "akka-testkit"                % "2.3.4"

libraryDependencies += "com.livestream" %% "scredis" % "2.0.0-RC1"

libraryDependencies += "net.debasishg" % "redisclient_2.10" % "2.13"