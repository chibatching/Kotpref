package com.chibatching.kotpref.enumpref

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.chibatching.kotpref.execute
import com.chibatching.kotpref.pref.AbstractPref
import kotlin.reflect.KClass
import kotlin.reflect.KProperty


class EnumNullableValuePref<T : Enum<*>>(enumClass: KClass<T>, val default: T?, override val key: String?, private val commitByDefault: Boolean) : AbstractPref<T?>() {
    private val enumConstants = enumClass.java.enumConstants

    override fun getFromPreference(property: KProperty<*>, preference: SharedPreferences): T? {
        val value = preference.getString(key ?: property.name, default?.name)
        return enumConstants.firstOrNull { it.name == value }
    }

    @SuppressLint("CommitPrefEdits")
    override fun setToPreference(property: KProperty<*>, value: T?, preference: SharedPreferences) {
        preference.edit().putString(key ?: property.name, value?.name).execute(commitByDefault)
    }

    override fun setToEditor(property: KProperty<*>, value: T?, editor: SharedPreferences.Editor) {
        editor.putString(key ?: property.name, value?.name)
    }
}
