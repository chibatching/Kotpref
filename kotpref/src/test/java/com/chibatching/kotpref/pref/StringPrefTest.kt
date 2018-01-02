package com.chibatching.kotpref.pref

import com.chibatching.kotpref.R
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner
import java.util.*


@RunWith(ParameterizedRobolectricTestRunner::class)
class StringPrefTest(commitAllProperties: Boolean) : BasePrefTest(commitAllProperties) {
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
        assertThat(example.testString).isEqualTo("Ohayo!")
        assertThat(example.testString).isEqualTo(examplePref.getString("testString", ""))
    }

    @Test
    fun customDefaultValue() {
        assertThat(customExample.testString).isEqualTo("default")
    }

    @Test
    fun useCustomPreferenceKey() {
        customExample.testString = "Hello"
        assertThat(customExample.testString).isEqualTo("Hello")
        assertThat(customExample.testString).isEqualTo(customPref.getString(context.getString(R.string.test_custom_string), null))
    }
}
