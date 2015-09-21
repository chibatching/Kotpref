package com.chibatching.kotpref

import android.test.AndroidTestCase
import kotlin.test.assertEquals

public class KotprefCustomTest : AndroidTestCase() {

    companion object {
        val PREFERENCE_NAME = "custom_example"
    }

    object CustomExample : KotprefModel() {
        override val kotprefName: String = PREFERENCE_NAME
        var testIntVar: Int by intPrefVar(Int.MAX_VALUE)
        var testStringVar: String by stringPrefVar(default = "default", key = "test_string_var")
    }

    override fun setUp() {
        Kotpref.init(context)
        context.getSharedPreferences(CustomExample.kotprefName, CustomExample.kotprefMode).edit().clear().commit()
    }

    public fun testPreferenceName() {
        CustomExample.clear()

        val pref = context.getSharedPreferences(PREFERENCE_NAME, CustomExample.kotprefMode)
        CustomExample.testIntVar = 39
        assertEquals(pref.getInt("testIntVar", 0), CustomExample.testIntVar)
    }

    public fun testCustomKey() {
        CustomExample.clear()

        val pref = context.getSharedPreferences(PREFERENCE_NAME, CustomExample.kotprefMode)
        CustomExample.testStringVar = "custom key name"
        assertEquals(pref.getString("test_string_var", "default"), CustomExample.testStringVar)
        assertEquals(pref.getString("test_string_var", "default"), "custom key name")
    }
}


