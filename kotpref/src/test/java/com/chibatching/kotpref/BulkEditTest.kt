package com.chibatching.kotpref

import android.annotation.TargetApi
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import java.util.*


@RunWith(ParameterizedRobolectricTestRunner::class)
class BulkEditTest(private val commitAllProperties: Boolean) {
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

    @Test
    fun changedPrefValueCanReadBothInAndOutBulkEdit() {
        example.testInt = 30

        example.bulk {
            testInt = 5839

            assertThat(testInt).isEqualTo(5839)
        }
        assertThat(example.testInt).isEqualTo(5839)
    }

    @Test
    fun changedPrefValueNotAffectPreferenceInBulkEdit() {
        example.testLong = -9831L

        example.bulk {
            testLong = 831456L

            assertThat(pref.getLong("testLong", 0L)).isEqualTo(-9831L)
        }
        assertThat(pref.getLong("testLong", 0L)).isEqualTo(831456L)
    }

    @Test
    fun errorInBulkEditCauseCancelTransaction() {
        example.testString = "before"

        try {
            example.bulk {
                testString = "edit in bulk"
                throw Exception()
            }
        } catch (e: Exception) {
            // no-op
        }
        assertThat(example.testString).isEqualTo("before")
        assertThat(pref.getString("testString", "")).isEqualTo("before")
    }

    @Test
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    fun addRemoveStringSetPrefValCanReadBothInAndOutBulkEdit() {
        example.testStringSet.add("test1")

        example.bulk {
            testStringSet.add("test2")
            testStringSet.add("test3")
            testStringSet.remove("test2")

            assertThat(testStringSet).containsExactlyInAnyOrder("test1", "test3")
        }
        assertThat(example.testStringSet).containsExactlyInAnyOrder("test1", "test3")
    }

    @Test
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    fun addRemoveStringSetValueNotAffectPreferenceInBulkEdit() {
        example.testStringSet.add("test1")

        example.bulk {
            testStringSet.add("test2")
            testStringSet.add("test3")
            testStringSet.remove("test2")

            assertThat(pref.getStringSet("testStringSet", null)).containsExactlyInAnyOrder("test1")
        }
        assertThat(pref.getStringSet("testStringSet", null)).containsExactlyInAnyOrder("test1", "test3")
    }

    @Test
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    fun addAllToStringSetPrefValueCanReadBothInAndOutBulkEdit() {
        example.testStringSet.add("test1")

        val addSet = TreeSet<String>().apply {
            add("test2")
            add("test3")
        }

        example.bulk {
            testStringSet.addAll(addSet)

            assertThat(testStringSet).containsExactlyInAnyOrder("test1", "test2", "test3")
        }
        assertThat(example.testStringSet).containsExactlyInAnyOrder("test1", "test2", "test3")
    }

    @Test
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    fun addAllToStringSetPrefValueNotAffectPreferenceInBulkEdit() {
        example.testStringSet.add("test1")

        val addSet = TreeSet<String>().apply {
            add("test2")
            add("test3")
        }

        example.bulk {
            testStringSet.addAll(addSet)

            assertThat(pref.getStringSet("testStringSet", null)).containsExactlyInAnyOrder("test1")
        }
        assertThat(pref.getStringSet("testStringSet", null)).containsExactlyInAnyOrder("test1", "test2", "test3")
    }

    @Test
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    fun removeAllFromStringSetPrefValueCanReadBothInAndOutBulkEdit() {
        example.testStringSet.apply {
            add("test1")
            add("test2")
            add("test3")
        }

        val removeSet = TreeSet<String>().apply {
            add("test1")
            add("test3")
        }

        example.bulk {
            testStringSet.removeAll(removeSet)

            assertThat(testStringSet).containsExactlyInAnyOrder("test2")
        }
        assertThat(example.testStringSet).containsExactlyInAnyOrder("test2")
    }

    @Test
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    fun removeAllFromStringSetPrefValueNotAffectPreferenceInBulkEdit() {
        example.testStringSet.apply {
            add("test1")
            add("test2")
            add("test3")
        }

        val removeSet = TreeSet<String>().apply {
            add("test1")
            add("test3")
        }

        example.bulk {
            testStringSet.removeAll(removeSet)

            assertThat(pref.getStringSet("testStringSet", null)).containsExactlyInAnyOrder("test1", "test2", "test3")
        }
        assertThat(pref.getStringSet("testStringSet", null)).containsExactlyInAnyOrder("test2")
    }

    @Test
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    fun retainAllStringSetPrefValueCanReadBothInAndOutBulkEdit() {
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

        example.bulk {
            testStringSet.retainAll(retainSet)

            assertThat(testStringSet).containsExactlyInAnyOrder("test1", "test3")
        }
        assertThat(example.testStringSet).containsExactlyInAnyOrder("test1", "test3")
    }

    @Test
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    fun retainAllStringSetPrefValueNotAffectPreferenceInBulkEdit() {
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

        example.bulk {
            testStringSet.retainAll(retainSet)

            assertThat(pref.getStringSet("testStringSet", null)).containsExactlyInAnyOrder("test1", "test2", "test3")
        }
        assertThat(pref.getStringSet("testStringSet", null)).containsExactlyInAnyOrder("test1", "test3")
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

        example.bulk {
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
