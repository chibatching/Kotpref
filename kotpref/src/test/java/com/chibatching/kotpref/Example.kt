package com.chibatching.kotpref

import android.content.Context
import com.chibatching.kotpref.pref.booleanPref
import com.chibatching.kotpref.pref.floatPref
import com.chibatching.kotpref.pref.intPref
import com.chibatching.kotpref.pref.longPref
import com.chibatching.kotpref.pref.nullableStringPref
import com.chibatching.kotpref.pref.stringPref
import com.chibatching.kotpref.pref.stringSetPref
import java.util.TreeSet

internal class Example(private val commitAllProperties: Boolean, context: Context) : KotprefModel(context) {
    override val commitAllPropertiesByDefault: Boolean
        get() = commitAllProperties

    var testInt by intPref()
    var testLong by longPref()
    var testFloat by floatPref()
    var testBoolean by booleanPref()
    var testString by stringPref()
    var testStringNullable by nullableStringPref()
    val testStringSet by stringSetPref(TreeSet())
}
