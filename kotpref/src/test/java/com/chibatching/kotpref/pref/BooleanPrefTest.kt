package com.chibatching.kotpref.pref

import com.chibatching.kotpref.R
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner
import java.util.*


@RunWith(ParameterizedRobolectricTestRunner::class)
class BooleanPrefTest(commitAllProperties: Boolean) : BasePrefTest(commitAllProperties) {
    companion object {
        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters(name = "commitAllProperties = {0}")
        fun data(): Collection<Array<out Any>> {
            return Arrays.asList(arrayOf(false), arrayOf(true))
        }
    }


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
