package com.chibatching.kotpref.pref

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import com.chibatching.kotpref.R
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner
import java.util.Arrays
import java.util.TreeSet

@RunWith(ParameterizedRobolectricTestRunner::class)
internal class StringSetPrefTest(commitAllProperties: Boolean) : BasePrefTest(commitAllProperties) {
    companion object {
        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters(name = "commitAllProperties = {0}")
        fun data(): Collection<Array<out Any>> {
            return Arrays.asList(arrayOf(false), arrayOf(true))
        }
    }

    @Test
    fun stringSetPrefDefaultSizeIs0() {
        assertThat(example.testStringSet).hasSize(0)
    }

    @Test
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    fun addAndRemoveStringSetPrefItemsCausePreferenceUpdate() {
        example.testStringSet.apply {
            add("test1")
            add("test2")
            add("test3")
            remove("test2")
            add("test4")
        }

        assertThat(example.testStringSet)
            .containsExactly("test1", "test3", "test4")
        assertThat(example.testStringSet)
            .containsExactlyElementsIn(examplePref.getStringSet("testStringSet", null))
    }

    @Test
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    fun addAllToStringSetPrefCausePreferenceUpdate() {
        val addSet = TreeSet<String>().apply {
            add("test1")
            add("test2")
            add("test3")
        }
        example.testStringSet.apply {
            add("test4")
            addAll(addSet)
        }

        assertThat(example.testStringSet)
            .containsExactly("test1", "test2", "test3", "test4")
        assertThat(example.testStringSet)
            .containsExactlyElementsIn(examplePref.getStringSet("testStringSet", null))
    }

    @Test
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    fun removeAllFromStringSetPrefCausePreferenceUpdate() {
        val removeSet = TreeSet<String>().apply {
            add("test2")
            add("test4")
        }
        example.testStringSet.apply {
            add("test1")
            add("test2")
            add("test3")
            add("test4")
            removeAll(removeSet)
        }

        assertThat(example.testStringSet)
            .containsExactly("test1", "test3")
        assertThat(example.testStringSet)
            .containsExactlyElementsIn(examplePref.getStringSet("testStringSet", null))
    }

    @Test
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    fun retainAllToStringSetPrefCausePreferenceUpdate() {
        val retainSet = TreeSet<String>().apply {
            add("test2")
            add("test4")
            add("test5")
        }
        example.testStringSet.apply {
            add("test1")
            add("test2")
            add("test3")
            add("test4")
            retainAll(retainSet)
        }

        assertThat(example.testStringSet)
            .containsExactly("test2", "test4")
        assertThat(example.testStringSet)
            .containsExactlyElementsIn(examplePref.getStringSet("testStringSet", null))
    }

    @Test
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    fun lazyDefaultStringSetDefaultIsSetOfDefinedInLazyBlock() {
        assertThat(customExample.testStringSet)
            .containsExactly("Lazy set item 1", "Lazy set item 2", "Lazy set item 3")
    }

    @Test
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    fun useCustomPreferenceKey() {
        customExample.testStringSet.add("Additional item")
        assertThat(customExample.testStringSet)
            .containsExactly(
                "Lazy set item 1",
                "Lazy set item 2",
                "Lazy set item 3",
                "Additional item"
            )
        assertThat(customExample.testStringSet)
            .containsExactlyElementsIn(
                customPref.getStringSet(
                    context.getString(R.string.test_custom_string_set),
                    null
                )
            )
        assertThat(customExample.testStringSet)
            .hasSize(
                customPref.getStringSet(
                    context.getString(R.string.test_custom_string_set),
                    null
                )!!.size
            )
    }

    @Test
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    fun removeItemViaIterator() {
        example.testStringSet.apply {
            add("test1")
            add("test2")
            add("test3")
        }

        val iterator = example.testStringSet.iterator()
        iterator.next()
        iterator.remove()
        iterator.next()
        iterator.remove()

        assertThat(example.testStringSet)
            .hasSize(1)
        assertThat(example.testStringSet)
            .containsExactlyElementsIn(examplePref.getStringSet("testStringSet", null))
    }

    @Test
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    fun readFromOtherPreferenceInstance() {
        val otherPreference =
            context.getSharedPreferences(example.kotprefName, Context.MODE_PRIVATE)

        example.testStringSet.apply {
            add("test")
        }

        assertThat(otherPreference.getStringSet("testStringSet", null))
            .containsExactly("test")
    }

    @Test
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    fun writeFromOtherPreferenceInstance() {
        val otherPreference =
            context.getSharedPreferences(example.kotprefName, Context.MODE_PRIVATE)

        example.testStringSet

        val set = otherPreference.getStringSet("testStringSet", mutableSetOf())!!
        set.add("test")
        otherPreference.edit().putStringSet("testStringSet", set).commit()

        assertThat(example.testStringSet).containsExactly("test")
    }
}
