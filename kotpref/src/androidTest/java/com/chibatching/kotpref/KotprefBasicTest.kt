package com.chibatching.kotpref

import android.content.Context
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import android.test.AndroidTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*


@RunWith(AndroidJUnit4::class)
public class KotprefBasicTest : AndroidTestCase() {

    object Example : KotprefModel() {
        var testIntVar: Int by intPrefVar()
        var testLongVar: Long by longPrefVar()
        var testFloatVar: Float by floatPrefVar()
        var testBooleanVar: Boolean by booleanPrefVar()
        var testStringVar: String by stringPrefVar()
        val testStringSetVal: MutableSet<String> by stringSetPrefVal(TreeSet<String>())
        val testLazyDefaultSS: MutableSet<String> by stringSetPrefVal {
            val defSet = LinkedHashSet<String>()
            defSet.add("Lazy set item 1")
            defSet.add("Lazy set item 2")
            defSet.add("Lazy set item 3")
            defSet
        }
    }

    @Before
    override public fun setUp() {
        super.setUp()
        context = InstrumentationRegistry.getTargetContext()
        Kotpref.init(context)
        context.getSharedPreferences(Example.javaClass.simpleName, Context.MODE_PRIVATE).edit().clear().commit()
    }

    @After
    override public fun tearDown() {
        Example.clear()
        super.tearDown()
    }

    @Test
    public fun testIntPrefVarDefaultValue() {
        Example.clear()
        assertEquals(0, Example.testIntVar)
    }

    @Test
    public fun testIntPrefVarDelegation() {
        Example.clear()
        val pref = context.getSharedPreferences(Example.javaClass.simpleName, Context.MODE_PRIVATE)

        Example.testIntVar = -922
        assertPreferenceEquals(pref, "testIntVar", -922, Example.testIntVar)
        Example.testIntVar = 4320
        assertPreferenceEquals(pref, "testIntVar", 4320, Example.testIntVar)
    }

    @Test
    public fun testLongPrefVarDefaultValue() {
        Example.clear()
        assertEquals(0L, Example.testLongVar)
    }

    @Test
    public fun testLongPrefVarDelegation() {
        Example.clear()
        val pref = context.getSharedPreferences(Example.javaClass.simpleName, Context.MODE_PRIVATE)

        Example.testLongVar = 83109402L
        assertPreferenceEquals(pref, "testLongVar", 83109402L, Example.testLongVar)
        Example.testLongVar = -43824L
        assertPreferenceEquals(pref, "testLongVar", -43824L, Example.testLongVar)
    }

    @Test
    public fun testFloatPrefVarDefaultValue() {
        Example.clear()
        assertEquals(0F, Example.testFloatVar)
    }

    @Test
    public fun testFloatPrefVar() {
        Example.clear()
        val pref = context.getSharedPreferences(Example.javaClass.simpleName, Context.MODE_PRIVATE)

        Example.testFloatVar = 78422.214F
        assertPreferenceEquals(pref, "testFloatVar", 78422.214F, Example.testFloatVar)
        Example.testFloatVar = -0.32394F
        assertPreferenceEquals(pref, "testFloatVar", -0.32394F, Example.testFloatVar)
    }

    @Test
    public fun testBooleanPrefVarDefaultValue() {
        Example.clear()
        assertEquals(false, Example.testBooleanVar)
    }

    @Test
    public fun testBooleanPrefVar() {
        Example.clear()
        val pref = context.getSharedPreferences(Example.javaClass.simpleName, Context.MODE_PRIVATE)

        Example.testBooleanVar = true
        assertPreferenceEquals(pref, "testBooleanVar", true, Example.testBooleanVar)
        Example.testBooleanVar = false
        assertPreferenceEquals(pref, "testBooleanVar", false, Example.testBooleanVar)
    }

    @Test
    public fun testStringPrefVarDefaultValue() {
        Example.clear()
        assertEquals("", Example.testStringVar)
    }

    @Test
    public fun testStringPrefVar() {
        Example.clear()
        val pref = context.getSharedPreferences(Example.javaClass.simpleName, Context.MODE_PRIVATE)

        Example.testStringVar = "Ohayo!"
        assertPreferenceEquals(pref, "testStringVar", "Ohayo!", Example.testStringVar)
        Example.testStringVar = "Oyasumi!"
        assertPreferenceEquals(pref, "testStringVar", "Oyasumi!", Example.testStringVar)
    }

    @Test fun testStringSetPrefVarDefaultValue() {
        Example.clear()
        assertEquals(0, Example.testStringSetVal.size)
    }

    @Test
    public fun testStringSetPrefVar() {
        Example.clear()
        val pref = context.getSharedPreferences(Example.javaClass.simpleName, Context.MODE_PRIVATE)

        val testSet = TreeSet<String>()
        testSet.add("test1")
        testSet.add("test2")
        testSet.add("test3")

        testSet.forEach { Example.testStringSetVal.add(it) }
        assertPreferenceEquals(pref, "testStringSetVal", testSet, Example.testStringSetVal)

        testSet.remove("test2")
        Example.testStringSetVal.remove("test2")
        assertPreferenceEquals(pref, "testStringSetVal", testSet, Example.testStringSetVal)

        testSet.add("test4")
        testSet.add("test5")
        testSet.add("test6")
        Example.testStringSetVal.addAll(testSet)
        assertPreferenceEquals(pref, "testStringSetVal", testSet, Example.testStringSetVal)

        val removeSet = TreeSet<String>()
        removeSet.remove("test2")
        removeSet.remove("test5")

        testSet.removeAll(removeSet)
        Example.testStringSetVal.removeAll(removeSet)
        assertPreferenceEquals(pref, "testStringSetVal", testSet, Example.testStringSetVal)

        testSet.add("test5")
        testSet.retainAll(removeSet)
        Example.testStringSetVal.add("test5")
        Example.testStringSetVal.retainAll(removeSet)
        assertPreferenceEquals(pref, "testStringSetVal", testSet, Example.testStringSetVal)
    }

    @Test
    public fun testLazyDefaultStringSet() {
        Example.clear()

        val testSet = LinkedHashSet<String>()
        testSet.add("Lazy set item 1")
        testSet.add("Lazy set item 2")
        testSet.add("Lazy set item 3")

        assertTrue(testSet.containsAll(Example.testLazyDefaultSS))
        assertEquals(testSet.size, Example.testLazyDefaultSS.size)
    }

    @Test
    public fun testBulkEdit() {
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
    public fun testBulkEditStringSet() {
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