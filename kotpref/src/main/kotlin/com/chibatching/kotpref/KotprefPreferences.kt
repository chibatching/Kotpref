package com.chibatching.kotpref

import android.content.SharedPreferences
import java.util.*


internal class KotprefPreferences(val preferences: SharedPreferences) : SharedPreferences by preferences {

    override fun edit(): SharedPreferences.Editor {
        return KotprefEditor(preferences.edit())
    }

    inner class KotprefEditor(val editor: SharedPreferences.Editor) : SharedPreferences.Editor by editor {

        private var prefStringSet: LinkedList<KotprefModel.PrefMutableSet>? = null

        override fun apply() {
            editor.apply()
            prefStringSet?.forEach {
                it.syncTransaction()
            }
            prefStringSet = null
        }

        override fun commit(): Boolean {
            val result = editor.commit()
            prefStringSet?.forEach {
                it.syncTransaction()
            }
            prefStringSet = null
            return result
        }

        fun putStringSet(key: String?, values: MutableSet<String>?, prefSet: KotprefModel.PrefMutableSet): SharedPreferences.Editor {
            editor.putStringSet(key, values)
            if (prefStringSet == null) {
                prefStringSet = LinkedList<KotprefModel.PrefMutableSet>()
            }
            prefStringSet?.add(prefSet)
            return this
        }
    }
}