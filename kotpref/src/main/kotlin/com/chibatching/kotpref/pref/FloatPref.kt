package com.chibatching.kotpref.pref

import android.content.SharedPreferences
import kotlin.reflect.KProperty


internal class FloatPref(val default: Float, val key: String?) : AbstractPref<Float>() {

    override fun getFromPreference(property: KProperty<*>, preference: SharedPreferences): Float {
        return preference.getFloat(key ?: property.name, default)
    }

    override fun setToPreference(property: KProperty<*>, value: Float, preference: SharedPreferences) {
        preference.edit().putFloat(key ?: property.name, value).apply()
    }

    override fun setToEditor(property: KProperty<*>, value: Float, editor: SharedPreferences.Editor) {
        editor.putFloat(key ?: property.name, value)
    }
}
