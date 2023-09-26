ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

Global / onChangedBuildSource := ReloadOnSourceChanges

lazy val plugin = (project in file("."))
  .settings(
    name := "better-equals",
    organization := "org.ainr",
    libraryDependencies ++= Seq(
      scalaOrganization.value % (
        if (scalaVersion.value.startsWith("3"))
          s"scala3-compiler_3"
        else "scala-compiler"
        ) % scalaVersion.value
    )
  )

val tests = project
  .settings(
    scalacOptions ++= {
      val jar = (plugin / Compile / packageBin).value
      Seq(
        s"-Xplugin:${jar.getAbsolutePath}",
        s"-Xplugin-require:better-equals",
        s"-Jdummy=${jar.lastModified}"
      ) // borrowed from bm4
    },
    libraryDependencies ++= Seq("org.scalameta" %% "munit" % "0.7.29" % Test),
    Compile / doc / sources := Seq()
  )


val betterEquals =
  project
    .in(file("."))
    .settings(name := "root")
    .settings(
      (publish / skip) := true,
    )
    .aggregate(plugin, tests)
