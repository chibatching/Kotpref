package com.chibatching.kotpref

import android.content.Context
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import android.test.AndroidTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class KotprefCustomTest : AndroidTestCase() {

    companion object {
        val PREFERENCE_NAME = "custom_example"
    }

    object CustomExample : KotprefModel() {
        override val kotprefName: String = PREFERENCE_NAME
        var testIntVar: Int by intPrefVar(Int.MAX_VALUE)
        var testLongVar: Long by longPrefVar(Long.MAX_VALUE)
        var testFloatVar: Float by floatPrefVar(Float.MAX_VALUE)
        var testBooleanVar: Boolean by booleanPrefVar(true)
        var testStringVar: String by stringPrefVar(default = "default", key = "test_string_var")
        var testStringNullableVar: String? by stringNullablePrefVar(default = "nullable default")
    }

    @Before
    override public fun setUp() {
        context = InstrumentationRegistry.getTargetContext()
        Kotpref.init(context)
        context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit().clear().commit()
    }

    @After
    override public fun tearDown() {
        super.tearDown()
    }

    @Test
    fun testIntDefault() {
        CustomExample.clear()
        assertEquals(Int.MAX_VALUE, CustomExample.testIntVar)
    }

    @Test
    fun testLongDefault() {
        CustomExample.clear()
        assertEquals(Long.MAX_VALUE, CustomExample.testLongVar)
    }

    @Test
    fun testFloatDefault() {
        CustomExample.clear()
        assertEquals(Float.MAX_VALUE, CustomExample.testFloatVar)
    }

    @Test
    fun testBooleanDefault() {
        CustomExample.clear()
        assertEquals(true, CustomExample.testBooleanVar)
    }

    @Test
    fun testStringNullableDefault() {
        CustomExample.clear()
        assertEquals("nullable default", CustomExample.testStringNullableVar)
    }

    @Test
    fun testPreferenceName() {
        CustomExample.clear()

        val pref = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        CustomExample.testIntVar = 39
        assertEquals(pref.getInt("testIntVar", 0), CustomExample.testIntVar)
    }

    @Test
    fun testCustomKey() {
        CustomExample.clear()

        val pref = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        CustomExample.testStringVar = "custom key name"
        assertEquals(pref.getString("test_string_var", "default"), CustomExample.testStringVar)
        assertEquals(pref.getString("test_string_var", "default"), "custom key name")
    }
}


