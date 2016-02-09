package com.chibatching.kotpref

import android.content.SharedPreferences
import android.test.AndroidTestCase


fun assertPreferenceEquals(preferences: SharedPreferences, key: String, expected: Int, actual: Int) {
    val prefValue = preferences.getInt(key, 0)
    AndroidTestCase.assertEquals("Preference value is not same with expected value.", expected, prefValue)
    AndroidTestCase.assertEquals("Actual value is not same with expected value.", expected, actual)
}

fun assertPreferenceEquals(preferences: SharedPreferences, key: String, expected: Long, actual: Long) {
    val prefValue = preferences.getLong(key, 0)
    AndroidTestCase.assertEquals("Preference value is not same with expected value.", expected, prefValue)
    AndroidTestCase.assertEquals("Actual value is not same with expected value.", expected, actual)
}

fun assertPreferenceEquals(preferences: SharedPreferences, key: String, expected: Float, actual: Float) {
    val prefValue = preferences.getFloat(key, 0F)
    AndroidTestCase.assertEquals("Preference value is not same with expected value.", expected, prefValue)
    AndroidTestCase.assertEquals("Actual value is not same with expected value.", expected, actual)
}

fun assertPreferenceEquals(preferences: SharedPreferences, key: String, expected: Boolean, actual: Boolean) {
    val prefValue = preferences.getBoolean(key, false)
    AndroidTestCase.assertEquals("Preference value is not same with expected value.", expected, prefValue)
    AndroidTestCase.assertEquals("Actual value is not same with expected value.", expected, actual)
}

fun assertPreferenceEquals(preferences: SharedPreferences, key: String, expected: String?, actual: String?) {
    val prefValue = preferences.getString(key, null)
    AndroidTestCase.assertEquals("Preference value is not same with expected value.", expected, prefValue)
    AndroidTestCase.assertEquals("Actual value is not same with expected value.", expected, actual)
}

fun assertPreferenceEquals(preferences: SharedPreferences, key: String, expected: Set<String>, actual: Set<String>) {
    val prefValue = preferences.getStringSet(key, null)
    AndroidTestCase.assertTrue("Expected size of set are not same with actual.", expected.size == actual.size)
    AndroidTestCase.assertTrue("Expected set are not same with actual.", expected.containsAll(actual))
    AndroidTestCase.assertTrue("Preference size of set are not same with expected.", expected.size == prefValue.size)
    AndroidTestCase.assertTrue("Preference set are not same with actual.", expected.containsAll(prefValue))
}

fun assertPreferenceInTransaction(preferences: SharedPreferences, key: String,
                                  prefExpected: Int, tranExpected: Int, tranActual: Int) {
    val prefValue = preferences.getInt(key, 0)
    AndroidTestCase.assertEquals("Preference value is not same with expected value.", prefExpected, prefValue)
    AndroidTestCase.assertEquals("Actual value is not same with expected value.", tranExpected, tranActual)
}

fun assertPreferenceInTransaction(preferences: SharedPreferences, key: String,
                                  prefExpected: Long, tranExpected: Long, tranActual: Long) {
    val prefValue = preferences.getLong(key, 0L)
    AndroidTestCase.assertEquals("Preference value is not same with expected value.", prefExpected, prefValue)
    AndroidTestCase.assertEquals("Actual value is not same with expected value.", tranExpected, tranActual)
}

fun assertPreferenceInTransaction(preferences: SharedPreferences, key: String,
                                  prefExpected: Float, tranExpected: Float, tranActual: Float) {
    val prefValue = preferences.getFloat(key, 0F)
    AndroidTestCase.assertEquals("Preference value is not same with expected value.", prefExpected, prefValue)
    AndroidTestCase.assertEquals("Actual value is not same with expected value.", tranExpected, tranActual)
}

fun assertPreferenceInTransaction(preferences: SharedPreferences, key: String,
                                  prefExpected: Boolean, tranExpected: Boolean, tranActual: Boolean) {
    val prefValue = preferences.getBoolean(key, false)
    AndroidTestCase.assertEquals("Preference value is not same with expected value.", prefExpected, prefValue)
    AndroidTestCase.assertEquals("Actual value is not same with expected value.", tranExpected, tranActual)
}

fun assertPreferenceInTransaction(preferences: SharedPreferences, key: String,
                                  prefExpected: String, tranExpected: String, tranActual: String) {
    val prefValue = preferences.getString(key, "")
    AndroidTestCase.assertEquals("Preference value is not same with expected value.", prefExpected, prefValue)
    AndroidTestCase.assertEquals("Actual value is not same with expected value.", tranExpected, tranActual)
}

fun assertPreferenceInTransaction(preferences: SharedPreferences, key: String,
                                  prefExpected: Set<String>, tranExpected: Set<String>, tranActual: Set<String>) {
    val prefValue = preferences.getStringSet(key, null)
    AndroidTestCase.assertTrue("Expected size of set are not same with actual.", tranExpected.size == tranActual.size)
    AndroidTestCase.assertTrue("Expected set are not same with actual.", tranExpected.containsAll(tranActual))
    AndroidTestCase.assertTrue("Preference size of set are not same with expected.", prefExpected.size == prefValue.size)
    AndroidTestCase.assertTrue("Preference set are not same with actual.", prefExpected.containsAll(prefValue))
}