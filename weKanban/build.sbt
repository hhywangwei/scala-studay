
jetty(port=9090)

libraryDependencies ++= Seq(
    "org.scalaz" %% "scalaz-core" % scalazVersion,
    "org.scalaz" %% "scalaz-http" % scalazVersion,
    "com.h2database" % "h2" % h2Version,
    "org.squeryl" % "squeryl_2.10" % squerylVersion
 )