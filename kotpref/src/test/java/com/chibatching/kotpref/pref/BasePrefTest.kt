package com.chibatching.kotpref.pref

import android.content.Context
import android.content.SharedPreferences
import com.chibatching.kotpref.CustomExample
import com.chibatching.kotpref.Example
import com.chibatching.kotpref.Kotpref
import org.junit.After
import org.junit.Before
import org.robolectric.RuntimeEnvironment


abstract class BasePrefTest {

    lateinit var example: Example
    lateinit var customExample: CustomExample
    lateinit var context: Context
    lateinit var examplePref: SharedPreferences
    lateinit var customPref: SharedPreferences

    @Before
    fun setUp() {
        context = RuntimeEnvironment.application
        Kotpref.init(context)
        example = Example()
        customExample = CustomExample()

        examplePref = context.getSharedPreferences(example.javaClass.simpleName, Context.MODE_PRIVATE)
        examplePref.edit().clear().commit()

        customPref = context.getSharedPreferences(CustomExample.PREFERENCE_NAME, Context.MODE_PRIVATE)
        customPref.edit().clear().commit()
    }

    @After
    fun tearDown() {
        example.clear()
    }
}
