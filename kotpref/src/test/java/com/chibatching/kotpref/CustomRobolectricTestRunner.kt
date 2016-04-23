package com.chibatching.kotpref

import org.robolectric.RobolectricGradleTestRunner
import org.robolectric.annotation.Config
import org.robolectric.manifest.AndroidManifest
import org.robolectric.res.FileFsFile


class CustomRobolectricTestRunner(testClass: Class<*>) : RobolectricGradleTestRunner(testClass) {

    companion object {
        val BUILD_OUTPUT = "build/intermediates"
        val TEST_FLAVOR = "androidTest"
    }

    override fun getAppManifest(config: Config?): AndroidManifest? {

        val res = FileFsFile.from(BUILD_OUTPUT, "res/merged", TEST_FLAVOR, BuildConfig.BUILD_TYPE)
        val assets = FileFsFile.from(BUILD_OUTPUT, "assets", TEST_FLAVOR, BuildConfig.BUILD_TYPE)
        val manifest = FileFsFile.from(BUILD_OUTPUT, "manifest", TEST_FLAVOR, BuildConfig.BUILD_TYPE, "AndroidManifest.xml")

        return AndroidManifest(manifest, res, assets, BuildConfig.APPLICATION_ID)
    }
}
