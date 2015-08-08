package com.chibatching.kotpref

import android.test.AndroidTestCase
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFalse
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
        val testStringSetVar: MutableSet<String> by stringSetPrefVal(TreeSet<String>())
        val testLazyDefaultSS: MutableSet<String> by stringSetPrefVal{
            val defSet = LinkedHashSet<String>()
            defSet.add("Lazy set item 1")
            defSet.add("lazy set item 2")
            defSet.add("lazy set item 3")
            defSet
        }
    }

    object NameExample : KotprefModel() {
        override val kotprefName: String = "name_example"
        var testIntVar: Int by intPrefVar(Int.MAX_VALUE)
    }

    override fun setUp() {
        Kotpref.init(getContext())
        getContext().getSharedPreferences(Example.kotprefName, Example.kotprefMode).edit().clear().apply()
        getContext().getSharedPreferences(NameExample.kotprefName, NameExample.kotprefMode).edit().clear().apply()
    }

    public fun testIntPrefVar() {
        // Test preference
        val pref = getContext().getSharedPreferences(Example.kotprefName, Example.kotprefMode)

        assertEquals(Int.MAX_VALUE, Example.testIntVar)

        Example.testIntVar = -922
        assertEquals(pref.getInt("testIntVar", 0), Example.testIntVar)
        assertEquals(pref.getInt("testIntVar", 0), -922)

        Example.testIntVar = 4320
        assertEquals(pref.getInt("testIntVar", 0), Example.testIntVar)
        assertEquals(pref.getInt("testIntVar", 0), 4320)

        Example.clear()
        assertEquals(Int.MAX_VALUE, Example.testIntVar)
    }

    public fun testLongPrefVar() {
        // Test preference
        val pref = getContext().getSharedPreferences(Example.kotprefName, Example.kotprefMode)

        assertEquals(Long.MAX_VALUE, Example.testLongVar)

        Example.testLongVar = 83109402L
        assertEquals(pref.getLong("testLongVar", 0), Example.testLongVar)
        assertEquals(pref.getLong("testLongVar", 0), 83109402L)

        Example.testLongVar = -43824L
        assertEquals(pref.getLong("testLongVar", 0), Example.testLongVar)
        assertEquals(pref.getLong("testLongVar", 0), -43824L)

        Example.clear()
        assertEquals(Long.MAX_VALUE, Example.testLongVar)
    }

    public fun testFloatPrefVar() {
        // Test preference
        val pref = getContext().getSharedPreferences(Example.kotprefName, Example.kotprefMode)

        assertEquals(Float.MAX_VALUE, Example.testFloatVar)

        Example.testFloatVar = 78422.214F
        assertEquals(pref.getFloat("testFloatVar", 0F), Example.testFloatVar)
        assertEquals(pref.getFloat("testFloatVar", 0F), 78422.214F)

        Example.testFloatVar = -0.32394F
        assertEquals(pref.getFloat("testFloatVar", 0F), Example.testFloatVar)
        assertEquals(pref.getFloat("testFloatVar", 0F), -0.32394F)

        pref.edit().remove("testFloatVar").apply()
        assertEquals(Float.MAX_VALUE, Example.testFloatVar)
    }

    public fun testBooleanPrefVar() {
        // Test preference
        val pref = getContext().getSharedPreferences(Example.kotprefName, Example.kotprefMode)

        assertEquals(true, Example.testBooleanVar)

        Example.testBooleanVar = false
        assertEquals(pref.getBoolean("testBooleanVar", false), Example.testBooleanVar)
        assertEquals(pref.getBoolean("testBooleanVar", false), false)

        Example.testBooleanVar = true
        assertEquals(pref.getBoolean("testBooleanVar", false), Example.testBooleanVar)
        assertEquals(pref.getBoolean("testBooleanVar", false), true)

        pref.edit().remove("testBooleanVar").apply()
        assertEquals(true, Example.testBooleanVar)
    }

    public fun testStringPrefVar() {
        // Test preference
        val pref = getContext().getSharedPreferences(Example.kotprefName, Example.kotprefMode)

        assertEquals("Default string", Example.testStringVar)

        Example.testStringVar = "Ohayo!"
        assertEquals(pref.getString("testStringVar", ""), Example.testStringVar)
        assertEquals(pref.getString("testStringVar", ""), "Ohayo!")
        assertEquals(Example.testStringVar, "Ohayo!")

        Example.testStringVar = "Oyasumi!"
        assertEquals(pref.getString("testStringVar", ""), Example.testStringVar)
        assertEquals(pref.getString("testStringVar", ""), "Oyasumi!")
        assertEquals(Example.testStringVar, "Oyasumi!")

        pref.edit().remove("testStringVar").apply()
        assertEquals("Default string", Example.testStringVar)
    }

    public fun testStringSetPrefVar() {
        // Test default preference
        val pref = getContext().getSharedPreferences(Example.kotprefName, Example.kotprefMode)

        assertEquals(0, Example.testStringSetVar.size())

        Example.testStringSetVar.add("test1")
        Example.testStringSetVar.add("test2")
        Example.testStringSetVar.add("test3")
        assertEquals(3, Example.testStringSetVar.size())
        assertEquals(3, pref.getStringSet("testStringSetVar", null).size())
        assertTrue(pref.getStringSet("testStringSetVar", null).contains("test1"))
        assertTrue(pref.getStringSet("testStringSetVar", null).contains("test2"))
        assertTrue(pref.getStringSet("testStringSetVar", null).contains("test3"))
        assertTrue(Example.testStringSetVar.contains("test1"))
        assertTrue(Example.testStringSetVar.contains("test2"))
        assertTrue(Example.testStringSetVar.contains("test3"))

        Example.testStringSetVar.remove("test2")
        assertEquals(2, Example.testStringSetVar.size())
        assertEquals(2, pref.getStringSet("testStringSetVar", null).size())
        assertTrue(Example.testStringSetVar.contains("test1"))
        assertFalse(Example.testStringSetVar.contains("test2"))
        assertTrue(Example.testStringSetVar.contains("test3"))
        assertTrue(pref.getStringSet("testStringSetVar", null).contains("test1"))
        assertFalse(pref.getStringSet("testStringSetVar", null).contains("test2"))
        assertTrue(pref.getStringSet("testStringSetVar", null).contains("test3"))

        val treeSet = TreeSet<String>()
        treeSet.add("test4")
        treeSet.add("test5")
        treeSet.add("test6")

        Example.testStringSetVar.addAll(treeSet)
        assertEquals(5, Example.testStringSetVar.size())
        assertEquals(5, pref.getStringSet("testStringSetVar", null).size())
        assertTrue(Example.testStringSetVar.contains("test1"))
        assertTrue(Example.testStringSetVar.contains("test3"))
        assertTrue(Example.testStringSetVar.containsAll(treeSet))
        assertTrue(pref.getStringSet("testStringSetVar", null).contains("test1"))
        assertTrue(pref.getStringSet("testStringSetVar", null).contains("test3"))
        assertTrue(pref.getStringSet("testStringSetVar", null).containsAll(treeSet))

        Example.testStringSetVar.removeAll(treeSet)
        assertEquals(2, Example.testStringSetVar.size())
        assertEquals(2, pref.getStringSet("testStringSetVar", null).size())
        assertTrue(Example.testStringSetVar.contains("test1"))
        assertTrue(Example.testStringSetVar.contains("test3"))
        assertTrue(pref.getStringSet("testStringSetVar", null).contains("test1"))
        assertTrue(pref.getStringSet("testStringSetVar", null).contains("test3"))

        Example.testStringSetVar.addAll(treeSet)
        Example.testStringSetVar.retainAll(treeSet)
        assertEquals(treeSet.size(), Example.testStringSetVar.size())
        assertEquals(treeSet.size(), pref.getStringSet("testStringSetVar", null).size())
        assertTrue(Example.testStringSetVar.containsAll(treeSet))
        assertTrue(pref.getStringSet("testStringSetVar", null).containsAll(treeSet))
    }

    public fun testLazyDefaultStringSet() {
        val pref = getContext().getSharedPreferences(Example.kotprefName, Example.kotprefMode)

        assertEquals(Example.testLazyDefaultSS.size(), 3)
        assertEquals(pref.getStringSet("testLazyDefaultSS", null).size(), 3)
        assertTrue(Example.testLazyDefaultSS.containsAll(pref.getStringSet("testLazyDefaultSS", null)))
    }

    public fun testPreferenceName() {
        val pref = getContext().getSharedPreferences("name_example", NameExample.kotprefMode)
        NameExample.testIntVar = 39
        assertEquals(pref.getInt("testIntVar", 0), NameExample.testIntVar)
    }
}