package com.chibatching.kotpref.enumpref

import android.content.Context
import android.content.SharedPreferences
import com.chibatching.kotpref.Kotpref
import com.chibatching.kotpref.KotprefModel
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment


@RunWith(RobolectricTestRunner::class)
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

        pref = example.preferences
        pref.edit().clear().commit()
    }

    @After
    fun tearDown() {
        example.clear()
    }

    @Test
    fun enumValuePrefDefaultIsDefined() {
        assertThat(example.testEnumValue).isEqualTo(ExampleEnum.FIRST)
    }

    @Test
    fun setEnumValuePrefCausePreferenceUpdate() {
        example.testEnumValue = ExampleEnum.SECOND
        assertThat(example.testEnumValue).isEqualTo(ExampleEnum.SECOND)
        assertThat(example.testEnumValue.name).isEqualTo(pref.getString("testEnumValue", ""))
    }

    @Test
    fun enumNullableValuePrefDefaultIsNull() {
        assertThat(example.testEnumNullableValue).isNull()
    }

    @Test
    fun setEnumNullableValuePrefCausePreferenceUpdate() {
        example.testEnumNullableValue = ExampleEnum.SECOND
        assertThat(example.testEnumNullableValue).isEqualTo(ExampleEnum.SECOND)
        assertThat(example.testEnumNullableValue!!.name).isEqualTo(pref.getString("testEnumNullableValue", ""))
    }

    @Test
    fun enumOrdinalPrefDefaultIsDefined() {
        assertThat(example.testEnumOrdinal).isEqualTo(ExampleEnum.FIRST)
    }

    @Test
    fun setEnumOrdinalPrefCausePreferenceUpdate() {
        example.testEnumOrdinal = ExampleEnum.SECOND
        assertThat(example.testEnumOrdinal).isEqualTo(ExampleEnum.SECOND)
        assertThat(example.testEnumOrdinal.ordinal).isEqualTo(pref.getInt("testEnumOrdinal", 0))
    }
}
