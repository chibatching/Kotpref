package com.chibatching.kotpref

import android.content.Context
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(KotprefTestRunner::class)
@Config(constants = BuildConfig::class, manifest = Config.NONE, sdk = intArrayOf(23))
class KotprefCustomTest {

    companion object {
        val PREFERENCE_NAME = "custom_example"
    }

    class CustomExample : KotprefModel() {
        override val kotprefName: String = PREFERENCE_NAME
        var testInt by intPref(Int.MAX_VALUE)
        var testLong by longPref(Long.MAX_VALUE)
        var testFloat by floatPref(Float.MAX_VALUE)
        var testBoolean by booleanPref(true)
        var testString by stringPref(default = "default", key = "test_string_var")
        var testStringNullable by nullableStringPref(default = "nullable default")
        var testStringRes by intPref(Int.MIN_VALUE, R.string.test_preference)
    }

    lateinit var context: Context
    lateinit var customExample: CustomExample

    @Before
    fun setUp() {
        context = RuntimeEnvironment.application
        Kotpref.init(context)
        customExample = CustomExample()

        context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit().clear().commit()
    }

    @After
    fun tearDown() {
        customExample.clear()
    }

    @Test
    fun customIntPrefVarDefaultIsSetValue() {
        assertThat(customExample.testInt, equalTo(Int.MAX_VALUE))
    }

    @Test
    fun customLongPrefVarDefaultIsSetValue() {
        assertThat(customExample.testLong, equalTo(Long.MAX_VALUE))
    }

    @Test
    fun customFloatPrefVarDefaultIsSetValue() {
        assertThat(customExample.testFloat, equalTo(Float.MAX_VALUE))
    }

    @Test
    fun customBooleanPrefVarDefaultIsSetValue() {
        assertThat(customExample.testBoolean, equalTo(true))
    }

    @Test
    fun customStringPrefVarDefaultIsSetValue() {
        assertThat(customExample.testString, equalTo("default"))
    }

    @Test
    fun customStringNullablePrefVarDefaultIsSetValue() {
        assertThat(customExample.testStringNullable, equalTo("nullable default"))
    }

    @Test
    fun canReadWithCustomPreferenceName() {
        val pref = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

        customExample.testInt = 39

        assertThat(pref.getInt("testInt", 0), equalTo(customExample.testInt))
    }

    @Test
    fun canReadWithCustomPreferenceKey() {
        val pref = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

        customExample.testString = "custom key test"

        assertThat(pref.getString("test_string_var", "default"), equalTo(customExample.testString))
    }

    @Test
    fun customPrefVarKeyByStringResource() {
        val pref = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

        customExample.testStringRes = 725

        assertThat(pref.getInt(context.getString(R.string.test_preference), 0), equalTo(customExample.testStringRes))
    }
}


