package com.chibatching.kotpref.pref

import com.chibatching.kotpref.KotprefTestRunner
import com.chibatching.kotpref.R
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(KotprefTestRunner::class)
class StringNullablePrefTest : BasePrefTest() {

    @Test
    fun stringPrefDefaultIsNull() {
        assertThat(example.testStringNullable).isNull()
    }

    @Test
    fun setValueToStringNullablePref() {
        example.testStringNullable = "Ohayo!"
        assertThat(example.testStringNullable).isEqualTo("Ohayo!")
        assertThat(example.testStringNullable).isEqualTo(examplePref.getString("testStringNullable", null))
    }

    @Test
    fun customDefaultValue() {
        assertThat(customExample.testStringNullable).isEqualTo("nullable default")
    }

    @Test
    fun useCustomPreferenceKey() {
        customExample.testStringNullable = "Hello"
        assertThat(customExample.testStringNullable).isEqualTo("Hello")
        assertThat(customExample.testStringNullable).isEqualTo(customPref.getString(context.getString(R.string.test_custom_nullable_string), null))
    }
}
