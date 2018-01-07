package com.chibatching.kotpref

import java.util.*


class CustomExample(private val commitAllProperties: Boolean) : KotprefModel() {
    override val commitAllPropertiesByDefault: Boolean
        get() = commitAllProperties

    companion object {
        val PREFERENCE_NAME = "custom_example"
    }

    override val kotprefName: String = PREFERENCE_NAME
    var testInt by intPref(Int.MAX_VALUE, R.string.test_custom_int)
    var testLong by longPref(Long.MAX_VALUE, R.string.test_custom_long)
    var testFloat by floatPref(Float.MAX_VALUE, R.string.test_custom_float)
    var testBoolean by booleanPref(true, R.string.test_custom_boolean)
    var testString by stringPref("default", R.string.test_custom_string)
    var testStringNullable by nullableStringPref("nullable default", R.string.test_custom_nullable_string)
    val testStringSet by stringSetPref(R.string.test_custom_string_set) {
        val defSet = LinkedHashSet<String>()
        defSet.add("Lazy set item 1")
        defSet.add("Lazy set item 2")
        defSet.add("Lazy set item 3")
        defSet
    }
}
