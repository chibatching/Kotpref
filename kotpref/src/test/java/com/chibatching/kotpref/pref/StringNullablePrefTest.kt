package com.chibatching.kotpref.pref

import android.content.Context
import com.chibatching.kotpref.R
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner
import java.util.Arrays

@RunWith(ParameterizedRobolectricTestRunner::class)
internal class StringNullablePrefTest(commitAllProperties: Boolean) : BasePrefTest(commitAllProperties) {
    companion object {
        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters(name = "commitAllProperties = {0}")
        fun data(): Collection<Array<out Any>> {
            return Arrays.asList(arrayOf(false), arrayOf(true))
        }
    }

    @Test
    fun stringPrefDefaultIsNull() {
        assertThat(example.testStringNullable).isNull()
    }

    @Test
    fun setValueToStringNullablePref() {
        example.testStringNullable = "Ohayo!"
        assertThat(example.testStringNullable)
            .isEqualTo("Ohayo!")
        assertThat(example.testStringNullable)
            .isEqualTo(examplePref.getString("testStringNullable", null))
    }

    @Test
    fun customDefaultValue() {
        assertThat(customExample.testStringNullable).isEqualTo("nullable default")
    }

    @Test
    fun useCustomPreferenceKey() {
        customExample.testStringNullable = "Hello"
        assertThat(customExample.testStringNullable)
            .isEqualTo("Hello")
        assertThat(customExample.testStringNullable)
            .isEqualTo(
                customPref.getString(
                    context.getString(R.string.test_custom_nullable_string),
                    null
                )
            )
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

        example.testStringNullable

        otherPreference.edit()
            .putString("testStringNullable", "Hello world")
            .commit()

        assertThat(example.testStringNullable).isEqualTo("Hello world")
    }
}
