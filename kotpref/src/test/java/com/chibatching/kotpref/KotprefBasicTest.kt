package com.chibatching.kotpref

import android.annotation.TargetApi
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
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
class KotprefBasicTest {

    class Example : KotprefModel() {
        var testInt by intPref()
        var testLong by longPref()
        var testFloat by floatPref()
        var testBoolean by booleanPref()
        var testString by stringPref()
        var testStringNullable by nullableStringPref()
        var testEnumValue by enumValuePref(ExampleEnum::class, ExampleEnum.FIRST)
        var testEnumNullableValue by nullableEnumValuePref(ExampleEnum::class)
        var testEnumOrdinal by enumOrdinalPref(ExampleEnum::class, ExampleEnum.FIRST)
        val testStringSet by stringSetPref(TreeSet<String>())
        val testLazyDefaultSS by stringSetPref {
            val defSet = LinkedHashSet<String>()
            defSet.add("Lazy set item 1")
            defSet.add("Lazy set item 2")
            defSet.add("Lazy set item 3")
            defSet
        }
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
    fun intPrefVarDefaultIs0() {
        assertThat(example.testInt, equalTo(0))
    }

    @Test
    fun setIntPrefVarSetSameValueToPreference() {
        example.testInt = 4320
        assertThat(example.testInt, equalTo(4320))
        assertThat(example.testInt, equalTo(pref.getInt("testInt", 0)))
    }

    @Test
    fun longPrefVarDefaultIs0() {
        assertThat(example.testLong, equalTo(0L))
    }

    @Test
    fun setLongPrefVarSetSameValueToPreference() {
        example.testLong = 83109402L
        assertThat(example.testLong, equalTo(83109402L))
        assertThat(example.testLong, equalTo(pref.getLong("testLong", 0L)))
    }

    @Test
    fun floatPrefVarDefaultIs0() {
        assertThat(example.testFloat, equalTo(0f))
    }

    @Test
    fun setFloatPrefVarSetSameValueToPreference() {
        example.testFloat = 78422.214F
        assertThat(example.testFloat, equalTo(78422.214F))
        assertThat(example.testFloat, equalTo(pref.getFloat("testFloat", 0f)))
    }

    @Test
    fun booleanPrefVarDefaultIsFalse() {
        assertThat(example.testBoolean, equalTo(false))
    }

    @Test
    fun setBooleanPrefVarSetSameValueToPreference() {
        example.testBoolean = false
        assertThat(example.testBoolean, equalTo(false))
        assertThat(example.testBoolean, equalTo(pref.getBoolean("testBoolean", false)))
    }

    @Test
    fun stringPrefVarDefaultIsEmpty() {
        assertThat(example.testString, equalTo(""))
    }

    @Test
    fun setStringPrefVarSetSameValueToPreference() {
        example.testString = "Ohayo!"
        assertThat(example.testString, equalTo("Ohayo!"))
        assertThat(example.testString, equalTo(pref.getString("testString", "")))
    }

    @Test
    fun stringNullablePrefVarDefaultIsNull() {
        assertThat(example.testStringNullable, nullValue())
    }

    @Test
    fun setStringNullablePrefVarSetSameValueToPreference() {
        example.testStringNullable = "Ohayo!"
        assertThat(example.testStringNullable, equalTo("Ohayo!"))
        assertThat(example.testStringNullable, equalTo(pref.getString("testStringNullable", "")))
    }

    @Test
    fun enumValuePrefVarDefaultSameValueAsDefined() {
        assertThat(example.testEnumValue, equalTo(ExampleEnum.FIRST))
    }

    @Test
    fun setEnumValuePrefVarSetSameValueToPreference() {
        example.testEnumValue = ExampleEnum.SECOND
        assertThat(example.testEnumValue, equalTo(ExampleEnum.SECOND))
        assertThat(example.testEnumValue.name, equalTo(pref.getString("testEnumValue", "")))
    }

    @Test
    fun enumNullableValuePrefVarDefaultIsNull() {
        assertThat(example.testEnumNullableValue, nullValue())
    }

    @Test
    fun setEnumNullableValuePrefVarSetSameValueToPreference() {
        example.testEnumNullableValue = ExampleEnum.SECOND
        assertThat(example.testEnumNullableValue, equalTo(ExampleEnum.SECOND))
        assertThat(example.testEnumNullableValue!!.name, equalTo(pref.getString("testEnumNullableValue", "")))
    }

    @Test
    fun enumOrdinalPrefVarDefaultSameValueAsDefined() {
        assertThat(example.testEnumOrdinal, equalTo(ExampleEnum.FIRST))
    }

    @Test
    fun setEnumOrdinalPrefVarSetSameValueToPreference() {
        example.testEnumOrdinal = ExampleEnum.SECOND
        assertThat(example.testEnumOrdinal, equalTo(ExampleEnum.SECOND))
        assertThat(example.testEnumOrdinal.ordinal, equalTo(pref.getInt("testEnumOrdinal", 0)))
    }

    @Test
    fun stringSetPrefValDefaultSizeIs0() {
        assertThat(example.testStringSet, hasSize(0))
    }

    @Test
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    fun addRemoveStringSetPrefValItemsUpdatePreference() {
        example.testStringSet.apply {
            add("test1")
            add("test2")
            add("test3")
            remove("test2")
            add("test4")
        }

        assertThat(example.testStringSet, containsInAnyOrder("test1", "test3", "test4"))
        assertThat(pref.getStringSet("testStringSet", null), containsInAnyOrder("test1", "test3", "test4"))
    }

    @Test
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    fun addAllToStringSetPrefValUpdatePreference() {
        val addSet = TreeSet<String>().apply {
            add("test1")
            add("test2")
            add("test3")
        }
        example.testStringSet.apply {
            add("test4")
            addAll(addSet)
        }

        assertThat(example.testStringSet, containsInAnyOrder("test1", "test2", "test3", "test4"))
        assertThat(pref.getStringSet("testStringSet", null), containsInAnyOrder("test1", "test2", "test3", "test4"))
    }

    @Test
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    fun removeAllToStringSetPrefValUpdatePreference() {
        val removeSet = TreeSet<String>().apply {
            add("test2")
            add("test4")
        }
        example.testStringSet.apply {
            add("test1")
            add("test2")
            add("test3")
            add("test4")
            removeAll(removeSet)
        }

        assertThat(example.testStringSet, containsInAnyOrder("test1", "test3"))
        assertThat(pref.getStringSet("testStringSet", null), containsInAnyOrder("test1", "test3"))
    }

    @Test
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    fun retainAllToStringSetPrefValUpdatePreference() {
        val retainSet = TreeSet<String>().apply {
            add("test2")
            add("test4")
            add("test5")
        }
        example.testStringSet.apply {
            add("test1")
            add("test2")
            add("test3")
            add("test4")
            retainAll(retainSet)
        }

        assertThat(example.testStringSet, containsInAnyOrder("test2", "test4"))
        assertThat(pref.getStringSet("testStringSet", null), containsInAnyOrder("test2", "test4"))
    }

    @Test
    fun lazyDefaultStringSetDefaultIsSetDefinedInLazyBlock() {
        assertThat(example.testLazyDefaultSS, containsInAnyOrder("Lazy set item 1", "Lazy set item 2", "Lazy set item 3"))
    }

    @Test
    fun changedPrefVarCanReadBothInAndOutBulkEdit() {
        example.testInt = 30

        example.bulk {
            testInt = 5839

            assertThat(testInt, equalTo(5839))
        }
        assertThat(example.testInt, equalTo(5839))
    }

    @Test
    fun changedPrefVarNotAffectPreferenceInBulkEdit() {
        example.testLong = -9831L

        example.bulk {
            testLong = 831456L

            assertThat(pref.getLong("testLong", 0L), equalTo(-9831L))
        }
        assertThat(pref.getLong("testLong", 0L), equalTo(831456L))
    }

    @Test
    fun occurErrorInBulkEditCancelTransaction() {
        example.testString = "before"

        try {
            example.bulk {
                testString = "edit in bulk"
                throw Exception()
            }
        } catch (e: Exception) {
        }
        assertThat(example.testString, equalTo("before"))
        assertThat(pref.getString("testString", ""), equalTo("before"))
    }

    @Test
    fun addRemoveStringSetPrefValCanReadBothInAndOutBulkEdit() {
        example.testStringSet.add("test1")

        example.bulk {
            testStringSet.add("test2")
            testStringSet.add("test3")
            testStringSet.remove("test2")

            assertThat(testStringSet, containsInAnyOrder("test1", "test3"))
        }
        assertThat(example.testStringSet, containsInAnyOrder("test1", "test3"))
    }

    @Test
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    fun addRemoveStringSetPrefValNotAffectPreferenceInBulkEdit() {
        example.testStringSet.add("test1")

        example.bulk {
            testStringSet.add("test2")
            testStringSet.add("test3")
            testStringSet.remove("test2")

            assertThat(pref.getStringSet("testStringSet", TreeSet<String>()), containsInAnyOrder("test1"))
        }
        assertThat(pref.getStringSet("testStringSet", TreeSet<String>()), containsInAnyOrder("test1", "test3"))
    }

    @Test
    fun addAllStringSetPrefValCanReadBothInAndOutBulkEdit() {
        example.testStringSet.add("test1")

        val addSet = TreeSet<String>().apply {
            add("test2")
            add("test3")
        }

        example.bulk {
            testStringSet.addAll(addSet)

            assertThat(testStringSet, containsInAnyOrder("test1", "test2", "test3"))
        }
        assertThat(example.testStringSet, containsInAnyOrder("test1", "test2", "test3"))
    }

    @Test
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    fun addAllStringSetPrefValNotAffectPreferenceInBulkEdit() {
        example.testStringSet.add("test1")

        val addSet = TreeSet<String>().apply {
            add("test2")
            add("test3")
        }

        example.bulk {
            testStringSet.addAll(addSet)

            assertThat(pref.getStringSet("testStringSet", TreeSet<String>()), containsInAnyOrder("test1"))
        }
        assertThat(pref.getStringSet("testStringSet", TreeSet<String>()), containsInAnyOrder("test1", "test2", "test3"))
    }

    @Test
    fun removeAllStringSetPrefValCanReadBothInAndOutBulkEdit() {
        example.testStringSet.apply {
            add("test1")
            add("test2")
            add("test3")
        }

        val removeSet = TreeSet<String>().apply {
            add("test1")
            add("test3")
        }

        example.bulk {
            testStringSet.removeAll(removeSet)

            assertThat(testStringSet, containsInAnyOrder("test2"))
        }
        assertThat(example.testStringSet, containsInAnyOrder("test2"))
    }

    @Test
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    fun removeAllStringSetPrefValNotAffectPreferenceInBulkEdit() {
        example.testStringSet.apply {
            add("test1")
            add("test2")
            add("test3")
        }

        val removeSet = TreeSet<String>().apply {
            add("test1")
            add("test3")
        }

        example.bulk {
            testStringSet.removeAll(removeSet)

            assertThat(pref.getStringSet("testStringSet", TreeSet<String>()), containsInAnyOrder("test1", "test2", "test3"))
        }
        assertThat(pref.getStringSet("testStringSet", TreeSet<String>()), containsInAnyOrder("test2"))
    }

    @Test
    fun retainAllStringSetPrefValCanReadBothInAndOutBulkEdit() {
        example.testStringSet.apply {
            add("test1")
            add("test2")
            add("test3")
        }

        val retainSet = TreeSet<String>().apply {
            add("test1")
            add("test3")
            add("test4")
        }

        example.bulk {
            testStringSet.retainAll(retainSet)

            assertThat(testStringSet, containsInAnyOrder("test1", "test3"))
        }
        assertThat(example.testStringSet, containsInAnyOrder("test1", "test3"))
    }

    @Test
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    fun retainAllStringSetPrefValNotAffectPreferenceInBulkEdit() {
        example.testStringSet.apply {
            add("test1")
            add("test2")
            add("test3")
        }

        val retainSet = TreeSet<String>().apply {
            add("test1")
            add("test3")
            add("test4")
        }

        example.bulk {
            testStringSet.retainAll(retainSet)

            assertThat(pref.getStringSet("testStringSet", TreeSet<String>()), containsInAnyOrder("test1", "test2", "test3"))
        }
        assertThat(pref.getStringSet("testStringSet", TreeSet<String>()), containsInAnyOrder("test1", "test3"))
    }

    // blockingBulk test

    @Test
    fun changedPrefVarCanReadBothInAndOutImmediateBulkEdit() {
        example.testInt = 30

        example.blockingBulk {
            testInt = 5839

            assertThat(testInt, equalTo(5839))
        }
        assertThat(example.testInt, equalTo(5839))
    }

    @Test
    fun changedPrefVarNotAffectPreferenceInImmediateBulkEdit() {
        example.testLong = -9831L

        example.blockingBulk {
            testLong = 831456L

            assertThat(pref.getLong("testLong", 0L), equalTo(-9831L))
        }
        assertThat(pref.getLong("testLong", 0L), equalTo(831456L))
    }

    @Test
    fun occurErrorInImmediateBulkEditCancelTransaction() {
        example.testString = "before"

        try {
            example.blockingBulk {
                testString = "edit in bulk"
                throw Exception()
            }
        } catch (e: Exception) {
        }
        assertThat(example.testString, equalTo("before"))
        assertThat(pref.getString("testString", ""), equalTo("before"))
    }

    @Test
    fun addRemoveStringSetPrefValCanReadBothInAndOutImmediateBulkEdit() {
        example.testStringSet.add("test1")

        example.blockingBulk {
            testStringSet.add("test2")
            testStringSet.add("test3")
            testStringSet.remove("test2")

            assertThat(testStringSet, containsInAnyOrder("test1", "test3"))
        }
        assertThat(example.testStringSet, containsInAnyOrder("test1", "test3"))
    }

    @Test
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    fun addRemoveStringSetPrefValNotAffectPreferenceInImmediateBulkEdit() {
        example.testStringSet.add("test1")

        example.blockingBulk {
            testStringSet.add("test2")
            testStringSet.add("test3")
            testStringSet.remove("test2")

            assertThat(pref.getStringSet("testStringSet", TreeSet<String>()), containsInAnyOrder("test1"))
        }
        assertThat(pref.getStringSet("testStringSet", TreeSet<String>()), containsInAnyOrder("test1", "test3"))
    }

    @Test
    fun addAllStringSetPrefValCanReadBothInAndOutImmediateBulkEdit() {
        example.testStringSet.add("test1")

        val addSet = TreeSet<String>().apply {
            add("test2")
            add("test3")
        }

        example.blockingBulk {
            testStringSet.addAll(addSet)

            assertThat(testStringSet, containsInAnyOrder("test1", "test2", "test3"))
        }
        assertThat(example.testStringSet, containsInAnyOrder("test1", "test2", "test3"))
    }

    @Test
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    fun addAllStringSetPrefValNotAffectPreferenceInImmediateBulkEdit() {
        example.testStringSet.add("test1")

        val addSet = TreeSet<String>().apply {
            add("test2")
            add("test3")
        }

        example.blockingBulk {
            testStringSet.addAll(addSet)

            assertThat(pref.getStringSet("testStringSet", TreeSet<String>()), containsInAnyOrder("test1"))
        }
        assertThat(pref.getStringSet("testStringSet", TreeSet<String>()), containsInAnyOrder("test1", "test2", "test3"))
    }

    @Test
    fun removeAllStringSetPrefValCanReadBothInAndOutImmediateBulkEdit() {
        example.testStringSet.apply {
            add("test1")
            add("test2")
            add("test3")
        }

        val removeSet = TreeSet<String>().apply {
            add("test1")
            add("test3")
        }

        example.blockingBulk {
            testStringSet.removeAll(removeSet)

            assertThat(testStringSet, containsInAnyOrder("test2"))
        }
        assertThat(example.testStringSet, containsInAnyOrder("test2"))
    }

    @Test
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    fun removeAllStringSetPrefValNotAffectPreferenceInImmediateBulkEdit() {
        example.testStringSet.apply {
            add("test1")
            add("test2")
            add("test3")
        }

        val removeSet = TreeSet<String>().apply {
            add("test1")
            add("test3")
        }

        example.blockingBulk {
            testStringSet.removeAll(removeSet)

            assertThat(pref.getStringSet("testStringSet", TreeSet<String>()), containsInAnyOrder("test1", "test2", "test3"))
        }
        assertThat(pref.getStringSet("testStringSet", TreeSet<String>()), containsInAnyOrder("test2"))
    }

    @Test
    fun retainAllStringSetPrefValCanReadBothInAndOutImmediateBulkEdit() {
        example.testStringSet.apply {
            add("test1")
            add("test2")
            add("test3")
        }

        val retainSet = TreeSet<String>().apply {
            add("test1")
            add("test3")
            add("test4")
        }

        example.blockingBulk {
            testStringSet.retainAll(retainSet)

            assertThat(testStringSet, containsInAnyOrder("test1", "test3"))
        }
        assertThat(example.testStringSet, containsInAnyOrder("test1", "test3"))
    }

    @Test
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    fun retainAllStringSetPrefValNotAffectPreferenceInImmediateBulkEdit() {
        example.testStringSet.apply {
            add("test1")
            add("test2")
            add("test3")
        }

        val retainSet = TreeSet<String>().apply {
            add("test1")
            add("test3")
            add("test4")
        }

        example.blockingBulk {
            testStringSet.retainAll(retainSet)

            assertThat(pref.getStringSet("testStringSet", TreeSet<String>()), containsInAnyOrder("test1", "test2", "test3"))
        }
        assertThat(pref.getStringSet("testStringSet", TreeSet<String>()), containsInAnyOrder("test1", "test3"))
    }
}
