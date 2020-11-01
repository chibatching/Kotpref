package com.chibatching.kotpref.pref

import android.content.Context
import com.chibatching.kotpref.R
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner
import java.util.Arrays

@RunWith(ParameterizedRobolectricTestRunner::class)
internal class LongPrefTest(commitAllProperties: Boolean) : BasePrefTest(commitAllProperties) {
    companion object {
        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters(name = "commitAllProperties = {0}")
        fun data(): Collection<Array<out Any>> {
            return Arrays.asList(arrayOf(false), arrayOf(true))
        }
    }

    @Test
    fun longPrefDefaultIs0() {
        assertThat(example.testLong).isEqualTo(0L)
    }

    @Test
    fun setValueToLongPref() {
        example.testLong = 32942L
        assertThat(example.testLong)
            .isEqualTo(32942L)
        assertThat(example.testLong)
            .isEqualTo(examplePref.getLong("testLong", 0L))
    }

    @Test
    fun customDefaultValue() {
        assertThat(customExample.testLong).isEqualTo(Long.MAX_VALUE)
    }

    @Test
    fun useCustomPreferenceKey() {
        customExample.testLong = 296201L
        assertThat(customExample.testLong)
            .isEqualTo(296201L)
        assertThat(customExample.testLong)
            .isEqualTo(customPref.getLong(context.getString(R.string.test_custom_long), 0L))
    }

    @Test
    fun readFromOtherPreferenceInstance() {
        val otherPreference =
            context.getSharedPreferences(example.kotprefName, Context.MODE_PRIVATE)

        example.testLong = 40L

        assertThat(otherPreference.getLong("testFloat", 40L))
            .isEqualTo(40L)
    }

    @Test
    fun writeFromOtherPreferenceInstance() {
        val otherPreference =
            context.getSharedPreferences(example.kotprefName, Context.MODE_PRIVATE)

        example.testLong

        otherPreference.edit()
            .putLong("testLong", 10L)
            .commit()

        assertThat(example.testLong).isEqualTo(10L)
    }
}
