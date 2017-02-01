package com.chibatching.kotpref.gsonpref

import android.content.Context
import android.content.SharedPreferences
import com.chibatching.kotpref.BuildConfig
import com.chibatching.kotpref.Kotpref
import com.chibatching.kotpref.KotprefModel
import com.google.gson.Gson
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import java.util.*


@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class, manifest = Config.NONE, sdk = intArrayOf(23))
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
    fun gsonPrefDefaultSameValueAsDefined() {
        assertThat(example.content, Matchers.equalTo(createDefaultContent()))
    }

    @Test
    fun setGsonPrefSetSameValueToPreference() {
        example.content = Content("new title", "this is new content", createDate(2017, 1, 25))
        assertThat(example.content, Matchers.equalTo(Content("new title", "this is new content", createDate(2017, 1, 25))))
        assertThat(example.content, Matchers.equalTo(Kotpref.gson?.fromJson(pref.getString("content", ""), Content::class.java)))
    }

    @Test
    fun gsonNullablePrefDefaultIsNull() {
        assertThat(example.nullableContent, Matchers.nullValue())
    }

    @Test
    fun gsonNullablePrefSetSameValueToPreference() {
        example.nullableContent= Content("nullable content", "this is not null", createDate(2017, 1, 20))
        assertThat(example.nullableContent, Matchers.equalTo(Content("nullable content", "this is not null", createDate(2017, 1, 20))))
        assertThat(example.nullableContent, Matchers.equalTo(Kotpref.gson?.fromJson(pref.getString("nullableContent", ""), Content::class.java)))
    }

    @Test
    fun gsonNullablePrefSetNull() {
        example.nullableContent= null
        assertThat(example.nullableContent, Matchers.nullValue())
        assertThat(example.nullableContent, Matchers.equalTo(Kotpref.gson?.fromJson(pref.getString("nullableContent", ""), Content::class.java)))
    }
}
