package com.chibatching.kotpref

import android.content.SharedPreferences
import java.util.*


internal class KotprefPreferences(val preferences: SharedPreferences) : SharedPreferences by preferences {

    override fun edit(): SharedPreferences.Editor {
        return KotprefEditor(preferences.edit())
    }

    internal inner class KotprefEditor(val editor: SharedPreferences.Editor) : SharedPreferences.Editor by editor {

        private val prefStringSet: LinkedList<KotprefModel.PrefMutableSet> by lazy { LinkedList<KotprefModel.PrefMutableSet>() }

        override fun apply() {
            editor.apply()
            prefStringSet.forEach { it.syncTransaction() }
            prefStringSet.clear()
        }

        override fun commit(): Boolean {
            val result = editor.commit()
            prefStringSet.forEach { it.syncTransaction() }
            prefStringSet.clear()
            return result
        }

        internal fun putStringSet(key: String?, values: MutableSet<String>?, prefSet: KotprefModel.PrefMutableSet): SharedPreferences.Editor {
            editor.putStringSet(key, values)
            prefStringSet.add(prefSet)
            return this
        }
    }
}