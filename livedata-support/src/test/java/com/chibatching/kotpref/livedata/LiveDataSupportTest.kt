package com.chibatching.kotpref.livedata

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.Observer
import androidx.test.core.app.ApplicationProvider
import com.chibatching.kotpref.KotprefModel
import com.google.common.truth.Truth.assertThat
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifySequence
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class LiveDataSupportTest {
    class Example(context: Context) : KotprefModel(context) {
        var someProperty by stringPref("default")
        var customKeyProperty by intPref(8, "custom_key")
    }

    private lateinit var example: Example
    private lateinit var pref: SharedPreferences

    private fun <T> observer() = mockk<Observer<T>>(relaxed = true)

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
        val liveData = example.asLiveData(example::someProperty)
        val observer = observer<String>()

        liveData.observeForever(observer)

        verify(exactly = 1) {
            observer.onChanged("default")
        }
    }

    @Test
    fun providesDefaultValueWithCustomKey() {
        val liveData = example.asLiveData(example::customKeyProperty)
        val observer = observer<Int>()

        liveData.observeForever(observer)

        verify(exactly = 1) {
            observer.onChanged(8)
        }
    }

    @Test
    fun firesValueChanges() {
        val liveData = example.asLiveData(example::someProperty)
        val observer = observer<String>()

        liveData.observeForever(observer)

        example.someProperty = "some value 1"
        example.someProperty = "value 2"

        verifySequence {
            observer.onChanged("default")
            observer.onChanged("some value 1")
            observer.onChanged("value 2")
        }
    }

    @Test
    fun firesValueChangesWithCustomKey() {
        val liveData = example.asLiveData(example::customKeyProperty)
        val observer = observer<Int>()

        liveData.observeForever(observer)

        example.customKeyProperty = 1
        example.customKeyProperty = 12

        verifySequence {
            observer.onChanged(8)
            observer.onChanged(1)
            observer.onChanged(12)
        }
    }

    @Test
    fun firesLatestValueOnObserve() {
        val liveData = example.asLiveData(example::someProperty)
        val observerSlot = mutableListOf<String>()
        val observer = observer<String>()

        every {
            observer.onChanged(capture(observerSlot))
        } just Runs

        liveData.observeForever(observer)
        liveData.removeObserver(observer)

        example.someProperty = "some value 1"
        example.someProperty = "value 2"

        liveData.observeForever(observer)

        assertThat(observerSlot)
            .containsExactly("default", "value 2")
    }

    @Test
    fun firesLatestValueOnObserveWithCustomKey() {
        val liveData = example.asLiveData(example::customKeyProperty)
        val observerSlot = mutableListOf<Int>()
        val observer = observer<Int>()

        every {
            observer.onChanged(capture(observerSlot))
        } just Runs

        liveData.observeForever(observer)
        liveData.removeObserver(observer)

        example.customKeyProperty = 1
        example.customKeyProperty = 12

        liveData.observeForever(observer)

        assertThat(observerSlot)
            .containsExactly(8, 12)
    }
}
