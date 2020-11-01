package com.chibatching.kotpref.pref

import android.content.Context
import com.chibatching.kotpref.R
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner
import java.util.Arrays

@RunWith(ParameterizedRobolectricTestRunner::class)
internal class StringPrefTest(commitAllProperties: Boolean) : BasePrefTest(commitAllProperties) {
    companion object {
        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters(name = "commitAllProperties = {0}")
        fun data(): Collection<Array<out Any>> {
            return Arrays.asList(arrayOf(false), arrayOf(true))
        }
    }

    @Test
    fun stringPrefDefaultIsEmptyString() {
        assertThat(example.testString).isEqualTo("")
    }

    @Test
    fun setValueToStringPref() {
        example.testString = "Ohayo!"
        assertThat(example.testString)
            .isEqualTo("Ohayo!")
        assertThat(example.testString)
            .isEqualTo(examplePref.getString("testString", ""))
    }

    @Test
    fun customDefaultValue() {
        assertThat(customExample.testString).isEqualTo("default")
    }

    @Test
    fun useCustomPreferenceKey() {
        customExample.testString = "Hello"
        assertThat(customExample.testString)
            .isEqualTo("Hello")
        assertThat(customExample.testString)
            .isEqualTo(customPref.getString(context.getString(R.string.test_custom_string), null))
    }

    @Test
    fun readFromOtherPreferenceInstance() {
        val otherPreference =
            context.getSharedPreferences(example.kotprefName, Context.MODE_PRIVATE)

        example.testStringNullable = "Hello world"

        assertThat(otherPreference.getString("testStringNullable", null))
            .isEqualTo("Hello world")
    }

    @Test
    fun writeFromOtherPreferenceInstance() {
        val otherPreference =
            context.getSharedPreferences(example.kotprefName, Context.MODE_PRIVATE)

        example.testString

        otherPreference.edit()
            .putString("testStringNullable", "Hello world")
            .commit()

        assertThat(example.testStringNullable).isEqualTo("Hello world")
    }
}
