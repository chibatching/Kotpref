package com.chibatching.kotpref

import java.util.*


class Example(private val commitAllProperties: Boolean) : KotprefModel() {
    override val commitAllPropertiesByDefault: Boolean
        get() = commitAllProperties

    var testInt by intPref()
    var testLong by longPref()
    var testFloat by floatPref()
    var testBoolean by booleanPref()
    var testString by stringPref()
    var testStringNullable by nullableStringPref()
    val testStringSet by stringSetPref(TreeSet<String>())
}
