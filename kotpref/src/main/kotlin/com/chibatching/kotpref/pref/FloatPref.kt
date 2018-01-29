package com.chibatching.kotpref.pref

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.chibatching.kotpref.execute
import kotlin.reflect.KProperty


internal class FloatPref(val default: Float, override val key: String?, private val commitByDefault: Boolean) : AbstractPref<Float>() {

    override fun getFromPreference(property: KProperty<*>, preference: SharedPreferences): Float {
        return preference.getFloat(key ?: property.name, default)
    }

    @SuppressLint("CommitPrefEdits")
    override fun setToPreference(property: KProperty<*>, value: Float, preference: SharedPreferences) {
        preference.edit().putFloat(key ?: property.name, value).execute(commitByDefault)
    }

    override fun setToEditor(property: KProperty<*>, value: Float, editor: SharedPreferences.Editor) {
        editor.putFloat(key ?: property.name, value)
    }
}
