package com.chibatching.kotpref.enumsupport

import android.content.Context
import android.content.SharedPreferences
import com.chibatching.kotpref.BuildConfig
import com.chibatching.kotpref.Kotpref
import com.chibatching.kotpref.KotprefModel
import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config


@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class, manifest = Config.NONE, sdk = intArrayOf(23))
class EnumSupportTest {

    class Example : KotprefModel() {
        var testEnumValue by enumValuePref(ExampleEnum.FIRST)
        var testEnumNullableValue: ExampleEnum? by nullableEnumValuePref()
        var testEnumOrdinal by enumOrdinalPref(ExampleEnum.FIRST)
    }

    lateinit var example: Example
    lateinit var context: Context
    lateinit var pref: SharedPreferences

    @Before
    fun setUp() {
        context = RuntimeEnvironment.application
        Kotpref.init(context)
        example = Example()

        pref = context.getSharedPreferences(example.javaClass.simpleName, Context.MODE_PRIVATE)
        pref.edit().clear().commit()
    }

    @After
    fun tearDown() {
        example.clear()
    }

    @Test
    fun enumValuePrefVarDefaultSameValueAsDefined() {
        assertThat(example.testEnumValue, Matchers.equalTo(ExampleEnum.FIRST))
    }

    @Test
    fun setEnumValuePrefVarSetSameValueToPreference() {
        example.testEnumValue = ExampleEnum.SECOND
        assertThat(example.testEnumValue, Matchers.equalTo(ExampleEnum.SECOND))
        assertThat(example.testEnumValue.name, Matchers.equalTo(pref.getString("testEnumValue", "")))
    }

    @Test
    fun enumNullableValuePrefVarDefaultIsNull() {
        assertThat(example.testEnumNullableValue, Matchers.nullValue())
    }

    @Test
    fun setEnumNullableValuePrefVarSetSameValueToPreference() {
        example.testEnumNullableValue = ExampleEnum.SECOND
        assertThat(example.testEnumNullableValue, Matchers.equalTo(ExampleEnum.SECOND))
        assertThat(example.testEnumNullableValue!!.name, Matchers.equalTo(pref.getString("testEnumNullableValue", "")))
    }

    @Test
    fun enumOrdinalPrefVarDefaultSameValueAsDefined() {
        MatcherAssert.assertThat(example.testEnumOrdinal, Matchers.equalTo(ExampleEnum.FIRST))
    }

    @Test
    fun setEnumOrdinalPrefVarSetSameValueToPreference() {
        example.testEnumOrdinal = ExampleEnum.SECOND
        MatcherAssert.assertThat(example.testEnumOrdinal, Matchers.equalTo(ExampleEnum.SECOND))
        MatcherAssert.assertThat(example.testEnumOrdinal.ordinal, Matchers.equalTo(pref.getInt("testEnumOrdinal", 0)))
    }

}
