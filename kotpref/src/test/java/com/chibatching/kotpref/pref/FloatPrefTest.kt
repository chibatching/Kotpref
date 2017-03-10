package com.chibatching.kotpref.pref

import com.chibatching.kotpref.KotprefTestRunner
import com.chibatching.kotpref.R
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(KotprefTestRunner::class)
class FloatPrefTest : BasePrefTest() {

    @Test
    fun floatPrefDefaultIs0() {
        assertThat(example.testFloat).isEqualTo(0f)
    }

    @Test
    fun setValueToFloatPref() {
        example.testFloat = 78422.214F
        assertThat(example.testFloat).isEqualTo(78422.214F)
        assertThat(example.testFloat).isEqualTo(examplePref.getFloat("testFloat", 0f))
    }

    @Test
    fun customDefaultValue() {
        assertThat(customExample.testFloat).isEqualTo(Float.MAX_VALUE)
    }

    @Test
    fun useCustomPreferenceKey() {
        customExample.testFloat = 309F
        assertThat(customExample.testFloat).isEqualTo(309F)
        assertThat(customExample.testFloat).isEqualTo(customPref.getFloat(context.getString(R.string.test_custom_float), 0F))
    }
}
