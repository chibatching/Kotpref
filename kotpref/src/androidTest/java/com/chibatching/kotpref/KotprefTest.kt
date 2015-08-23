package com.chibatching.kotpref

import android.content.SharedPreferences
import android.test.AndroidTestCase
import java.util.LinkedHashSet
import java.util.TreeSet
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

/**
 * [Testing Fundamentals](http://d.android.com/tools/testing/testing_android.html)
 */
public class KotprefTest : AndroidTestCase() {

    object Example : KotprefModel() {
        var testIntVar: Int by intPrefVar(Int.MAX_VALUE)
        var testLongVar: Long by longPrefVar(Long.MAX_VALUE)
        var testFloatVar: Float by floatPrefVar(Float.MAX_VALUE)
        var testBooleanVar: Boolean by booleanPrefVar(true)
        var testStringVar: String by stringPrefVar("Default string")
        val testStringSetVal: MutableSet<String> by stringSetPrefVal(TreeSet<String>())
        val testLazyDefaultSS: MutableSet<String> by stringSetPrefVal {
            val defSet = LinkedHashSet<String>()
            defSet.add("Lazy set item 1")
            defSet.add("lazy set item 2")
            defSet.add("lazy set item 3")
            defSet
        }
    }

    fun SharedPreferences.getTestInt() = getInt("testIntVar", Int.MAX_VALUE)
    fun SharedPreferences.getTestLong() = getLong("testLongVar", Long.MAX_VALUE)
    fun SharedPreferences.getTestFloat() = getFloat("testFloatVar", Float.MAX_VALUE)
    fun SharedPreferences.getTestBoolean() = getBoolean("testBooleanVar", true)
    fun SharedPreferences.getTestString() = getString("testStringVar", "Default string")
    fun SharedPreferences.getTestStringSet() = getStringSet("testStringSetVal", TreeSet<String>())

    object NameExample : KotprefModel() {
        override val kotprefName: String = "name_example"
        var testIntVar: Int by intPrefVar(Int.MAX_VALUE)
    }

    override fun setUp() {
        Kotpref.init(getContext())
        getContext().getSharedPreferences(Example.kotprefName, Example.kotprefMode).edit().clear().commit()
        getContext().getSharedPreferences(NameExample.kotprefName, NameExample.kotprefMode).edit().clear().commit()
    }

    public fun testIntPrefVar() {
        // Init model
        Example.clear()

        val pref = getContext().getSharedPreferences(Example.kotprefName, Example.kotprefMode)

        assertEquals(Int.MAX_VALUE, Example.testIntVar)

        Example.testIntVar = -922
        assertEquals(pref.getTestInt(), Example.testIntVar)
        assertEquals(pref.getTestInt(), -922)

        Example.testIntVar = 4320
        assertEquals(pref.getTestInt(), Example.testIntVar)
        assertEquals(pref.getTestInt(), 4320)

        Example.clear()
        assertEquals(Int.MAX_VALUE, Example.testIntVar)
    }

    public fun testLongPrefVar() {
        // Init model
        Example.clear()

        val pref = getContext().getSharedPreferences(Example.kotprefName, Example.kotprefMode)

        assertEquals(Long.MAX_VALUE, Example.testLongVar)

        Example.testLongVar = 83109402L
        assertEquals(pref.getTestLong(), Example.testLongVar)
        assertEquals(pref.getTestLong(), 83109402L)

        Example.testLongVar = -43824L
        assertEquals(pref.getTestLong(), Example.testLongVar)
        assertEquals(pref.getTestLong(), -43824L)

        Example.clear()
        assertEquals(Long.MAX_VALUE, Example.testLongVar)
    }

    public fun testFloatPrefVar() {
        // Init model
        Example.clear()

        val pref = getContext().getSharedPreferences(Example.kotprefName, Example.kotprefMode)

        assertEquals(Float.MAX_VALUE, Example.testFloatVar)

        Example.testFloatVar = 78422.214F
        assertEquals(pref.getTestFloat(), Example.testFloatVar)
        assertEquals(pref.getTestFloat(), 78422.214F)

        Example.testFloatVar = -0.32394F
        assertEquals(pref.getTestFloat(), Example.testFloatVar)
        assertEquals(pref.getTestFloat(), -0.32394F)

        pref.edit().remove("testFloatVar").apply()
        assertEquals(Float.MAX_VALUE, Example.testFloatVar)
    }

    public fun testBooleanPrefVar() {
        // Init model
        Example.clear()

        val pref = getContext().getSharedPreferences(Example.kotprefName, Example.kotprefMode)

        assertEquals(true, Example.testBooleanVar)

        Example.testBooleanVar = false
        assertEquals(pref.getTestBoolean(), Example.testBooleanVar)
        assertEquals(pref.getTestBoolean(), false)

        Example.testBooleanVar = true
        assertEquals(pref.getTestBoolean(), Example.testBooleanVar)
        assertEquals(pref.getTestBoolean(), true)

        pref.edit().remove("testBooleanVar").apply()
        assertEquals(true, Example.testBooleanVar)
    }

    public fun testStringPrefVar() {
        // Init model
        Example.clear()

        val pref = getContext().getSharedPreferences(Example.kotprefName, Example.kotprefMode)

        assertEquals("Default string", Example.testStringVar)

        Example.testStringVar = "Ohayo!"
        assertEquals(pref.getTestString(), Example.testStringVar)
        assertEquals(pref.getTestString(), "Ohayo!")
        assertEquals(Example.testStringVar, "Ohayo!")

        Example.testStringVar = "Oyasumi!"
        assertEquals(pref.getTestString(), Example.testStringVar)
        assertEquals(pref.getTestString(), "Oyasumi!")
        assertEquals(Example.testStringVar, "Oyasumi!")

        pref.edit().remove("testStringVar").apply()
        assertEquals("Default string", Example.testStringVar)
    }

    public fun testStringSetPrefVar() {
        // Init model
        Example.clear()

        val pref = getContext().getSharedPreferences(Example.kotprefName, Example.kotprefMode)

        assertEquals(0, Example.testStringSetVal.size())

        Example.testStringSetVal.add("test1")
        Example.testStringSetVal.add("test2")
        Example.testStringSetVal.add("test3")
        assertEquals(3, Example.testStringSetVal.size())
        assertEquals(3, pref.getTestStringSet().size())
        assertTrue(pref.getTestStringSet().contains("test1"))
        assertTrue(pref.getTestStringSet().contains("test2"))
        assertTrue(pref.getTestStringSet().contains("test3"))
        assertTrue(Example.testStringSetVal.contains("test1"))
        assertTrue(Example.testStringSetVal.contains("test2"))
        assertTrue(Example.testStringSetVal.contains("test3"))

        Example.testStringSetVal.remove("test2")
        assertEquals(2, Example.testStringSetVal.size())
        assertEquals(2, pref.getTestStringSet().size())
        assertTrue(Example.testStringSetVal.contains("test1"))
        assertFalse(Example.testStringSetVal.contains("test2"))
        assertTrue(Example.testStringSetVal.contains("test3"))
        assertTrue(pref.getTestStringSet().contains("test1"))
        assertFalse(pref.getTestStringSet().contains("test2"))
        assertTrue(pref.getTestStringSet().contains("test3"))

        val treeSet = TreeSet<String>()
        treeSet.add("test4")
        treeSet.add("test5")
        treeSet.add("test6")

        Example.testStringSetVal.addAll(treeSet)
        assertEquals(5, Example.testStringSetVal.size())
        assertEquals(5, pref.getTestStringSet().size())
        assertTrue(Example.testStringSetVal.contains("test1"))
        assertTrue(Example.testStringSetVal.contains("test3"))
        assertTrue(Example.testStringSetVal.containsAll(treeSet))
        assertTrue(pref.getTestStringSet().contains("test1"))
        assertTrue(pref.getTestStringSet().contains("test3"))
        assertTrue(pref.getTestStringSet().containsAll(treeSet))

        Example.testStringSetVal.removeAll(treeSet)
        assertEquals(2, Example.testStringSetVal.size())
        assertEquals(2, pref.getTestStringSet().size())
        assertTrue(Example.testStringSetVal.contains("test1"))
        assertTrue(Example.testStringSetVal.contains("test3"))
        assertTrue(pref.getTestStringSet().contains("test1"))
        assertTrue(pref.getTestStringSet().contains("test3"))

        Example.testStringSetVal.addAll(treeSet)
        Example.testStringSetVal.retainAll(treeSet)
        assertEquals(treeSet.size(), Example.testStringSetVal.size())
        assertEquals(treeSet.size(), pref.getTestStringSet().size())
        assertTrue(Example.testStringSetVal.containsAll(treeSet))
        assertTrue(pref.getTestStringSet().containsAll(treeSet))
    }

    public fun testLazyDefaultStringSet() {
        // Init model
        Example.clear()

        val pref = getContext().getSharedPreferences(Example.kotprefName, Example.kotprefMode)

        assertEquals(Example.testLazyDefaultSS.size(), 3)
        assertEquals(pref.getStringSet("testLazyDefaultSS", null).size(), 3)
        assertTrue(Example.testLazyDefaultSS.containsAll(pref.getStringSet("testLazyDefaultSS", null)))
    }

    public fun testPreferenceName() {
        // Init model
        Example.clear()

        val pref = getContext().getSharedPreferences("name_example", NameExample.kotprefMode)
        NameExample.testIntVar = 39
        assertEquals(pref.getInt("testIntVar", 0), NameExample.testIntVar)
    }

    public fun testBulkEdit() {
        // Init model
        Example.clear()

        val pref = getContext().getSharedPreferences(Example.kotprefName, Example.kotprefMode)

        Kotpref.bulk(Example) {
            testIntVar = 1024
            testLongVar = 422098523L
            testFloatVar = 43093.301F
            testBooleanVar = false
            testStringVar = "bulk edit"

            assertEquals(1024, testIntVar)
            assertEquals(Int.MAX_VALUE, pref.getTestInt())
            assertNotEquals(Int.MAX_VALUE, testIntVar)

            assertEquals(422098523L, testLongVar)
            assertEquals(Long.MAX_VALUE, pref.getTestLong())
            assertNotEquals(Long.MAX_VALUE, testLongVar)

            assertEquals(43093.301F, testFloatVar)
            assertEquals(Float.MAX_VALUE, pref.getTestFloat())
            assertNotEquals(Float.MAX_VALUE, testFloatVar)

            assertFalse(testBooleanVar)
            assertTrue(pref.getTestBoolean())
            assertFalse(testBooleanVar)

            assertEquals("bulk edit", testStringVar)
            assertEquals("Default string", pref.getTestString())
            assertNotEquals("Default string", testStringVar)
        }

        assertEquals(1024, Example.testIntVar)
        assertEquals(1024, pref.getTestInt())
        assertEquals(422098523L, Example.testLongVar)
        assertEquals(422098523L, pref.getTestLong())
        assertEquals(43093.301F, Example.testFloatVar)
        assertEquals(43093.301F, pref.getTestFloat())
        assertFalse(Example.testBooleanVar)
        assertFalse(pref.getTestBoolean())
        assertEquals("bulk edit", Example.testStringVar)
        assertEquals("bulk edit", pref.getTestString())
    }

    public fun testBulkEditStringSet() {
        // Init model
        Example.clear()

        val pref = getContext().getSharedPreferences(Example.kotprefName, Example.kotprefMode)

        Kotpref.bulk(Example) {
            testStringSetVal.add("test1")
            testStringSetVal.add("test2")
            testStringSetVal.add("test3")
            assertEquals(3, testStringSetVal.size())
            assertEquals(0, pref.getTestStringSet().size())
            assertFalse(pref.getTestStringSet().contains("test1"))
            assertFalse(pref.getTestStringSet().contains("test2"))
            assertFalse(pref.getTestStringSet().contains("test3"))
            assertTrue(testStringSetVal.contains("test1"))
            assertTrue(testStringSetVal.contains("test2"))
            assertTrue(testStringSetVal.contains("test3"))
        }

        assertEquals(3, Example.testStringSetVal.size())
        assertEquals(3, pref.getTestStringSet().size())
        assertTrue(pref.getTestStringSet().contains("test1"))
        assertTrue(pref.getTestStringSet().contains("test2"))
        assertTrue(pref.getTestStringSet().contains("test3"))
        assertTrue(Example.testStringSetVal.contains("test1"))
        assertTrue(Example.testStringSetVal.contains("test2"))
        assertTrue(Example.testStringSetVal.contains("test3"))

        Kotpref.bulk(Example) {
            testStringSetVal.remove("test2")

            assertEquals(2, testStringSetVal.size())
            assertEquals(3, pref.getTestStringSet().size())
            assertTrue(testStringSetVal.contains("test1"))
            assertFalse(testStringSetVal.contains("test2"))
            assertTrue(testStringSetVal.contains("test3"))
            assertTrue(pref.getTestStringSet().contains("test1"))
            assertTrue(pref.getTestStringSet().contains("test2"))
            assertTrue(pref.getTestStringSet().contains("test3"))
        }

        assertEquals(2, Example.testStringSetVal.size())
        assertEquals(2, pref.getTestStringSet().size())
        assertTrue(Example.testStringSetVal.contains("test1"))
        assertFalse(Example.testStringSetVal.contains("test2"))
        assertTrue(Example.testStringSetVal.contains("test3"))
        assertTrue(pref.getTestStringSet().contains("test1"))
        assertFalse(pref.getTestStringSet().contains("test2"))
        assertTrue(pref.getTestStringSet().contains("test3"))


        val treeSet = TreeSet<String>()
        treeSet.add("test4")
        treeSet.add("test5")
        treeSet.add("test6")

        Kotpref.bulk(Example) {
            testStringSetVal.addAll(treeSet)

            assertEquals(5, Example.testStringSetVal.size())
            assertEquals(2, pref.getTestStringSet().size())
            assertTrue(testStringSetVal.contains("test1"))
            assertTrue(testStringSetVal.contains("test3"))
            assertTrue(testStringSetVal.containsAll(treeSet))
            assertTrue(pref.getTestStringSet().contains("test1"))
            assertTrue(pref.getTestStringSet().contains("test3"))
            assertFalse(pref.getTestStringSet().containsAll(treeSet))
        }

        assertEquals(5, Example.testStringSetVal.size())
        assertEquals(5, pref.getTestStringSet().size())
        assertTrue(Example.testStringSetVal.contains("test1"))
        assertTrue(Example.testStringSetVal.contains("test3"))
        assertTrue(Example.testStringSetVal.containsAll(treeSet))
        assertTrue(pref.getTestStringSet().contains("test1"))
        assertTrue(pref.getTestStringSet().contains("test3"))
        assertTrue(pref.getTestStringSet().containsAll(treeSet))


        Kotpref.bulk(Example) {
            testStringSetVal.removeAll(treeSet)

            assertEquals(2, Example.testStringSetVal.size())
            assertEquals(5, pref.getTestStringSet().size())
            assertTrue(testStringSetVal.contains("test1"))
            assertTrue(testStringSetVal.contains("test3"))
            assertTrue(pref.getTestStringSet().contains("test1"))
            assertTrue(pref.getTestStringSet().contains("test3"))
            assertTrue(pref.getTestStringSet().containsAll(treeSet))
        }

        assertEquals(2, Example.testStringSetVal.size())
        assertEquals(2, pref.getTestStringSet().size())
        assertTrue(Example.testStringSetVal.contains("test1"))
        assertTrue(Example.testStringSetVal.contains("test3"))
        assertTrue(pref.getTestStringSet().contains("test1"))
        assertTrue(pref.getTestStringSet().contains("test3"))


        Kotpref.bulk(Example) {
            testStringSetVal.addAll(treeSet)
            testStringSetVal.retainAll(treeSet)

            assertEquals(3, testStringSetVal.size())
            assertEquals(2, pref.getTestStringSet().size())
            assertTrue(testStringSetVal.containsAll(treeSet))
        }

        assertEquals(3, Example.testStringSetVal.size())
        assertEquals(3, pref.getTestStringSet().size())
        assertTrue(Example.testStringSetVal.containsAll(treeSet))
        assertTrue(pref.getTestStringSet().containsAll(treeSet))
    }
}