package com.chibatching.kotpref.pref

import android.content.Context
import android.content.SharedPreferences
import com.chibatching.kotpref.CustomExample
import com.chibatching.kotpref.Example
import com.chibatching.kotpref.Kotpref
import org.junit.After
import org.junit.Before
import org.robolectric.RuntimeEnvironment


abstract class BasePrefTest(private val commitAllProperties: Boolean) {

    lateinit var example: Example
    lateinit var customExample: CustomExample
    lateinit var context: Context
    lateinit var examplePref: SharedPreferences
    lateinit var customPref: SharedPreferences

    @Before
    fun setUp() {
        context = RuntimeEnvironment.application
        Kotpref.init(context)
        example = Example(commitAllProperties)
        customExample = CustomExample(commitAllProperties)

        examplePref = example.preferences
        examplePref.edit().clear().commit()

        customPref = customExample.preferences
        customPref.edit().clear().commit()
    }

    @After
    fun tearDown() {
        example.clear()
    }
}
