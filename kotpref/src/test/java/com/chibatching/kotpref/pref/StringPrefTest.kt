package com.chibatching.kotpref.pref

import com.chibatching.kotpref.KotprefTestRunner
import com.chibatching.kotpref.R
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(KotprefTestRunner::class)
class StringPrefTest : BasePrefTest() {

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
