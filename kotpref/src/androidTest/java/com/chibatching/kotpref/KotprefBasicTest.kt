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
    fun testBulkEdit() {
        Example.clear()
        val pref = context.getSharedPreferences(Example.javaClass.simpleName, Context.MODE_PRIVATE)

        Example.testIntVar = -500
        Example.testLongVar = -3000L
        Example.testFloatVar = -3.8F
        Example.testBooleanVar = true
        Example.testStringVar = "committed data"

        Kotpref.bulk(Example) {
            testIntVar = 1024
            testLongVar = 422098523L
            testFloatVar = 43093.301F
            testBooleanVar = false
            testStringVar = "bulk edit"

            assertPreferenceInTransaction(pref, "testIntVar", -500, 1024, testIntVar)
            assertPreferenceInTransaction(pref, "testLongVar", -3000L, 422098523L, testLongVar)
            assertPreferenceInTransaction(pref, "testFloatVar", -3.8F, 43093.301F, testFloatVar)
            assertPreferenceInTransaction(pref, "testBooleanVar", true, false, testBooleanVar)
            assertPreferenceInTransaction(pref, "testStringVar", "committed data", "bulk edit", testStringVar)
        }

        assertPreferenceEquals(pref, "testIntVar", 1024, Example.testIntVar)
        assertPreferenceEquals(pref, "testLongVar", 422098523L, Example.testLongVar)
        assertPreferenceEquals(pref, "testFloatVar", 43093.301F, Example.testFloatVar)
        assertPreferenceEquals(pref, "testBooleanVar", false, Example.testBooleanVar)
        assertPreferenceEquals(pref, "testStringVar", "bulk edit", Example.testStringVar)
    }

    @Test
    fun testBulkEditStringSet() {
        Example.clear()
        val pref = context.getSharedPreferences(Example.javaClass.simpleName, Context.MODE_PRIVATE)

        var testSet = TreeSet<String>()
        val tranSet = TreeSet<String>()

        testSet.add("Initial item")
        tranSet.addAll(testSet)
        Example.testStringSetVal.addAll(testSet)

        Kotpref.bulk(Example) {
            tranSet.add("test1")
            tranSet.add("test2")
            tranSet.add("test3")

            testStringSetVal.add("test1")
            testStringSetVal.add("test2")
            testStringSetVal.add("test3")

            assertPreferenceInTransaction(pref, "testStringSetVal", testSet, tranSet, testStringSetVal)
        }
        assertPreferenceEquals(pref, "testStringSetVal", tranSet, Example.testStringSetVal)
        testSet.addAll(tranSet)
        testSet.retainAll(tranSet)

        Kotpref.bulk(Example) {
            tranSet.remove("test2")
            testStringSetVal.remove("test2")

            assertPreferenceInTransaction(pref, "testStringSetVal", testSet, tranSet, testStringSetVal)
        }
        assertPreferenceEquals(pref, "testStringSetVal", tranSet, Example.testStringSetVal)
        testSet.addAll(tranSet)
        testSet.retainAll(tranSet)

        Kotpref.bulk(Example) {
            val subSet = TreeSet<String>()
            subSet.add("test4")
            subSet.add("test5")
            subSet.add("test6")

            tranSet.addAll(subSet)
            testStringSetVal.addAll(subSet)

            assertPreferenceInTransaction(pref, "testStringSetVal", testSet, tranSet, testStringSetVal)
        }
        assertPreferenceEquals(pref, "testStringSetVal", tranSet, Example.testStringSetVal)
        testSet.addAll(tranSet)
        testSet.retainAll(tranSet)

        Kotpref.bulk(Example) {
            val subSet = TreeSet<String>()
            subSet.add("test4")
            subSet.add("test5")
            subSet.add("test6")

            tranSet.removeAll(subSet)
            testStringSetVal.removeAll(subSet)

            assertPreferenceInTransaction(pref, "testStringSetVal", testSet, tranSet, testStringSetVal)
        }
        assertPreferenceEquals(pref, "testStringSetVal", tranSet, Example.testStringSetVal)
        testSet.addAll(tranSet)
        testSet.retainAll(tranSet)

        Kotpref.bulk(Example) {
            val subSet = TreeSet<String>()
            subSet.add("test4")
            subSet.add("test5")
            subSet.add("test6")

            tranSet.addAll(subSet)
            tranSet.retainAll(subSet)
            testStringSetVal.addAll(subSet)
            testStringSetVal.retainAll(subSet)

            assertPreferenceInTransaction(pref, "testStringSetVal", testSet, tranSet, testStringSetVal)
        }
        assertPreferenceEquals(pref, "testStringSetVal", tranSet, Example.testStringSetVal)
        testSet.addAll(tranSet)
        testSet.retainAll(tranSet)
    }
}