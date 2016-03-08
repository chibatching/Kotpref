package com.chibatching.kotpref

import android.content.Context
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import android.test.AndroidTestCase
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
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
    fun customIntPrefVarDefaultIsSetValue() {
        assertThat(CustomExample.testIntVar, equalTo(Int.MAX_VALUE))
    }

    @Test
    fun customLongPrefVarDefaultIsSetValue() {
        assertThat(CustomExample.testLongVar, equalTo(Long.MAX_VALUE))
    }

    @Test
    fun customFloatPrefVarDefaultIsSetValue() {
        assertThat(CustomExample.testFloatVar, equalTo(Float.MAX_VALUE))
    }

    @Test
    fun customBooleanPrefVarDefaultIsSetValue() {
        assertThat(CustomExample.testBooleanVar, equalTo(true))
    }

    @Test
    fun customStringPrefVarDefaultIsSetValue() {
        assertThat(CustomExample.testStringVar, equalTo("default"))
    }

    @Test
    fun customStringNullablePrefVarDefaultIsSetValue() {
        assertThat(CustomExample.testStringNullableVar, equalTo("nullable default"))
    }

    @Test
    fun canReadWithCustomPreferenceName() {
        val pref = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

        CustomExample.testIntVar = 39

        assertThat(pref.getInt("testIntVar", 0), equalTo(CustomExample.testIntVar))
    }

    @Test
    fun canReadWithCustomPreferenceKey() {
        val pref = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

        CustomExample.testStringVar = "custom key test"

        assertThat(pref.getString("test_string_var", "default"), equalTo(CustomExample.testStringVar))
    }
}


