package com.chibatching.kotpref

import android.annotation.TargetApi
import android.content.SharedPreferences
import android.os.Build
import com.chibatching.kotpref.pref.StringSetPref


internal class KotprefPreferences(val preferences: SharedPreferences) : SharedPreferences by preferences {

    override fun edit(): SharedPreferences.Editor {
        return KotprefEditor(preferences.edit())
    }

    internal inner class KotprefEditor(val editor: SharedPreferences.Editor) : SharedPreferences.Editor by editor {

        private val prefStringSet: HashMap<String, StringSetPref.PrefMutableSet> by lazy { HashMap<String, StringSetPref.PrefMutableSet>() }

        override fun apply() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                prefStringSet.forEach { key, set ->
                    editor.putStringSet(key, set)
                    set.syncTransaction()
                }
                prefStringSet.clear()
            }
            editor.apply()
        }

        override fun commit(): Boolean {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                prefStringSet.forEach { key, set ->
                    editor.putStringSet(key, set)
                    set.syncTransaction()
                }
                prefStringSet.clear()
            }
            return editor.commit()
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        internal fun putStringSet(key: String, prefSet: StringSetPref.PrefMutableSet): SharedPreferences.Editor {
            prefStringSet.put(key, prefSet)
            return this
        }
    }
}
