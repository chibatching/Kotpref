package com.chibatching.kotpref

import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config


class KotprefTestRunner(clazz: Class<*>) : RobolectricTestRunner(clazz) {

    override fun buildGlobalConfig(): Config {
        val buildType = BuildConfig.BUILD_TYPE
        return Config.Builder.defaults()
                .setSdk(25)
                .setManifest("build/intermediates/manifests/aapt/$buildType/AndroidManifest.xml")
                .setConstants(BuildConfig::class.java)
                .setPackageName("com.chibatching.kotpref")
                .setResourceDir("../../../../../src/$buildType/res")
                .build()
    }
}