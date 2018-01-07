package com.chibatching.kotpref.pref

import android.annotation.TargetApi
import android.os.Build
import com.chibatching.kotpref.R
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner
import java.util.*


@RunWith(ParameterizedRobolectricTestRunner::class)
class StringSetPrefTest(commitAllProperties: Boolean) : BasePrefTest(commitAllProperties) {
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

        assertThat(example.testStringSet).containsExactlyInAnyOrder("test1", "test3", "test4")
        assertThat(examplePref.getStringSet("testStringSet", null)).containsExactlyInAnyOrder("test1", "test3", "test4")
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

        assertThat(example.testStringSet).containsExactlyInAnyOrder("test1", "test2", "test3", "test4")
        assertThat(examplePref.getStringSet("testStringSet", null)).containsExactlyInAnyOrder("test1", "test2", "test3", "test4")
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

        assertThat(example.testStringSet).containsExactlyInAnyOrder("test1", "test3")
        assertThat(examplePref.getStringSet("testStringSet", null)).containsExactlyInAnyOrder("test1", "test3")
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

        assertThat(example.testStringSet).containsExactlyInAnyOrder("test2", "test4")
        assertThat(examplePref.getStringSet("testStringSet", null)).containsExactlyInAnyOrder("test2", "test4")
    }

    @Test
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    fun lazyDefaultStringSetDefaultIsSetOfDefinedInLazyBlock() {
        assertThat(customExample.testStringSet).containsExactlyInAnyOrder("Lazy set item 1", "Lazy set item 2", "Lazy set item 3")
    }

    @Test
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    fun useCustomPreferenceKey() {
        customExample.testStringSet.add("Additional item")
        assertThat(customExample.testStringSet).containsExactlyInAnyOrder("Lazy set item 1", "Lazy set item 2", "Lazy set item 3", "Additional item")
        assertThat(customExample.testStringSet).containsAll(customPref.getStringSet(context.getString(R.string.test_custom_string_set), null))
        assertThat(customExample.testStringSet).hasSize(customPref.getStringSet(context.getString(R.string.test_custom_string_set), null).size)
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

        assertThat(example.testStringSet).containsExactlyInAnyOrder("test3")
        assertThat(examplePref.getStringSet("testStringSet", null)).containsExactlyInAnyOrder("test3")
    }
}
