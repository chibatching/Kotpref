package com.chibatching.kotpref

import android.content.Context
import android.content.SharedPreferences
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
@Config(constants = BuildConfig::class, manifest = Config.NONE)
class KotprefBasicTest {

    class Example : KotprefModel() {
        var testIntVar: Int by intPrefVar()
        var testLongVar: Long by longPrefVar()
        var testFloatVar: Float by floatPrefVar()
        var testBooleanVar: Boolean by booleanPrefVar()
        var testStringVar: String by stringPrefVar()
        var testStringNullableVar: String? by stringNullablePrefVar()
        val testStringSetVal: MutableSet<String> by stringSetPrefVal(TreeSet<String>())
        val testLazyDefaultSS: MutableSet<String> by stringSetPrefVal {
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
        assertThat(example.testIntVar, equalTo(0))
    }

    @Test
    fun setIntPrefVarSetSameValueToPreference() {
        example.testIntVar = 4320
        assertThat(example.testIntVar, equalTo(4320))
        assertThat(example.testIntVar, equalTo(pref.getInt("testIntVar", 0)))
    }

    @Test
    fun longPrefVarDefaultIs0() {
        assertThat(example.testLongVar, equalTo(0L))
    }

    @Test
    fun setLongPrefVarSetSameValueToPreference() {
        example.testLongVar = 83109402L
        assertThat(example.testLongVar, equalTo(83109402L))
        assertThat(example.testLongVar, equalTo(pref.getLong("testLongVar", 0L)))
    }

    @Test
    fun floatPrefVarDefaultIs0() {
        assertThat(example.testFloatVar, equalTo(0f))
    }

    @Test
    fun setFloatPrefVarSetSameValueToPreference() {
        example.testFloatVar = 78422.214F
        assertThat(example.testFloatVar, equalTo(78422.214F))
        assertThat(example.testFloatVar, equalTo(pref.getFloat("testFloatVar", 0f)))
    }

    @Test
    fun booleanPrefVarDefaultIsFalse() {
        assertThat(example.testBooleanVar, equalTo(false))
    }

    @Test
    fun setBooleanPrefVarSetSameValueToPreference() {
        example.testBooleanVar = false
        assertThat(example.testBooleanVar, equalTo(false))
        assertThat(example.testBooleanVar, equalTo(pref.getBoolean("testBooleanVar", false)))
    }

    @Test
    fun stringPrefVarDefaultIsEmpty() {
        assertThat(example.testStringVar, equalTo(""))
    }

    @Test
    fun setStringPrefVarSetSameValueToPreference() {
        example.testStringVar = "Ohayo!"
        assertThat(example.testStringVar, equalTo("Ohayo!"))
        assertThat(example.testStringVar, equalTo(pref.getString("testStringVar", "")))
    }

    @Test
    fun stringNullablePrefVarDefaultIsNull() {
        assertThat(example.testStringNullableVar, nullValue())
    }

    @Test
    fun setStringNullablePrefVarSetSameValueToPreference() {
        example.testStringNullableVar = "Ohayo!"
        assertThat(example.testStringNullableVar, equalTo("Ohayo!"))
        assertThat(example.testStringNullableVar, equalTo(pref.getString("testStringNullableVar", "")))
    }

    @Test
    fun stringSetPrefValDefaultSizeIs0() {
        assertThat(example.testStringSetVal, hasSize(0))
    }

    @Test
    fun addRemoveStringSetPrefValItemsUpdatePreference() {
        example.testStringSetVal.apply {
            add("test1")
            add("test2")
            add("test3")
            remove("test2")
            add("test4")
        }

        assertThat(example.testStringSetVal, containsInAnyOrder("test1", "test3", "test4"))
        assertThat(pref.getStringSet("testStringSetVal", null), containsInAnyOrder("test1", "test3", "test4"))
    }

    @Test
    fun addAllToStringSetPrefValUpdatePreference() {
        val addSet = TreeSet<String>().apply {
            add("test1")
            add("test2")
            add("test3")
        }
        example.testStringSetVal.apply {
            add("test4")
            addAll(addSet)
        }

        assertThat(example.testStringSetVal, containsInAnyOrder("test1", "test2", "test3", "test4"))
        assertThat(pref.getStringSet("testStringSetVal", null), containsInAnyOrder("test1", "test2", "test3", "test4"))
    }

    @Test
    fun removeAllToStringSetPrefValUpdatePreference() {
        val removeSet = TreeSet<String>().apply {
            add("test2")
            add("test4")
        }
        example.testStringSetVal.apply {
            add("test1")
            add("test2")
            add("test3")
            add("test4")
            removeAll(removeSet)
        }

        assertThat(example.testStringSetVal, containsInAnyOrder("test1", "test3"))
        assertThat(pref.getStringSet("testStringSetVal", null), containsInAnyOrder("test1", "test3"))
    }

    @Test
    fun retainAllToStringSetPrefValUpdatePreference() {
        val retainSet = TreeSet<String>().apply {
            add("test2")
            add("test4")
            add("test5")
        }
        example.testStringSetVal.apply {
            add("test1")
            add("test2")
            add("test3")
            add("test4")
            retainAll(retainSet)
        }

        assertThat(example.testStringSetVal, containsInAnyOrder("test2", "test4"))
        assertThat(pref.getStringSet("testStringSetVal", null), containsInAnyOrder("test2", "test4"))
    }

    @Test
    fun lazyDefaultStringSetDefaultIsSetDefinedInLazyBlock() {
        assertThat(example.testLazyDefaultSS, containsInAnyOrder("Lazy set item 1", "Lazy set item 2", "Lazy set item 3"))
    }

    @Test
    fun changedPrefVarCanReadBothInAndOutBulkEdit() {
        example.testIntVar = 30

        example.bulk {
            testIntVar = 5839

            assertThat(testIntVar, equalTo(5839))
        }
        assertThat(example.testIntVar, equalTo(5839))
    }

    @Test
    fun changedPrefVarNotAffectPreferenceInBulkEdit() {
        example.testLongVar = -9831L

        example.bulk {
            testLongVar = 831456L

            assertThat(pref.getLong("testLongVar", 0L), equalTo(-9831L))
        }
        assertThat(pref.getLong("testLongVar", 0L), equalTo(831456L))
    }

    @Test
    fun occurErrorInBulkEditCancelTransaction() {
        example.testStringVar = "before"

        try {
            example.bulk {
                testStringVar = "edit in bulk"
                throw Exception()
            }
        } catch (e: Exception) {
        }
        assertThat(example.testStringVar, equalTo("before"))
        assertThat(pref.getString("testStringVar", ""), equalTo("before"))
    }

    @Test
    fun addRemoveStringSetPrefValCanReadBothInAndOutBulkEdit() {
        example.testStringSetVal.add("test1")

        example.bulk {
            testStringSetVal.add("test2")
            testStringSetVal.add("test3")
            testStringSetVal.remove("test2")

            assertThat(testStringSetVal, containsInAnyOrder("test1", "test3"))
        }
        assertThat(example.testStringSetVal, containsInAnyOrder("test1", "test3"))
    }

    @Test
    fun addRemoveStringSetPrefValNotAffectPreferenceInBulkEdit() {
        example.testStringSetVal.add("test1")

        example.bulk {
            testStringSetVal.add("test2")
            testStringSetVal.add("test3")
            testStringSetVal.remove("test2")

            assertThat(pref.getStringSet("testStringSetVal", TreeSet<String>()), containsInAnyOrder("test1"))
        }
        assertThat(pref.getStringSet("testStringSetVal", TreeSet<String>()), containsInAnyOrder("test1", "test3"))
    }

    @Test
    fun addAllStringSetPrefValCanReadBothInAndOutBulkEdit() {
        example.testStringSetVal.add("test1")

        val addSet = TreeSet<String>().apply {
            add("test2")
            add("test3")
        }

        example.bulk {
            testStringSetVal.addAll(addSet)

            assertThat(testStringSetVal, containsInAnyOrder("test1", "test2", "test3"))
        }
        assertThat(example.testStringSetVal, containsInAnyOrder("test1", "test2", "test3"))
    }

    @Test
    fun addAllStringSetPrefValNotAffectPreferenceInBulkEdit() {
        example.testStringSetVal.add("test1")

        val addSet = TreeSet<String>().apply {
            add("test2")
            add("test3")
        }

        example.bulk {
            testStringSetVal.addAll(addSet)

            assertThat(pref.getStringSet("testStringSetVal", TreeSet<String>()), containsInAnyOrder("test1"))
        }
        assertThat(pref.getStringSet("testStringSetVal", TreeSet<String>()), containsInAnyOrder("test1", "test2", "test3"))
    }

    @Test
    fun removeAllStringSetPrefValCanReadBothInAndOutBulkEdit() {
        example.testStringSetVal.apply {
            add("test1")
            add("test2")
            add("test3")
        }

        val removeSet = TreeSet<String>().apply {
            add("test1")
            add("test3")
        }

        example.bulk {
            testStringSetVal.removeAll(removeSet)

            assertThat(testStringSetVal, containsInAnyOrder("test2"))
        }
        assertThat(example.testStringSetVal, containsInAnyOrder("test2"))
    }

    @Test
    fun removeAllStringSetPrefValNotAffectPreferenceInBulkEdit() {
        example.testStringSetVal.apply {
            add("test1")
            add("test2")
            add("test3")
        }

        val removeSet = TreeSet<String>().apply {
            add("test1")
            add("test3")
        }

        example.bulk {
            testStringSetVal.removeAll(removeSet)

            assertThat(pref.getStringSet("testStringSetVal", TreeSet<String>()), containsInAnyOrder("test1", "test2", "test3"))
        }
        assertThat(pref.getStringSet("testStringSetVal", TreeSet<String>()), containsInAnyOrder("test2"))
    }

    @Test
    fun retainAllStringSetPrefValCanReadBothInAndOutBulkEdit() {
        example.testStringSetVal.apply {
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
            testStringSetVal.retainAll(retainSet)

            assertThat(testStringSetVal, containsInAnyOrder("test1", "test3"))
        }
        assertThat(example.testStringSetVal, containsInAnyOrder("test1", "test3"))
    }

    @Test
    fun retainAllStringSetPrefValNotAffectPreferenceInBulkEdit() {
        example.testStringSetVal.apply {
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
            testStringSetVal.retainAll(retainSet)

            assertThat(pref.getStringSet("testStringSetVal", TreeSet<String>()), containsInAnyOrder("test1", "test2", "test3"))
        }
        assertThat(pref.getStringSet("testStringSetVal", TreeSet<String>()), containsInAnyOrder("test1", "test3"))
    }
}
