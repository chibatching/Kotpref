package com.chibatching.kotprefsample.livedata

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.junit.Test

class EditTextDataTest {

    @Test
    fun sampleMockTest() {
        val data = mockk<EditTextData>(relaxed = true)
        every {
            data.savedText
        } returns "foo"

        assertThat(data.savedText).isEqualTo("foo")
    }
}
