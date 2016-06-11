package com.chibatching.kotpref

import android.content.Context
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricGradleTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricGradleTestRunner::class)
@Config(constants = BuildConfig::class, manifest = Config.NONE, sdk = intArrayOf(23))
class KotprefCustomTest {

    companion object {
        val PREFERENCE_NAME = "custom_example"
    }

    class CustomExample : KotprefModel() {
        override val kotprefName: String = PREFERENCE_NAME
        var testIntVar: Int by intPrefVar(Int.MAX_VALUE)
        var testLongVar: Long by longPrefVar(Long.MAX_VALUE)
        var testFloatVar: Float by floatPrefVar(Float.MAX_VALUE)
        var testBooleanVar: Boolean by booleanPrefVar(true)
        var testStringVar: String by stringPrefVar(default = "default", key = "test_string_var")
        var testStringNullableVar: String? by stringNullablePrefVar(default = "nullable default")
        var testStringRes: Int by intPrefVar(Int.MIN_VALUE, R.string.test_preference)
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
        assertThat(customExample.testIntVar, equalTo(Int.MAX_VALUE))
    }

    @Test
    fun customLongPrefVarDefaultIsSetValue() {
        assertThat(customExample.testLongVar, equalTo(Long.MAX_VALUE))
    }

    @Test
    fun customFloatPrefVarDefaultIsSetValue() {
        assertThat(customExample.testFloatVar, equalTo(Float.MAX_VALUE))
    }

    @Test
    fun customBooleanPrefVarDefaultIsSetValue() {
        assertThat(customExample.testBooleanVar, equalTo(true))
    }

    @Test
    fun customStringPrefVarDefaultIsSetValue() {
        assertThat(customExample.testStringVar, equalTo("default"))
    }

    @Test
    fun customStringNullablePrefVarDefaultIsSetValue() {
        assertThat(customExample.testStringNullableVar, equalTo("nullable default"))
    }

    @Test
    fun canReadWithCustomPreferenceName() {
        val pref = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

        customExample.testIntVar = 39

        assertThat(pref.getInt("testIntVar", 0), equalTo(customExample.testIntVar))
    }

    @Test
    fun canReadWithCustomPreferenceKey() {
        val pref = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

        customExample.testStringVar = "custom key test"

        assertThat(pref.getString("test_string_var", "default"), equalTo(customExample.testStringVar))
    }

    @Test
    fun customPrefVarKeyByStringResource() {
        val pref = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

        customExample.testStringRes = 725

        assertThat(pref.getInt(context.getString(R.string.test_preference), 0), equalTo(customExample.testStringRes))
    }
}


