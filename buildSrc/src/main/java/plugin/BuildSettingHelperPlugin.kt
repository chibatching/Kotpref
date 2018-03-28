package plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByName
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.tasks.JacocoReport

class BuildSettingHelperPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.extensions.add("isCi", System.getenv("CI")?.toBoolean() ?: false)
        addJacocoSettings(project)
    }

    private fun addJacocoSettings(project: Project) {
        project.extensions.getByName<JacocoPluginExtension>("jacoco").apply {
            toolVersion = "0.8.0"
        }
        project.tasks.create("jacocoTestReport", JacocoReport::class.java) {
            dependsOn("testDebugUnitTest")
            reports {
                xml.isEnabled = true
                html.isEnabled = true
            }
            val fileFilter = listOf(
                "**/R.class", "**/R$*.class", "**/BuildConfig.*", "**/Manifest*.*"
            )
            val debugTree = project.fileTree(
                mapOf(
                    "dir" to "${project.buildDir}/intermediates/classes/debug",
                    "excludes" to fileFilter
                )
            )
            val kotlinDebugTree = project.fileTree(
                mapOf(
                    "dir" to "${project.buildDir}/tmp/kotlin-classes/debug",
                    "excludes" to fileFilter
                )
            )
            val mainSrc = "${project.projectDir}/src/main/kotlin"
            sourceDirectories = project.files(listOf(mainSrc))
            classDirectories = project.files(listOf(debugTree, kotlinDebugTree))
            executionData = project.fileTree(
                mapOf(
                    "dir" to project.buildDir.toString(),
                    "includes" to listOf("jacoco/testDebugUnitTest.exec")
                )
            )
        }
    }
}
