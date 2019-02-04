package com.chibatching.kotprefsample.livedata

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class EditTextDataTest {

    @Test
    fun sampleMockTest() {
        val data = mock<EditTextData>()
        whenever(data.savedText).thenReturn("foo")

        assertThat(data.savedText).isEqualTo("foo")
    }
}
