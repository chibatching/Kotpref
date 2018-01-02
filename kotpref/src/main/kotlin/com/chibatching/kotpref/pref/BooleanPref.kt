package com.chibatching.kotpref.pref

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.chibatching.kotpref.execute
import kotlin.reflect.KProperty


internal class BooleanPref(val default: Boolean, val key: String?, private val commitByDefault: Boolean) : AbstractPref<Boolean>() {

    override fun getFromPreference(property: KProperty<*>, preference: SharedPreferences): Boolean {
        return preference.getBoolean(key ?: property.name, default)
    }

    @SuppressLint("CommitPrefEdits")
    override fun setToPreference(property: KProperty<*>, value: Boolean, preference: SharedPreferences) {
        preference.edit().putBoolean(key ?: property.name, value).execute(commitByDefault)
    }

    override fun setToEditor(property: KProperty<*>, value: Boolean, editor: SharedPreferences.Editor) {
        editor.putBoolean(key ?: property.name, value)
    }
}
