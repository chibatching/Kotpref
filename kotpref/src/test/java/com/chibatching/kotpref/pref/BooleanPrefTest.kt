package com.chibatching.kotpref.pref

import com.chibatching.kotpref.KotprefTestRunner
import com.chibatching.kotpref.R
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(KotprefTestRunner::class)
class BooleanPrefTest : BasePrefTest() {


    @Test
    fun booleanPrefDefaultIsFalse() {
        assertThat(example.testBoolean).isEqualTo(false)
    }

    @Test
    fun setValueToPreference() {
        example.testBoolean = false
        assertThat(example.testBoolean).isEqualTo(false)
        assertThat(example.testBoolean).isEqualTo(examplePref.getBoolean("testBoolean", false))
    }

    @Test
    fun customDefaultValue() {
        assertThat(customExample.testBoolean).isEqualTo(true)
    }

    @Test
    fun useCustomPreferenceKey() {
        customExample.testBoolean = false
        assertThat(customExample.testBoolean).isEqualTo(false)
        assertThat(customExample.testBoolean).isEqualTo(customPref.getBoolean(context.getString(R.string.test_custom_boolean), false))
    }
}
