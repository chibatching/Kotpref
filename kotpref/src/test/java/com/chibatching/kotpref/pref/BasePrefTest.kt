package com.chibatching.kotpref.pref

import android.content.Context
import android.content.SharedPreferences
import androidx.test.core.app.ApplicationProvider
import com.chibatching.kotpref.CustomExample
import com.chibatching.kotpref.Example
import org.junit.After
import org.junit.Before

internal abstract class BasePrefTest(private val commitAllProperties: Boolean) {

    lateinit var example: Example
    lateinit var customExample: CustomExample
    lateinit var context: Context
    lateinit var examplePref: SharedPreferences
    lateinit var customPref: SharedPreferences

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        example = Example(commitAllProperties, context)
        customExample = CustomExample(commitAllProperties, context)

        examplePref = example.preferences
        examplePref.edit().clear().commit()

        customPref = customExample.preferences
        customPref.edit().clear().commit()
    }

    @After
    fun tearDown() {
        example.clear()
        customExample.clear()
    }
}
