package com.chibatching.kotpref

import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.manifest.AndroidManifest
import org.robolectric.res.FileFsFile


class CustomRobolectricTestRunner(testClass: Class<*>) : RobolectricTestRunner(testClass) {

    companion object {
        val BUILD_OUTPUT = "build/intermediates/bundles"
    }

    override fun getAppManifest(config: Config?): AndroidManifest? {

        val res = FileFsFile.from(BUILD_OUTPUT, BuildConfig.FLAVOR, BuildConfig.BUILD_TYPE, "res")
        val assets = FileFsFile.from(BUILD_OUTPUT, BuildConfig.FLAVOR, BuildConfig.BUILD_TYPE, "assets")
        val manifest = FileFsFile.from(BUILD_OUTPUT, BuildConfig.FLAVOR, BuildConfig.BUILD_TYPE, "AndroidManifest.xml")

        return AndroidManifest(manifest, res, assets, BuildConfig.APPLICATION_ID)
    }
}
