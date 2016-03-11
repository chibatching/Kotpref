package com.chibatching.kotpref

import android.content.Context
import android.content.SharedPreferences
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import android.test.AndroidTestCase
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*


@RunWith(AndroidJUnit4::class)
class KotprefBasicTest : AndroidTestCase() {

    object Example : KotprefModel() {
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

    lateinit private var pref: SharedPreferences

    @Before
    override public fun setUp() {
        super.setUp()
        context = InstrumentationRegistry.getTargetContext()
        Kotpref.init(context)

        Example.clear()
        pref = context.getSharedPreferences(Example.javaClass.simpleName, Context.MODE_PRIVATE)
        pref.edit().clear().commit()
    }

    @After
    override public fun tearDown() {
        Example.clear()
        super.tearDown()
    }

    @Test
    fun intPrefVarDefaultIs0() {
        assertThat(Example.testIntVar, equalTo(0))
    }

    @Test
    fun setIntPrefVarSetSameValueToPreference() {
        Example.testIntVar = 4320
        assertThat(Example.testIntVar, equalTo(4320))
        assertThat(Example.testIntVar, equalTo(pref.getInt("testIntVar", 0)))
    }

    @Test
    fun longPrefVarDefaultIs0() {
        assertThat(Example.testLongVar, equalTo(0L))
    }

    @Test
    fun setLongPrefVarSetSameValueToPreference() {
        Example.testLongVar = 83109402L
        assertThat(Example.testLongVar, equalTo(83109402L))
        assertThat(Example.testLongVar, equalTo(pref.getLong("testLongVar", 0L)))
    }

    @Test
    fun floatPrefVarDefaultIs0() {
        assertThat(Example.testFloatVar, equalTo(0f))
    }

    @Test
    fun setFloatPrefVarSetSameValueToPreference() {
        Example.testFloatVar = 78422.214F
        assertThat(Example.testFloatVar, equalTo(78422.214F))
        assertThat(Example.testFloatVar, equalTo(pref.getFloat("testFloatVar", 0f)))
    }

    @Test
    fun booleanPrefVarDefaultIsFalse() {
        assertThat(Example.testBooleanVar, equalTo(false))
    }

    @Test
    fun setBooleanPrefVarSetSameValueToPreference() {
        Example.testBooleanVar = false
        assertThat(Example.testBooleanVar, equalTo(false))
        assertThat(Example.testBooleanVar, equalTo(pref.getBoolean("testBooleanVar", false)))
    }

    @Test
    fun stringPrefVarDefaultIsEmpty() {
        assertThat(Example.testStringVar, equalTo(""))
    }

    @Test
    fun setStringPrefVarSetSameValueToPreference() {
        Example.testStringVar = "Ohayo!"
        assertThat(Example.testStringVar, equalTo("Ohayo!"))
        assertThat(Example.testStringVar, equalTo(pref.getString("testStringVar", "")))
    }

    @Test
    fun stringNullablePrefVarDefaultIsNull() {
        assertThat(Example.testStringNullableVar, nullValue())
    }

    @Test
    fun setStringNullablePrefVarSetSameValueToPreference() {
        Example.testStringNullableVar = "Ohayo!"
        assertThat(Example.testStringNullableVar, equalTo("Ohayo!"))
        assertThat(Example.testStringNullableVar, equalTo(pref.getString("testStringNullableVar", "")))
    }

    @Test
    fun stringSetPrefValDefaultSizeIs0() {
        assertThat(Example.testStringSetVal, hasSize(0))
    }

    @Test
    fun addRemoveStringSetPrefValItemsUpdatePreference() {
        Example.testStringSetVal.apply {
            add("test1")
            add("test2")
            add("test3")
            remove("test2")
            add("test4")
        }

        assertThat(Example.testStringSetVal, containsInAnyOrder("test1", "test3", "test4"))
        assertThat(pref.getStringSet("testStringSetVal", null), containsInAnyOrder("test1", "test3", "test4"))
    }

    @Test
    fun addAllToStringSetPrefValUpdatePreference() {
        val addSet = TreeSet<String>().apply {
            add("test1")
            add("test2")
            add("test3")
        }
        Example.testStringSetVal.apply {
            add("test4")
            addAll(addSet)
        }

        assertThat(Example.testStringSetVal, containsInAnyOrder("test1", "test2", "test3", "test4"))
        assertThat(pref.getStringSet("testStringSetVal", null), containsInAnyOrder("test1", "test2", "test3", "test4"))
    }

    @Test
    fun removeAllToStringSetPrefValUpdatePreference() {
        val removeSet = TreeSet<String>().apply {
            add("test2")
            add("test4")
        }
        Example.testStringSetVal.apply {
            add("test1")
            add("test2")
            add("test3")
            add("test4")
            removeAll(removeSet)
        }

        assertThat(Example.testStringSetVal, containsInAnyOrder("test1", "test3"))
        assertThat(pref.getStringSet("testStringSetVal", null), containsInAnyOrder("test1", "test3"))
    }

    @Test
    fun retainAllToStringSetPrefValUpdatePreference() {
        val retainSet = TreeSet<String>().apply {
            add("test2")
            add("test4")
            add("test5")
        }
        Example.testStringSetVal.apply {
            add("test1")
            add("test2")
            add("test3")
            add("test4")
            retainAll(retainSet)
        }

        assertThat(Example.testStringSetVal, containsInAnyOrder("test2", "test4"))
        assertThat(pref.getStringSet("testStringSetVal", null), containsInAnyOrder("test2", "test4"))
    }

    @Test
    fun lazyDefaultStringSetDefaultIsSetDefinedInLazyBlock() {
        assertThat(Example.testLazyDefaultSS, containsInAnyOrder("Lazy set item 1", "Lazy set item 2", "Lazy set item 3"))
    }

    @Test
    fun changedPrefVarCanReadBothInAndOutBulkEdit() {
        Example.testIntVar = 30

        Example.bulk {
            testIntVar = 5839

            assertThat(testIntVar, equalTo(5839))
        }
        assertThat(Example.testIntVar, equalTo(5839))
    }

    @Test
    fun changedPrefVarNotAffectPreferenceInBulkEdit() {
        Example.testLongVar = -9831L

        Example.bulk {
            testLongVar = 831456L

            assertThat(pref.getLong("testLongVar", 0L), equalTo(-9831L))
        }
        assertThat(pref.getLong("testLongVar", 0L), equalTo(831456L))
    }

    @Test
    fun occurErrorInBulkEditCancelTransaction() {
        Example.testStringVar = "before"

        try {
            Example.bulk {
                testStringVar = "edit in bulk"
                throw Exception()
            }
        } catch (e: Exception) {
        }
        assertThat(Example.testStringVar, equalTo("before"))
        assertThat(pref.getString("testStringVar", ""), equalTo("before"))
    }

    @Test
    fun addRemoveStringSetPrefValCanReadBothInAndOutBulkEdit() {
        Example.testStringSetVal.add("test1")

        Example.bulk {
            testStringSetVal.add("test2")
            testStringSetVal.add("test3")
            testStringSetVal.remove("test2")

            assertThat(testStringSetVal, containsInAnyOrder("test1", "test3"))
        }
        assertThat(Example.testStringSetVal, containsInAnyOrder("test1", "test3"))
    }

    @Test
    fun addRemoveStringSetPrefValNotAffectPreferenceInBulkEdit() {
        Example.testStringSetVal.add("test1")

        Example.bulk {
            testStringSetVal.add("test2")
            testStringSetVal.add("test3")
            testStringSetVal.remove("test2")

            assertThat(pref.getStringSet("testStringSetVal", TreeSet<String>()), containsInAnyOrder("test1"))
        }
        assertThat(pref.getStringSet("testStringSetVal", TreeSet<String>()), containsInAnyOrder("test1", "test3"))
    }

    @Test
    fun addAllStringSetPrefValCanReadBothInAndOutBulkEdit() {
        Example.testStringSetVal.add("test1")

        val addSet = TreeSet<String>().apply {
            add("test2")
            add("test3")
        }

        Example.bulk {
            testStringSetVal.addAll(addSet)

            assertThat(testStringSetVal, containsInAnyOrder("test1", "test2", "test3"))
        }
        assertThat(Example.testStringSetVal, containsInAnyOrder("test1", "test2", "test3"))
    }

    @Test
    fun addAllStringSetPrefValNotAffectPreferenceInBulkEdit() {
        Example.testStringSetVal.add("test1")

        val addSet = TreeSet<String>().apply {
            add("test2")
            add("test3")
        }

        Example.bulk {
            testStringSetVal.addAll(addSet)

            assertThat(pref.getStringSet("testStringSetVal", TreeSet<String>()), containsInAnyOrder("test1"))
        }
        assertThat(pref.getStringSet("testStringSetVal", TreeSet<String>()), containsInAnyOrder("test1", "test2", "test3"))
    }

    @Test
    fun removeAllStringSetPrefValCanReadBothInAndOutBulkEdit() {
        Example.testStringSetVal.apply {
            add("test1")
            add("test2")
            add("test3")
        }

        val removeSet = TreeSet<String>().apply {
            add("test1")
            add("test3")
        }

        Example.bulk {
            testStringSetVal.removeAll(removeSet)

            assertThat(testStringSetVal, containsInAnyOrder("test2"))
        }
        assertThat(Example.testStringSetVal, containsInAnyOrder("test2"))
    }

    @Test
    fun removeAllStringSetPrefValNotAffectPreferenceInBulkEdit() {
        Example.testStringSetVal.apply {
            add("test1")
            add("test2")
            add("test3")
        }

        val removeSet = TreeSet<String>().apply {
            add("test1")
            add("test3")
        }

        Example.bulk {
            testStringSetVal.removeAll(removeSet)

            assertThat(pref.getStringSet("testStringSetVal", TreeSet<String>()), containsInAnyOrder("test1", "test2", "test3"))
        }
        assertThat(pref.getStringSet("testStringSetVal", TreeSet<String>()), containsInAnyOrder("test2"))
    }

    @Test
    fun retainAllStringSetPrefValCanReadBothInAndOutBulkEdit() {
        Example.testStringSetVal.apply {
            add("test1")
            add("test2")
            add("test3")
        }

        val retainSet = TreeSet<String>().apply {
            add("test1")
            add("test3")
            add("test4")
        }

        Example.bulk {
            testStringSetVal.retainAll(retainSet)

            assertThat(testStringSetVal, containsInAnyOrder("test1", "test3"))
        }
        assertThat(Example.testStringSetVal, containsInAnyOrder("test1", "test3"))
    }

    @Test
    fun retainAllStringSetPrefValNotAffectPreferenceInBulkEdit() {
        Example.testStringSetVal.apply {
            add("test1")
            add("test2")
            add("test3")
        }

        val retainSet = TreeSet<String>().apply {
            add("test1")
            add("test3")
            add("test4")
        }

        Example.bulk {
            testStringSetVal.retainAll(retainSet)

            assertThat(pref.getStringSet("testStringSetVal", TreeSet<String>()), containsInAnyOrder("test1", "test2", "test3"))
        }
        assertThat(pref.getStringSet("testStringSetVal", TreeSet<String>()), containsInAnyOrder("test1", "test3"))
    }
}