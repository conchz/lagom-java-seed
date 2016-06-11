organization in ThisBuild := "com.github.lavenderx"

// the Scala version that will be used for cross-compiled libraries
scalaVersion in ThisBuild := "2.11.8"


lazy val helloworldApi = project("helloworld-api")
  .settings(
    version := "1.0-SNAPSHOT",
    libraryDependencies += lagomJavadslApi
  )

lazy val helloworldImpl = project("helloworld-impl")
  .enablePlugins(LagomJava)
  .settings(
    version := "1.0-SNAPSHOT",
    libraryDependencies ++= Seq(
      lagomJavadslPersistence,
      lagomJavadslTestKit
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(helloworldApi)

lazy val hellostreamApi = project("hellostream-api")
  .settings(version := "1.0-SNAPSHOT")
  .settings(
    libraryDependencies += lagomJavadslApi
  )

lazy val hellostreamImpl = project("hellostream-impl")
  .settings(version := "1.0-SNAPSHOT")
  .enablePlugins(LagomJava)
  .dependsOn(hellostreamApi, helloworldApi)
  .settings(
    libraryDependencies += lagomJavadslTestKit
  )

def project(id: String) = Project(id, base = file(id))
  .settings(commonSettings: _*)
  .settings(jacksonParameterNamesJavacSettings: _*) // applying it to every project even if not strictly needed.


lazy val commonSettings = Seq(
  homepage := Some(url("https://github.com/lavenderx/lagom-java-seed")),

  developers := List(Developer(
    "lavenderx",
    "Zongzhi Bai",
    "dolphineor@gmail.com",
    url("https://github.com/lavenderx"))
  ),

  scmInfo := Some(ScmInfo(
    url("https://github.com/lavenderx/lagom-java-seed"),
    "scm:git:git@github.com:lavenderx/lagom-java-seed.git",
    Some("scm:git:git@github.com:lavenderx/lagom-java-seed.git"))
  ),

  licenses := Seq(
    ("MIT", url("https://opensource.org/licenses/MIT"))
  ),

  fork in run := true,

  javacOptions in compile ++= Seq(
    "-encoding", "UTF-8",
    "-source", "1.8",
    "-target", "1.8",
    "-Xlint:unchecked"
  ),

  ivyScala := ivyScala.value map {
    _.copy(overrideScalaVersion = true)
  },

  // avoid some scala specific source directories
  unmanagedSourceDirectories in Compile := Seq((javaSource in Compile).value),
  unmanagedSourceDirectories in Test := Seq((javaSource in Test).value)
)

// See https://github.com/FasterXML/jackson-module-parameter-names
lazy val jacksonParameterNamesJavacSettings = Seq(
  javacOptions in compile += "-parameters"
)
