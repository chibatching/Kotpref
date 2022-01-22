package com.chibatching.kotpref

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.chibatching.kotpref.pref.intPref
import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner
import org.robolectric.RobolectricTestRunner
import java.util.Arrays

@RunWith(Enclosed::class)
internal class KotprefModelTest {

    @RunWith(ParameterizedRobolectricTestRunner::class)
    class RemovePropertyTest(private val commitAllProperties: Boolean) {
        companion object {
            @JvmStatic
            @ParameterizedRobolectricTestRunner.Parameters(name = "commitAllProperties = {0}")
            fun data(): Collection<Array<out Any>> {
                return Arrays.asList(arrayOf(false), arrayOf(true))
            }
        }

        private lateinit var data: RemoveTestData

        class RemoveTestData(
            override val commitAllPropertiesByDefault: Boolean,
            context: Context
        ) : KotprefModel(context) {
            var value by intPref()
            var withDefault by intPref(20)
        }

        @Before
        fun setUp() {
            data = RemoveTestData(commitAllProperties, ApplicationProvider.getApplicationContext())
        }

        @After
        fun tearDown() {
            data.clear()
        }

        @Test
        fun testRemoveProperty() {
            data.value = 150
            data.remove(data::value)

            assertThat(data.value).isEqualTo(0)
        }

        @Test
        fun testRemovePropertyWithDefault() {
            data.withDefault = 40
            data.remove(data::withDefault)

            assertThat(data.withDefault).isEqualTo(20)
        }
    }

    @RunWith(RobolectricTestRunner::class)
    class GetPreferenceKeyTest {
        private lateinit var data: GetKeyTestData

        class GetKeyTestData(context: Context) : KotprefModel(context) {
            var value by intPref()
            var withCustomKey by intPref(key = "custom")
        }

        @Before
        fun setUp() {
            data = GetKeyTestData(ApplicationProvider.getApplicationContext())
        }

        @After
        fun tearDown() {
            data.clear()
        }

        @Test
        fun testGetKey() {
            assertThat(data.getPrefKey(data::value)).isEqualTo("value")
        }

        @Test
        fun testGetKeyWithCustomKey() {
            assertThat(data.getPrefKey(data::withCustomKey)).isEqualTo("custom")
        }
    }
}
