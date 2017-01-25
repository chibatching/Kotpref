package com.chibatching.kotpref.enumsupport

import android.content.SharedPreferences
import com.chibatching.kotpref.pref.AbstractPref
import kotlin.reflect.KClass
import kotlin.reflect.KProperty


internal class EnumOrdinalPref<T : Enum<*>>(enumClass : KClass<T>, val default: T, val key: String?) : AbstractPref<T>() {
    private val enumConstants = enumClass.java.enumConstants

    override fun getFromPreference(property: KProperty<*>, preference: SharedPreferences): T {
        val value = preference.getInt(key ?: property.name, default.ordinal)
        return enumConstants.first { it.ordinal == value }
    }

    override fun setToPreference(property: KProperty<*>, value: T, preference: SharedPreferences) {
        preference.edit().putInt(key ?: property.name, value.ordinal).apply()
    }

    override fun setToEditor(property: KProperty<*>, value: T, editor: SharedPreferences.Editor) {
        editor.putInt(key ?: property.name, value.ordinal)
    }
}
