package com.chibatching.kotpref.livedata

import android.content.Context
import com.chibatching.kotpref.KotprefModel
import com.chibatching.kotpref.enumpref.enumValuePref
import com.chibatching.kotpref.gsonpref.gsonPref
import com.chibatching.kotpref.pref.intPref
import com.chibatching.kotpref.pref.stringPref
import com.chibatching.kotpref.pref.stringSetPref

internal class Example(context: Context) : KotprefModel(context) {
    companion object {
        val gsonSampleDefault = LiveDataSupportTest.GsonSample("text", 1)
        val defaultSet = setOf("test1", "test2")
    }

    var someProperty by stringPref("default")
    var customKeyProperty by intPref(8, "custom_key")

    var gsonPref by gsonPref(gsonSampleDefault)
    var enumPref by enumValuePref(LiveDataSupportTest.EnumSample.FIRST)
    val setPref by stringSetPref(defaultSet)
}
