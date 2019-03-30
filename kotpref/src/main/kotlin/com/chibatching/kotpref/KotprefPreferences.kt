package com.chibatching.kotpref

import android.annotation.TargetApi
import android.content.SharedPreferences
import android.os.Build
import com.chibatching.kotpref.pref.StringSetPref
import java.util.HashMap

internal class KotprefPreferences(
    val preferences: SharedPreferences
) : SharedPreferences by preferences {

    override fun edit(): SharedPreferences.Editor {
        return KotprefEditor(preferences.edit())
    }

    internal inner class KotprefEditor(
        val editor: SharedPreferences.Editor
    ) : SharedPreferences.Editor by editor {

        private val prefStringSet: MutableMap<String, StringSetPref.PrefMutableSet> by lazy {
            HashMap<String, StringSetPref.PrefMutableSet>()
        }

        override fun apply() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                syncTransaction()
            }
            editor.apply()
        }

        override fun commit(): Boolean {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                syncTransaction()
            }
            return editor.commit()
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        internal fun putStringSet(
            key: String,
            prefSet: StringSetPref.PrefMutableSet
        ): SharedPreferences.Editor {
            prefStringSet.put(key, prefSet)
            return this
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        private fun syncTransaction() {
            prefStringSet.keys.forEach { key ->
                prefStringSet[key]?.let {
                    editor.putStringSet(key, it)
                    it.syncTransaction()
                }
            }
            prefStringSet.clear()
        }
    }
}
