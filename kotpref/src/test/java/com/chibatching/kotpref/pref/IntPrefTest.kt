package com.chibatching.kotpref.pref

import android.content.Context
import com.chibatching.kotpref.R
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner
import java.util.Arrays

@RunWith(ParameterizedRobolectricTestRunner::class)
internal class IntPrefTest(commitAllProperties: Boolean) : BasePrefTest(commitAllProperties) {
    companion object {
        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters(name = "commitAllProperties = {0}")
        fun data(): Collection<Array<out Any>> {
            return Arrays.asList(arrayOf(false), arrayOf(true))
        }
    }

    @Test
    fun intPrefDefaultIs0() {
        assertThat(example.testInt).isEqualTo(0)
    }

    @Test
    fun setValueToIntPref() {
        example.testInt = 4320
        assertThat(example.testInt)
            .isEqualTo(4320)
        assertThat(example.testInt)
            .isEqualTo(examplePref.getInt("testInt", 0))
    }

    @Test
    fun customDefaultValue() {
        assertThat(customExample.testInt).isEqualTo(Int.MAX_VALUE)
    }

    @Test
    fun useCustomPreferenceKey() {
        customExample.testInt = 29
        assertThat(customExample.testInt)
            .isEqualTo(29)
        assertThat(customExample.testInt)
            .isEqualTo(customPref.getInt(context.getString(R.string.test_custom_int), 0))
    }

    @Test
    fun readFromOtherPreferenceInstance() {
        val otherPreference =
            context.getSharedPreferences(example.kotprefName, Context.MODE_PRIVATE)

        example.testInt = 20

        assertThat(otherPreference.getInt("testInt", 0))
            .isEqualTo(20)
    }

    @Test
    fun writeFromOtherPreferenceInstance() {
        val otherPreference =
            context.getSharedPreferences(example.kotprefName, Context.MODE_PRIVATE)

        example.testInt

        otherPreference.edit()
            .putInt("testInt", 50)
            .commit()

        assertThat(example.testInt).isEqualTo(50)
    }
}
