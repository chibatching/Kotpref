package com.chibatching.kotpref

import android.content.Context
import android.preference.PreferenceManager
import android.test.AndroidTestCase
import java.util.TreeSet
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * [Testing Fundamentals](http://d.android.com/tools/testing/testing_android.html)
 */
public class KotprefTest : AndroidTestCase() {

    var testIntVarDefault: Int by Kotpref.intPrefVar({ getContext() }, default = Int.MIN_VALUE)
    var testIntVar: Int by Kotpref.intPrefVar({ getContext() }, default = Int.MAX_VALUE, preferenceName = PREFERENCE_NAME)

    var testLongVarDefault: Long by Kotpref.longPrefVar({ getContext() }, default = Long.MIN_VALUE)
    var testLongVar: Long by Kotpref.longPrefVar({ getContext() }, default = Long.MAX_VALUE, preferenceName = PREFERENCE_NAME)

    var testFloatVarDefault: Float by Kotpref.floatPrefVar({ getContext() }, default = Float.MIN_VALUE)
    var testFloatVar: Float by Kotpref.floatPrefVar({ getContext() }, default = Float.MAX_VALUE, preferenceName = PREFERENCE_NAME)

    var testBooleanVarDefault: Boolean by Kotpref.booleanPrefVar({ getContext() }, default = false)
    var testBooleanVar: Boolean by Kotpref.booleanPrefVar({ getContext() }, default = true, preferenceName = PREFERENCE_NAME)

    var testStringVarDefault: String by Kotpref.stringPrefVar({ getContext() }, default = "Default string")
    var testStringVar: String by Kotpref.stringPrefVar({ getContext() }, default = "Default 2nd string", preferenceName = PREFERENCE_NAME)

    var testStringSetVar: MutableSet<String> by Kotpref.stringSetPrefVar({ getContext() }, default = TreeSet<String>())


    companion object {
        val PREFERENCE_NAME = "test_preference"
    }

    override fun setUp() {
        PreferenceManager.getDefaultSharedPreferences(getContext()).edit().clear().commit()
        getContext().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit().clear().commit()
    }

    public fun testIntPrefVar() {
        // Test default preference
        val defPref = PreferenceManager.getDefaultSharedPreferences(getContext())

        assertEquals(Int.MIN_VALUE, testIntVarDefault)

        testIntVarDefault = 98
        assertEquals(defPref.getInt("testIntVarDefault", 0), testIntVarDefault)
        assertEquals(defPref.getInt("testIntVarDefault", 0), 98)

        testIntVarDefault = -78923
        assertEquals(defPref.getInt("testIntVarDefault", 0), testIntVarDefault)
        assertEquals(defPref.getInt("testIntVarDefault", 0), -78923)

        defPref.edit().remove("testIntVarDefault").apply()
        assertEquals(Int.MIN_VALUE, testIntVarDefault)

        // Test preference
        val pref = getContext().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

        assertEquals(Int.MAX_VALUE, testIntVar)

        testIntVar = -922
        assertEquals(pref.getInt("testIntVar", 0), testIntVar)
        assertEquals(pref.getInt("testIntVar", 0), -922)

        testIntVar = 4320
        assertEquals(pref.getInt("testIntVar", 0), testIntVar)
        assertEquals(pref.getInt("testIntVar", 0), 4320)

        pref.edit().remove("testIntVar").apply()
        assertEquals(Int.MAX_VALUE, testIntVar)
    }

    public fun testLongPrefVar() {
        // Test default preference
        val defPref = PreferenceManager.getDefaultSharedPreferences(getContext())

        assertEquals(Long.MIN_VALUE, testLongVarDefault)

        testLongVarDefault = -193L
        assertEquals(defPref.getLong("testLongVarDefault", 0), testLongVarDefault)
        assertEquals(defPref.getLong("testLongVarDefault", 0), -193L)

        testLongVarDefault = 9852466L
        assertEquals(defPref.getLong("testLongVarDefault", 0), testLongVarDefault)
        assertEquals(defPref.getLong("testLongVarDefault", 0), 9852466L)

        defPref.edit().remove("testLongVarDefault").apply()
        assertEquals(Long.MIN_VALUE, testLongVarDefault)

        // Test preference
        val pref = getContext().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

        assertEquals(Long.MAX_VALUE, testLongVar)

        testLongVar = 83109402L
        assertEquals(pref.getLong("testLongVar", 0), testLongVar)
        assertEquals(pref.getLong("testLongVar", 0), 83109402L)

        testLongVar = -43824L
        assertEquals(pref.getLong("testLongVar", 0), testLongVar)
        assertEquals(pref.getLong("testLongVar", 0), -43824L)

        pref.edit().remove("testLongVar").apply()
        assertEquals(Long.MAX_VALUE, testLongVar)
    }

    public fun testFloatPrefVar() {
        // Test default preference
        val defPref = PreferenceManager.getDefaultSharedPreferences(getContext())

        assertEquals(Float.MIN_VALUE, testFloatVarDefault)

        testFloatVarDefault = -984.14F
        assertEquals(defPref.getFloat("testFloatVarDefault", 0F), testFloatVarDefault)
        assertEquals(defPref.getFloat("testFloatVarDefault", 0F), -984.14F)

        testFloatVarDefault = 32.98526F
        assertEquals(defPref.getFloat("testFloatVarDefault", 0F), testFloatVarDefault)
        assertEquals(defPref.getFloat("testFloatVarDefault", 0F), 32.98526F)

        defPref.edit().remove("testFloatVarDefault").apply()
        assertEquals(Float.MIN_VALUE, testFloatVarDefault)

        // Test preference
        val pref = getContext().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

        assertEquals(Float.MAX_VALUE, testFloatVar)

        testFloatVar = 78422.214F
        assertEquals(pref.getFloat("testFloatVar", 0F), testFloatVar)
        assertEquals(pref.getFloat("testFloatVar", 0F), 78422.214F)

        testFloatVar = -0.32394F
        assertEquals(pref.getFloat("testFloatVar", 0F), testFloatVar)
        assertEquals(pref.getFloat("testFloatVar", 0F), -0.32394F)

        pref.edit().remove("testFloatVar").apply()
        assertEquals(Float.MAX_VALUE, testFloatVar)
    }

    public fun testBooleanPrefVar() {
        // Test default preference
        val defPref = PreferenceManager.getDefaultSharedPreferences(getContext())

        assertEquals(false, testBooleanVarDefault)

        testBooleanVarDefault = true
        assertEquals(defPref.getBoolean("testBooleanVarDefault", false), testBooleanVarDefault)
        assertEquals(defPref.getBoolean("testBooleanVarDefault", false), true)

        testBooleanVarDefault = false
        assertEquals(defPref.getBoolean("testBooleanVarDefault", false), testBooleanVarDefault)
        assertEquals(defPref.getBoolean("testBooleanVarDefault", false), false)

        defPref.edit().remove("testBooleanVarDefault").apply()
        assertEquals(false, testBooleanVarDefault)

        // Test preference
        val pref = getContext().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

        assertEquals(true, testBooleanVar)

        testBooleanVar = false
        assertEquals(pref.getBoolean("testBooleanVar", false), testBooleanVar)
        assertEquals(pref.getBoolean("testBooleanVar", false), false)

        testBooleanVar = true
        assertEquals(pref.getBoolean("testBooleanVar", false), testBooleanVar)
        assertEquals(pref.getBoolean("testBooleanVar", false), true)

        pref.edit().remove("testBooleanVar").apply()
        assertEquals(true, testBooleanVar)
    }

    public fun testStringPrefVar() {
        // Test default preference
        val defPref = PreferenceManager.getDefaultSharedPreferences(getContext())

        assertEquals("Default string", testStringVarDefault)

        testStringVarDefault = "Good morning!"
        assertEquals(defPref.getString("testStringVarDefault", ""), testStringVarDefault)
        assertEquals(defPref.getString("testStringVarDefault", ""), "Good morning!")
        assertEquals(testStringVarDefault, "Good morning!")

        testStringVarDefault = "Good evening!"
        assertEquals(defPref.getString("testStringVarDefault", ""), testStringVarDefault)
        assertEquals(defPref.getString("testStringVarDefault", ""), "Good evening!")
        assertEquals(testStringVarDefault, "Good evening!")

        defPref.edit().remove("testStringVarDefault").apply()
        assertEquals("Default string", testStringVarDefault)

        // Test preference
        val pref = getContext().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

        assertEquals("Default 2nd string", testStringVar)

        testStringVar = "Ohayo!"
        assertEquals(pref.getString("testStringVar", ""), testStringVar)
        assertEquals(pref.getString("testStringVar", ""), "Ohayo!")
        assertEquals(testStringVar, "Ohayo!")

        testStringVar = "Oyasumi!"
        assertEquals(pref.getString("testStringVar", ""), testStringVar)
        assertEquals(pref.getString("testStringVar", ""), "Oyasumi!")
        assertEquals(testStringVar, "Oyasumi!")

        pref.edit().remove("testStringVar").apply()
        assertEquals("Default 2nd string", testStringVar)
    }

    public fun testStringSetPrefVar() {
        // Test default preference
        val defPref = PreferenceManager.getDefaultSharedPreferences(getContext())

        assertEquals(0, testStringSetVar.size())

        testStringSetVar.add("test1")
        testStringSetVar.add("test2")
        testStringSetVar.add("test3")
        assertEquals(3, testStringSetVar.size())
        assertEquals(3, defPref.getStringSet("testStringSetVar", null).size())
        assertTrue(defPref.getStringSet("testStringSetVar", null).contains("test1"))
        assertTrue(defPref.getStringSet("testStringSetVar", null).contains("test2"))
        assertTrue(defPref.getStringSet("testStringSetVar", null).contains("test3"))
        assertTrue(testStringSetVar.contains("test1"))
        assertTrue(testStringSetVar.contains("test2"))
        assertTrue(testStringSetVar.contains("test3"))

        testStringSetVar.remove("test2")
        assertEquals(2, testStringSetVar.size())
        assertEquals(2, defPref.getStringSet("testStringSetVar", null).size())
        assertTrue(testStringSetVar.contains("test1"))
        assertFalse(testStringSetVar.contains("test2"))
        assertTrue(testStringSetVar.contains("test3"))
        assertTrue(defPref.getStringSet("testStringSetVar", null).contains("test1"))
        assertFalse(defPref.getStringSet("testStringSetVar", null).contains("test2"))
        assertTrue(defPref.getStringSet("testStringSetVar", null).contains("test3"))

        val treeSet = TreeSet<String>()
        treeSet.add("test4")
        treeSet.add("test5")
        treeSet.add("test6")

        testStringSetVar.addAll(treeSet)
        assertEquals(5, testStringSetVar.size())
        assertEquals(5, defPref.getStringSet("testStringSetVar", null).size())
        assertTrue(testStringSetVar.contains("test1"))
        assertTrue(testStringSetVar.contains("test3"))
        assertTrue(testStringSetVar.containsAll(treeSet))
        assertTrue(defPref.getStringSet("testStringSetVar", null).contains("test1"))
        assertTrue(defPref.getStringSet("testStringSetVar", null).contains("test3"))
        assertTrue(defPref.getStringSet("testStringSetVar", null).containsAll(treeSet))

        testStringSetVar.removeAll(treeSet)
        assertEquals(2, testStringSetVar.size())
        assertEquals(2, defPref.getStringSet("testStringSetVar", null).size())
        assertTrue(testStringSetVar.contains("test1"))
        assertTrue(testStringSetVar.contains("test3"))
        assertTrue(defPref.getStringSet("testStringSetVar", null).contains("test1"))
        assertTrue(defPref.getStringSet("testStringSetVar", null).contains("test3"))

        testStringSetVar.addAll(treeSet)
        testStringSetVar.retainAll(treeSet)
        assertEquals(treeSet.size(), testStringSetVar.size())
        assertEquals(treeSet.size(), defPref.getStringSet("testStringSetVar", null).size())
        assertTrue(testStringSetVar.containsAll(treeSet))
        assertTrue(defPref.getStringSet("testStringSetVar", null).containsAll(treeSet))
    }
}