package com.chibatching.kotpref.moshipref

import android.content.Context
import android.content.SharedPreferences
import com.chibatching.kotpref.Kotpref
import com.chibatching.kotpref.KotprefModel
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.Rfc3339DateJsonAdapter
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import java.util.*


@RunWith(ParameterizedRobolectricTestRunner::class)
class MoshiSupportTest(private val commitAllProperties: Boolean) {

    companion object {
        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters(name = "commitAllProperties = {0}")
        fun data(): Collection<Array<out Any>> {
            return Arrays.asList(arrayOf(false), arrayOf(true))
        }

        fun createDefaultContent(): Content
                = Content("default title", "contents write here", createDate(2017, 1, 5))

        fun createDate(year: Int, month: Int, day: Int): Date =
                Calendar.getInstance().apply {
                    set(year, month, day)
                    set(Calendar.HOUR, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }.time
    }

    class Example(private val commitAllProperties: Boolean) : KotprefModel() {
        override val commitAllPropertiesByDefault: Boolean
            get() = commitAllProperties

        var content by moshiPref(createDefaultContent())

        var nullableContent: Content? by moshiNullablePref()
    }

    lateinit var example: Example
    lateinit var context: Context
    lateinit var pref: SharedPreferences

    @Before
    fun setUp() {
        context = RuntimeEnvironment.application
        Kotpref.init(context)
        Kotpref.moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .add(Date::class.java, Rfc3339DateJsonAdapter())
                .build()
        example = Example(commitAllProperties)

        pref = example.preferences
        pref.edit().clear().commit()
    }

    @After
    fun tearDown() {
        example.clear()
    }

    @Test
    fun moshiPrefDefaultIsDefined() {
        assertThat(example.content).isEqualTo(createDefaultContent())
    }

    @Test
    fun setMoshiPrefSetCausePreferenceUpdate() {
        example.content = Content("new title", "this is new content", createDate(2017, 1, 25))
        assertThat(example.content).isEqualTo(Content("new title", "this is new content", createDate(2017, 1, 25)))
        assertThat(example.content).isEqualTo(Kotpref.moshi?.adapter(Content::class.java)?.fromJson(pref.getString("content", "")))
    }

    @Test
    fun moshiNullablePrefDefaultIsNull() {
        assertThat(example.nullableContent).isNull()
    }

    @Test
    fun moshiNullablePrefCausePreferenceUpdate() {
        example.nullableContent = Content("nullable content", "this is not null", createDate(2017, 1, 20))
        assertThat(example.nullableContent).isEqualTo(Content("nullable content", "this is not null", createDate(2017, 1, 20)))
        assertThat(example.nullableContent).isEqualTo(Kotpref.moshi?.adapter(Content::class.java)?.fromJson(pref.getString("nullableContent", "")))
    }

    @Test
    fun moshiNullablePrefSetNull() {

        fun setNull() {
            example.nullableContent = null
        }
        setNull()
        assertThat(example.nullableContent).isNull()
        assertThat(example.nullableContent).isEqualTo(Kotpref.moshi?.adapter(Content::class.java)?.fromJson(pref.getString("nullableContent", "")))
    }
}
