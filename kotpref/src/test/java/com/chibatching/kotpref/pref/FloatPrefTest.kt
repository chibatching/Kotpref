package com.chibatching.kotpref.pref

import android.content.Context
import com.chibatching.kotpref.R
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner
import java.util.*

@RunWith(ParameterizedRobolectricTestRunner::class)
class FloatPrefTest(commitAllProperties: Boolean) : BasePrefTest(commitAllProperties) {
    companion object {
        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters(name = "commitAllProperties = {0}")
        fun data(): Collection<Array<out Any>> {
            return Arrays.asList(arrayOf(false), arrayOf(true))
        }
    }

    @Test
    fun floatPrefDefaultIs0() {
        assertThat(example.testFloat).isEqualTo(0f)
    }

    @Test
    fun setValueToFloatPref() {
        example.testFloat = 78422.214F
        assertThat(example.testFloat)
            .isEqualTo(78422.214F)
            .isEqualTo(examplePref.getFloat("testFloat", 0f))
    }

    @Test
    fun customDefaultValue() {
        assertThat(customExample.testFloat).isEqualTo(Float.MAX_VALUE)
    }

    @Test
    fun useCustomPreferenceKey() {
        customExample.testFloat = 309F
        assertThat(customExample.testFloat)
            .isEqualTo(309F)
            .isEqualTo(customPref.getFloat(context.getString(R.string.test_custom_float), 0F))
    }

    @Test
    fun readFromOtherPreferenceInstance() {
        val otherPreference =
            context.getSharedPreferences(example.kotprefName, Context.MODE_PRIVATE)

        example.testFloat = 100F

        assertThat(otherPreference.getFloat("testFloat", 0F))
            .isEqualTo(100F)
    }

    @Test
    fun writeFromOtherPreferenceInstance() {
        val otherPreference =
            context.getSharedPreferences(example.kotprefName, Context.MODE_PRIVATE)

        example.testFloat

        otherPreference.edit()
            .putFloat("testFloat", 120F)
            .commit()

        assertThat(example.testFloat).isEqualTo(120F)
    }
}
