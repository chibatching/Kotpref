package com.chibatching.kotpref.enumpref

import android.content.SharedPreferences
import com.chibatching.kotpref.pref.AbstractPref
import kotlin.reflect.KClass
import kotlin.reflect.KProperty


class EnumValuePref<T : Enum<*>>(enumClass : KClass<T>, val default: T, val key: String?) : AbstractPref<T>() {
    private val enumConstants = enumClass.java.enumConstants

    override fun getFromPreference(property: KProperty<*>, preference: SharedPreferences): T {
        val value = preference.getString(key ?: property.name, default.name)
        return enumConstants.first { it.name == value }
    }

    override fun setToPreference(property: KProperty<*>, value: T, preference: SharedPreferences) {
        preference.edit().putString(key ?: property.name, value.name).apply()
    }

    override fun setToEditor(property: KProperty<*>, value: T, editor: SharedPreferences.Editor) {
        editor.putString(key ?: property.name, value.name)
    }
}
