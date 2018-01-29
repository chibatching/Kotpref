package com.chibatching.kotpref.pref

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.chibatching.kotpref.execute
import kotlin.reflect.KProperty


internal class IntPref(val default: Int, override val key: String?, private val commitByDefault: Boolean) : AbstractPref<Int>() {

    override fun getFromPreference(property: KProperty<*>, preference: SharedPreferences): Int {
        return preference.getInt(key ?: property.name, default)
    }

    @SuppressLint("CommitPrefEdits")
    override fun setToPreference(property: KProperty<*>, value: Int, preference: SharedPreferences) {
        preference.edit().putInt(key ?: property.name, value).execute(commitByDefault)
    }

    override fun setToEditor(property: KProperty<*>, value: Int, editor: SharedPreferences.Editor) {
        editor.putInt(key ?: property.name, value)
    }
}
