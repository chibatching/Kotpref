package com.chibatching.kotpref

import android.annotation.TargetApi
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import java.util.*
import kotlin.collections.HashSet


@RunWith(ParameterizedRobolectricTestRunner::class)
class BlockingBulkEditTest(private val commitAllProperties: Boolean) {
    companion object {
        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters(name = "commitAllProperties = {0}")
        fun data(): Collection<Array<out Any>> {
            return Arrays.asList(arrayOf(false), arrayOf(true))
        }
    }

    lateinit var example: Example
    lateinit var context: Context
    lateinit var pref: SharedPreferences

    @Before
    fun setUp() {
        context = RuntimeEnvironment.application
        Kotpref.init(context)
        example = Example(commitAllProperties)

        pref = example.preferences
        pref.edit().clear().commit()
    }

    @After
    fun tearDown() {
        example.clear()
    }

    // blockingBulk test

    @Test
    fun changedPrefValueCanReadBothInAndOutImmediateBulkEdit() {
        example.testInt = 30

        example.blockingBulk {
            testInt = 5839

            Assertions.assertThat(testInt).isEqualTo(5839)
        }
        Assertions.assertThat(example.testInt).isEqualTo(5839)
    }

    @Test
    fun changedPrefValueNotAffectPreferenceInImmediateBulkEdit() {
        example.testLong = -9831L

        example.blockingBulk {
            testLong = 831456L

            Assertions.assertThat(pref.getLong("testLong", 0L)).isEqualTo(-9831L)
        }
        Assertions.assertThat(pref.getLong("testLong", 0L)).isEqualTo(831456L)
    }

    @Test
    fun errorInImmediateBulkEditCauseCancelTransaction() {
        example.testString = "before"

        try {
            example.blockingBulk {
                testString = "edit in bulk"
                throw Exception()
            }
        } catch (e: Exception) {
        }
        Assertions.assertThat(example.testString).isEqualTo("before")
        Assertions.assertThat(pref.getString("testString", "")).isEqualTo("before")
    }

    @Test
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    fun addRemoveStringSetPrefValueCanReadBothInAndOutImmediateBulkEdit() {
        example.testStringSet.add("test1")

        example.blockingBulk {
            testStringSet.add("test2")
            testStringSet.add("test3")
            testStringSet.remove("test2")

            Assertions.assertThat(testStringSet).containsExactlyInAnyOrder("test1", "test3")
        }
        Assertions.assertThat(example.testStringSet).containsExactlyInAnyOrder("test1", "test3")
    }

    @Test
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    fun addRemoveStringSetPrefValNotAffectPreferenceInImmediateBulkEdit() {
        example.testStringSet.add("test1")

        example.blockingBulk {
            testStringSet.add("test2")
            testStringSet.add("test3")
            testStringSet.remove("test2")

            Assertions.assertThat(pref.getStringSet("testStringSet", null)).containsExactlyInAnyOrder("test1")
        }
        Assertions.assertThat(pref.getStringSet("testStringSet", null)).containsExactlyInAnyOrder("test1", "test3")
    }

    @Test
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    fun addAllToStringSetPrefValueCanReadBothInAndOutImmediateBulkEdit() {
        example.testStringSet.add("test1")

        val addSet = TreeSet<String>().apply {
            add("test2")
            add("test3")
        }

        example.blockingBulk {
            testStringSet.addAll(addSet)

            Assertions.assertThat(testStringSet).containsExactlyInAnyOrder("test1", "test2", "test3")
        }
        Assertions.assertThat(example.testStringSet).containsExactlyInAnyOrder("test1", "test2", "test3")
    }

    @Test
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    fun addAllToStringSetPrefValueNotAffectPreferenceInImmediateBulkEdit() {
        example.testStringSet.add("test1")

        val addSet = TreeSet<String>().apply {
            add("test2")
            add("test3")
        }

        example.blockingBulk {
            testStringSet.addAll(addSet)

            Assertions.assertThat(pref.getStringSet("testStringSet", null)).containsExactlyInAnyOrder("test1")
        }
        Assertions.assertThat(pref.getStringSet("testStringSet", null)).containsExactlyInAnyOrder("test1", "test2", "test3")
    }

    @Test
    fun removeAllFromStringSetPrefValueCanReadBothInAndOutImmediateBulkEdit() {
        example.testStringSet.apply {
            add("test1")
            add("test2")
            add("test3")
        }

        val removeSet = TreeSet<String>().apply {
            add("test1")
            add("test3")
        }

        example.blockingBulk {
            testStringSet.removeAll(removeSet)

            Assertions.assertThat(testStringSet).containsExactlyInAnyOrder("test2")
        }
        Assertions.assertThat(example.testStringSet).containsExactlyInAnyOrder("test2")
    }

    @Test
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    fun removeAllFromStringSetPrefValNotAffectPreferenceInImmediateBulkEdit() {
        example.testStringSet.apply {
            add("test1")
            add("test2")
            add("test3")
        }

        val removeSet = TreeSet<String>().apply {
            add("test1")
            add("test3")
        }

        example.blockingBulk {
            testStringSet.removeAll(removeSet)

            Assertions.assertThat(pref.getStringSet("testStringSet", null)).containsExactlyInAnyOrder("test1", "test2", "test3")
        }
        Assertions.assertThat(pref.getStringSet("testStringSet", null)).containsExactlyInAnyOrder("test2")
    }

    @Test
    fun retainAllStringSetPrefValueCanReadBothInAndOutImmediateBulkEdit() {
        example.testStringSet.apply {
            add("test1")
            add("test2")
            add("test3")
        }

        val retainSet = TreeSet<String>().apply {
            add("test1")
            add("test3")
            add("test4")
        }

        example.blockingBulk {
            testStringSet.retainAll(retainSet)

            Assertions.assertThat(testStringSet).containsExactlyInAnyOrder("test1", "test3")
        }
        Assertions.assertThat(example.testStringSet).containsExactlyInAnyOrder("test1", "test3")
    }

    @Test
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    fun retainAllStringSetPrefValueNotAffectPreferenceInImmediateBulkEdit() {
        example.testStringSet.apply {
            add("test1")
            add("test2")
            add("test3")
        }

        val retainSet = TreeSet<String>().apply {
            add("test1")
            add("test3")
            add("test4")
        }

        example.blockingBulk {
            testStringSet.retainAll(retainSet)

            Assertions.assertThat(pref.getStringSet("testStringSet", null)).containsExactlyInAnyOrder("test1", "test2", "test3")
        }
        Assertions.assertThat(pref.getStringSet("testStringSet", null)).containsExactlyInAnyOrder("test1", "test3")
    }

    @Test
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    fun removeStringSetPrefItemViaIteratorInBulkEdit() {
        example.testStringSet.apply {
            add("test1")
            add("test2")
            add("test3")
        }

        val originalCopy = HashSet<String>(example.testStringSet)
        val deletedItem = HashSet<String>()

        example.blockingBulk {
            example.testStringSet.iterator().let { iterator ->
                deletedItem.add(iterator.next())
                iterator.remove()
                deletedItem.add(iterator.next())
                iterator.remove()
            }

            assertThat(example.testStringSet).containsAll(originalCopy - deletedItem)
            assertThat(pref.getStringSet("testStringSet", null)).containsAll(originalCopy)
        }

        assertThat(example.testStringSet).containsAll(originalCopy - deletedItem)
        assertThat(pref.getStringSet("testStringSet", null)).containsAll(originalCopy - deletedItem)
    }
}
