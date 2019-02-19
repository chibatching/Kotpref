package com.chibatching.kotpref.coroutines

import android.content.Context
import android.content.SharedPreferences
import androidx.test.core.app.ApplicationProvider
import com.chibatching.kotpref.KotprefModel
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner


@RunWith(RobolectricTestRunner::class)
class CoroutinesSupportTest {
    class Example(context: Context) : KotprefModel(context) {
        var someProperty by stringPref("default")
        var customKeyProperty by intPref(8, "custom_key")
    }

    lateinit var example: Example
    lateinit var pref: SharedPreferences

    @Before
    fun setUp() {
        example = Example(ApplicationProvider.getApplicationContext())

        pref = example.preferences
        pref.edit().clear().commit()
    }

    @After
    fun tearDown() {
        example.clear()
    }

    @Test
    fun providesDefaultValue() {
        val deferred = example.asCoroutine(example::someProperty)

        runBlocking {
            val result = deferred.await()
            assertThat(result).isEqualTo("default")
        }
    }

    @Test
    fun providesDefaultValueWithCustomKey() {
        val deferred = example.asCoroutine(example::customKeyProperty)

        runBlocking {
            val result = deferred.await()
            assertThat(result).isEqualTo(8)
        }
    }

    @Test
    fun changeValue() {
        runBlocking {
            // get default value
            val defaultDeferred = example.asCoroutine(example::someProperty)
            val defaultValue = defaultDeferred.await()
            assertThat(defaultValue).isEqualTo("default")

            // set new value
            example.someProperty = "new value"

            // get new value
            val newDeferred = example.asCoroutine(example::someProperty)
            val newValue = newDeferred.await()
            assertThat(newValue).isEqualTo("new value")
        }
    }
}