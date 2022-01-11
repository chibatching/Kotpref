package plugin

import dependencies.Versions
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByName
import org.gradle.kotlin.dsl.invoke
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.tasks.JacocoReport

class BuildSettingHelperPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        addJacocoSettings(project)
    }

    private fun addJacocoSettings(project: Project): Unit = project.run {
        extensions.getByName<JacocoPluginExtension>("jacoco").apply {
            toolVersion = Versions.jacoco
        }
        tasks {
            register("jacocoTestReport", JacocoReport::class.java) {
                dependsOn("testDebugUnitTest")
                reports {
                    xml.required.set(true)
                    html.required.set(true)
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
                sourceDirectories.from(files(listOf(mainSrc)))
                classDirectories.from(files(listOf(debugTree, kotlinDebugTree)))
                executionData.from(
                    fileTree(
                        mapOf(
                            "dir" to project.buildDir.toString(),
                            "includes" to listOf("jacoco/testDebugUnitTest.exec")
                        )
                    )
                )
            }
        }
    }
}
