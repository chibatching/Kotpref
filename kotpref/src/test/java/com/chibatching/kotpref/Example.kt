package com.chibatching.kotpref

import java.util.*


class Example : KotprefModel() {
    var testInt by intPref()
    var testLong by longPref()
    var testFloat by floatPref()
    var testBoolean by booleanPref()
    var testString by stringPref()
    var testStringNullable by nullableStringPref()
    val testStringSet by stringSetPref(TreeSet<String>())
}
