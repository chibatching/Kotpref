package com.chibatching.kotpref.pref

import com.chibatching.kotpref.R
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner


@RunWith(RobolectricTestRunner::class)
class IntPrefTest : BasePrefTest() {

    @Test
    fun intPrefDefaultIs0() {
        assertThat(example.testInt).isEqualTo(0)
    }

    @Test
    fun setValueToIntPref() {
        example.testInt = 4320
        assertThat(example.testInt).isEqualTo(4320)
        assertThat(example.testInt).isEqualTo(examplePref.getInt("testInt", 0))
    }

    @Test
    fun customDefaultValue() {
        assertThat(customExample.testInt).isEqualTo(Int.MAX_VALUE)
    }

    @Test
    fun useCustomPreferenceKey() {
        customExample.testInt = 29
        assertThat(customExample.testInt).isEqualTo(29)
        assertThat(customExample.testInt).isEqualTo(customPref.getInt(context.getString(R.string.test_custom_int), 0))
    }
}
