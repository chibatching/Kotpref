package com.chibatching.kotpref.livedata

import android.app.Application
import android.content.SharedPreferences
import android.os.Looper.getMainLooper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import io.mockk.verifySequence
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.LooperMode

@RunWith(RobolectricTestRunner::class)
internal class LiveDataSupportTest {

    data class GsonSample(
        val text: String,
        val number: Int
    )

    enum class EnumSample {
        FIRST
    }

    private lateinit var example: Example
    private lateinit var pref: SharedPreferences

    private fun <T> observer() = mockk<Observer<T>>(relaxed = true)

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Application>()
        example =
            Example(context)

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
    fun providesDefaultValueWithGson() {
        val liveData = example.asLiveData(example::gsonPref)
        val observer = observer<GsonSample>()

        liveData.observeForever(observer)

        verify(exactly = 1) {
            observer.onChanged(Example.gsonSampleDefault)
        }
    }

    @Test
    fun providesDefaultValueWithEnum() {
        val liveData = example.asLiveData(example::enumPref)
        val observer = observer<EnumSample>()

        liveData.observeForever(observer)

        verify(exactly = 1) {
            observer.onChanged(EnumSample.FIRST)
        }
    }

    @Test
    fun providesDefaultValueWithStringSet() {
        val liveData = example.asLiveData(example::setPref)
        val observer = observer<Set<String>>()

        val slot = slot<Set<String>>()
        every {
            observer.onChanged(capture(slot))
        } just Runs

        liveData.observeForever(observer)

        verify(exactly = 1) {
            observer.onChanged(any())
        }
        assertThat(slot.captured).containsExactlyElementsIn(Example.defaultSet)
    }

    @Test
    fun firesValueChanges() {
        val liveData = example.asLiveData(example::someProperty)
        val observer = observer<String>()

        liveData.observeForever(observer)

        example.someProperty = "some value 1"
        shadowOf(getMainLooper()).idle()

        example.someProperty = "value 2"
        shadowOf(getMainLooper()).idle()

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
        shadowOf(getMainLooper()).idle()

        example.customKeyProperty = 12
        shadowOf(getMainLooper()).idle()

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

    @Test
    fun firesLastValueWhileInactive() {
        val liveData = example.asLiveData(example::someProperty)
        val observerSlot = mutableListOf<String>()
        val observer = observer<String>()
        val lifecycle = LifecycleRegistry(mockk(relaxed = true))

        every {
            observer.onChanged(capture(observerSlot))
        } just Runs

        liveData.observe(LifecycleOwner { lifecycle }, observer)
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_START)
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_STOP)

        example.someProperty = "some value 1"
        example.someProperty = "value 2"

        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_START)

        assertThat(observerSlot)
            .containsExactly("default", "value 2")
    }

    @Test
    fun firesLastValueWhileInactiveWithCustomKey() {
        val liveData = example.asLiveData(example::customKeyProperty)
        val observerSlot = mutableListOf<Int>()
        val observer = observer<Int>()
        val lifecycle = LifecycleRegistry(mockk(relaxed = true))

        every {
            observer.onChanged(capture(observerSlot))
        } just Runs

        liveData.observe(LifecycleOwner { lifecycle }, observer)
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_START)
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_STOP)

        example.customKeyProperty = 1
        example.customKeyProperty = 12

        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_START)

        assertThat(observerSlot)
            .containsExactly(8, 12)
    }
}
