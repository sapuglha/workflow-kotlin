@file:Suppress("SuspiciousCollectionReassignment")

plugins {
  `java-library`
  `kotlin-jvm`
  published
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
  kotlinOptions {
    // Configure friend paths so the testing module can access internal declarations from the
    // following modules. Note that the IntelliJ Kotlin plugin won't be aware of this configuration
    // so it will still complain about internal accesses across modules, but they will actually
    // compile just fine. See https://youtrack.jetbrains.com/issue/KT-20760.
    val friendModule = project(":workflow-core")

    // Pointing to jar instead of classes dir since :workflow-core is a multiplatform project.
    val jarPath = friendModule.configurations["jvmRuntimeElements"].artifacts.first().file.path
    freeCompilerArgs += "-Xfriend-paths=$jarPath"
  }
}

dependencies {
  compileOnly(libs.jetbrains.annotations)

  api(project(":workflow-core"))
  api(project(":workflow-runtime"))
  api(libs.kotlin.jdk7)

  implementation(project(":internal-testing-utils"))
  implementation(project(":workflow-config:config-jvm"))
  implementation(libs.kotlinx.coroutines.test)
  implementation(libs.kotlin.reflect)

  testImplementation(libs.kotlin.test.jdk)
  testImplementation(libs.mockito.kotlin)
  testImplementation(libs.mockk)
}
