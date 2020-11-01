package com.chibatching.kotpref

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import io.mockk.mockkObject
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class KotprefTest {

    @Before
    fun setUp() {
        mockkObject(StaticContextProvider)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun init() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        assertThat(Kotpref.isInitialized).isFalse()
        Kotpref.init(context)
        verify(exactly = 1) { StaticContextProvider.setContext(context) }
        assertThat(Kotpref.isInitialized).isTrue()
    }
}
