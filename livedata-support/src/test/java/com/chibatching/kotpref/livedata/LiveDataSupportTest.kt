package com.chibatching.kotpref.livedata

import android.arch.lifecycle.*
import android.content.Context
import android.content.SharedPreferences
import com.chibatching.kotpref.Kotpref
import com.chibatching.kotpref.KotprefModel
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


@RunWith(RobolectricTestRunner::class)
class LiveDataSupportTest {
    class Example : KotprefModel() {
        var someProperty by stringPref("default")
    }

    lateinit var example: Example
    lateinit var liveData: LiveData<String>
    lateinit var context: Context
    lateinit var pref: SharedPreferences

    @Before
    fun setUp() {
        context = RuntimeEnvironment.application
        Kotpref.init(context)
        example = Example()

        pref = example.preferences
        pref.edit().clear().commit()

        liveData = example.asLiveData(example::someProperty)
    }

    @After
    fun tearDown() {
        example.clear()
    }

    @Test
    fun providesDefaultValue() {
        val latch = CountDownLatch(1)

        liveData.observeForever {
            assertThat(it).isEqualTo("default")
            latch.countDown()
        }

        latch.await(1, TimeUnit.SECONDS)
    }

    @Test
    fun firesValueChanges() {
        val latch = CountDownLatch(3)

        val values = listOf("default", "some value 1", "value 2")
        var i = 0
        liveData.observeForever {
            assertThat(it).isEqualTo(values[i++])
            latch.countDown()
        }
        example.someProperty = values[1]
        example.someProperty = values[2]

        latch.await(1, TimeUnit.SECONDS)
    }

    @Test
    fun firesLatestValueOnObserve() {
        val latch = CountDownLatch(3)

        val values = listOf("some value 1", "value 2")
        val expectedResults = listOf("default", "default", values[0], values[0], values[1])
        var i = 0

        val observer = Observer<String> {
            assertThat(it).isEqualTo(expectedResults[i++])
            latch.countDown()
        }
        liveData.observeForever(observer)
        liveData.removeObserver(observer)

        (0..1).forEach {
            liveData.observeForever(observer)
            example.someProperty = values[it]
            liveData.removeObserver(observer)
        }

        latch.await(1, TimeUnit.SECONDS)
    }
}
