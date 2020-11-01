package com.chibatching.kotpref.gsonpref

import android.content.Context
import android.content.SharedPreferences
import androidx.test.core.app.ApplicationProvider
import com.chibatching.kotpref.Kotpref
import com.chibatching.kotpref.KotprefModel
import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner
import java.util.Arrays
import java.util.Calendar
import java.util.Date

@RunWith(ParameterizedRobolectricTestRunner::class)
internal class GsonSupportTest(private val commitAllProperties: Boolean) {

    companion object {
        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters(name = "commitAllProperties = {0}")
        fun data(): Collection<Array<out Any>> {
            return Arrays.asList(arrayOf(false), arrayOf(true))
        }

        fun createDefaultContent(): Content =
            Content("default title", "contents write here", createDate(2017, 1, 5))

        fun createDate(year: Int, month: Int, day: Int): Date =
            Calendar.getInstance().apply {
                set(year, month, day)
                set(Calendar.HOUR, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.time
    }

    class Example(private val commitAllProperties: Boolean) :
        KotprefModel(ApplicationProvider.getApplicationContext<Context>()) {
        override val commitAllPropertiesByDefault: Boolean
            get() = commitAllProperties

        var content by gsonPref(createDefaultContent())

        var list: List<String> by gsonPref(emptyList<String>())

        var nullableContent: Content? by gsonNullablePref<Content>()
    }

    private lateinit var example: Example
    private lateinit var pref: SharedPreferences

    @Before
    fun setUp() {
        Kotpref.gson = Gson()
        example = Example(commitAllProperties)

        pref = example.preferences
        pref.edit().clear().commit()
    }

    @After
    fun tearDown() {
        example.clear()
    }

    @Test
    fun gsonPrefDefaultIsDefined() {
        assertThat(example.content).isEqualTo(createDefaultContent())
    }

    @Test
    fun setGsonPrefSetCausePreferenceUpdate() {
        example.content = Content("new title", "this is new content", createDate(2017, 1, 25))
        assertThat(example.content)
            .isEqualTo(
                Content(
                    "new title",
                    "this is new content",
                    createDate(2017, 1, 25)
                )
            )
        assertThat(example.content)
            .isEqualTo(
                Kotpref.gson?.fromJson(
                    pref.getString("content", ""),
                    Content::class.java
                )
            )
    }

    @Test
    fun gsonNullablePrefDefaultIsNull() {
        assertThat(example.nullableContent).isNull()
    }

    @Test
    fun gsonNullablePrefCausePreferenceUpdate() {
        example.nullableContent =
            Content("nullable content", "this is not null", createDate(2017, 1, 20))
        assertThat(example.nullableContent)
            .isEqualTo(
                Content(
                    "nullable content",
                    "this is not null",
                    createDate(2017, 1, 20)
                )
            )
        assertThat(example.nullableContent)
            .isEqualTo(
                Kotpref.gson?.fromJson(
                    pref.getString(
                        "nullableContent",
                        ""
                    ), Content::class.java
                )
            )
    }

    @Test
    fun gsonNullablePrefSetNull() {

        fun setNull() {
            example.nullableContent = null
        }
        setNull()
        assertThat(example.nullableContent)
            .isEqualTo(
                Kotpref.gson?.fromJson(
                    pref.getString(
                        "nullableContent",
                        ""
                    ), Content::class.java
                )
            )
        assertThat(example.nullableContent).isNull()
    }

    @Test
    fun gsonGenericTypeTest() {
        example.list = listOf("gson", "generic", "type")
        val result = Kotpref.gson?.fromJson<List<String>>(
            pref.getString("list", ""), object : TypeToken<List<String>>() {}.type
        )
        assertThat(example.list)
            .containsExactlyElementsIn(result)
            .inOrder()
    }
}
