package com.chibatching.kotpref.gsonpref

import android.content.Context
import android.content.SharedPreferences
import com.chibatching.kotpref.Kotpref
import com.chibatching.kotpref.KotprefModel
import com.chibatching.kotpref.KotprefTestRunner
import com.google.gson.Gson
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RuntimeEnvironment
import java.util.*


@RunWith(KotprefTestRunner::class)
class GsonSupportTest {

    companion object {
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

    class Example : KotprefModel() {
        var content by gsonPref(createDefaultContent())

        var nullableContent: Content? by gsonNullablePref()
    }

    lateinit var example: Example
    lateinit var context: Context
    lateinit var pref: SharedPreferences

    @Before
    fun setUp() {
        context = RuntimeEnvironment.application
        Kotpref.init(context)
        Kotpref.gson = Gson()
        example = Example()

        pref = context.getSharedPreferences(example.javaClass.simpleName, Context.MODE_PRIVATE)
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
        assertThat(example.content).isEqualTo(Content("new title", "this is new content", createDate(2017, 1, 25)))
        assertThat(example.content).isEqualTo(Kotpref.gson?.fromJson(pref.getString("content", ""), Content::class.java))
    }

    @Test
    fun gsonNullablePrefDefaultIsNull() {
        assertThat(example.nullableContent).isNull()
    }

    @Test
    fun gsonNullablePrefCausePreferenceUpdate() {
        example.nullableContent = Content("nullable content", "this is not null", createDate(2017, 1, 20))
        assertThat(example.nullableContent).isEqualTo(Content("nullable content", "this is not null", createDate(2017, 1, 20)))
        assertThat(example.nullableContent).isEqualTo(Kotpref.gson?.fromJson(pref.getString("nullableContent", ""), Content::class.java))
    }

    @Test
    fun gsonNullablePrefSetNull() {

        fun setNull() {
            example.nullableContent = null
        }
        setNull()
        assertThat(example.nullableContent).isNull()
        assertThat(example.nullableContent).isEqualTo(Kotpref.gson?.fromJson(pref.getString("nullableContent", ""), Content::class.java))
    }
}
