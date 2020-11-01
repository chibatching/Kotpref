package com.chibatching.kotpref

import android.annotation.TargetApi
import android.content.SharedPreferences
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.Arrays
import java.util.HashSet
import java.util.TreeSet

@RunWith(ParameterizedRobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.N]) // Avoid hang caused by Robolectric https://github.com/robolectric/robolectric/issues/3641
internal class BlockingBulkEditTest(private val commitAllProperties: Boolean) {
    companion object {
        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters(name = "commitAllProperties = {0}")
        fun data(): Collection<Array<out Any>> {
            return Arrays.asList(arrayOf(false), arrayOf(true))
        }
    }

    lateinit var example: Example
    lateinit var pref: SharedPreferences

    @Before
    fun setUp() {
        example = Example(commitAllProperties, ApplicationProvider.getApplicationContext())

        pref = example.preferences
        pref.edit().clear().commit()
    }

    @After
    fun tearDown() {
        example.clear()
    }

    @Test
    fun changedPrefValueCanReadBothInAndOutImmediateBulkEdit() {
        example.testInt = 30

        example.blockingBulk {
            testInt = 5839

            assertThat(testInt).isEqualTo(5839)
        }
        assertThat(example.testInt).isEqualTo(5839)
    }

    @Test
    fun changedPrefValueNotAffectPreferenceInImmediateBulkEdit() {
        example.testLong = -9831L

        example.blockingBulk {
            testLong = 831456L

            assertThat(pref.getLong("testLong", 0L)).isEqualTo(-9831L)
        }
        assertThat(pref.getLong("testLong", 0L)).isEqualTo(831456L)
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
            // no-op
        }
        assertThat(example.testString)
            .isEqualTo("before")
        assertThat(example.testString)
            .isEqualTo(pref.getString("testString", ""))
    }

    @Test
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    fun addRemoveStringSetPrefValueCanReadBothInAndOutImmediateBulkEdit() {
        example.testStringSet.add("test1")

        example.blockingBulk {
            testStringSet.add("test2")
            testStringSet.add("test3")
            testStringSet.remove("test2")

            assertThat(testStringSet).containsExactly("test1", "test3")
        }
        assertThat(example.testStringSet).containsExactly("test1", "test3")
    }

    @Test
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    fun addRemoveStringSetPrefValNotAffectPreferenceInImmediateBulkEdit() {
        example.testStringSet.add("test1")

        example.blockingBulk {
            testStringSet.add("test2")
            testStringSet.add("test3")
            testStringSet.remove("test2")

            assertThat(pref.getStringSet("testStringSet", null)).containsExactly("test1")
        }
        assertThat(pref.getStringSet("testStringSet", null)).containsExactly(
            "test1",
            "test3"
        )
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

            assertThat(testStringSet).containsExactly("test1", "test2", "test3")
        }
        assertThat(example.testStringSet).containsExactly("test1", "test2", "test3")
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

            assertThat(pref.getStringSet("testStringSet", null)).containsExactly("test1")
        }
        assertThat(pref.getStringSet("testStringSet", null)).containsExactly(
            "test1",
            "test2",
            "test3"
        )
    }

    @Test
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
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

            assertThat(testStringSet).containsExactly("test2")
        }
        assertThat(example.testStringSet).containsExactly("test2")
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

            assertThat(pref.getStringSet("testStringSet", null)).containsExactly(
                "test1",
                "test2",
                "test3"
            )
        }
        assertThat(pref.getStringSet("testStringSet", null)).containsExactly("test2")
    }

    @Test
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
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

            assertThat(testStringSet).containsExactly("test1", "test3")
        }
        assertThat(example.testStringSet).containsExactly("test1", "test3")
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

            assertThat(pref.getStringSet("testStringSet", null)).containsExactly(
                "test1",
                "test2",
                "test3"
            )
        }
        assertThat(pref.getStringSet("testStringSet", null)).containsExactly(
            "test1",
            "test3"
        )
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

            assertThat(example.testStringSet).containsExactlyElementsIn(originalCopy - deletedItem)
            assertThat(pref.getStringSet("testStringSet", null)).containsExactlyElementsIn(originalCopy)
        }

        assertThat(example.testStringSet).containsExactlyElementsIn(originalCopy - deletedItem)
        assertThat(pref.getStringSet("testStringSet", null)).containsExactlyElementsIn(originalCopy - deletedItem)
    }
}
