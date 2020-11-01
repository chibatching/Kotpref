package com.chibatching.kotpref.enumpref

import android.content.Context
import android.content.SharedPreferences
import androidx.test.core.app.ApplicationProvider
import com.chibatching.kotpref.KotprefModel
import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.Arrays

@Config(manifest = Config.NONE)
@RunWith(ParameterizedRobolectricTestRunner::class)
internal class EnumSupportTest(private val commitAllProperties: Boolean) {

    companion object {
        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters(name = "commitAllProperties = {0}")
        fun data(): Collection<Array<out Any>> {
            return Arrays.asList(arrayOf(false), arrayOf(true))
        }
    }

    class Example(private val commitAllProperties: Boolean) :
        KotprefModel(ApplicationProvider.getApplicationContext<Context>()) {
        override val commitAllPropertiesByDefault: Boolean
            get() = commitAllProperties

        var testEnumValue by enumValuePref(ExampleEnum.FIRST)
        var testEnumNullableValue: ExampleEnum? by nullableEnumValuePref()
        var testEnumOrdinal by enumOrdinalPref(ExampleEnum.FIRST)
    }

    private lateinit var example: Example
    private lateinit var pref: SharedPreferences

    @Before
    fun setUp() {
        example = Example(commitAllProperties)

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
        assertThat(example.testEnumNullableValue!!.name).isEqualTo(
            pref.getString(
                "testEnumNullableValue",
                ""
            )
        )
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
