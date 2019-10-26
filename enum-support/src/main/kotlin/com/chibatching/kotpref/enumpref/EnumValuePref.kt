package com.chibatching.kotpref.enumpref

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.chibatching.kotpref.execute
import com.chibatching.kotpref.pref.AbstractPref
import kotlin.reflect.KClass

class EnumValuePref<T : Enum<*>>(
    enumClass: KClass<T>,
    val default: T,
    override val key: String?,
    private val commitByDefault: Boolean
) : AbstractPref<T>() {
    private val enumConstants = enumClass.java.enumConstants

    override fun getFromPreference(propertyName: String, preference: SharedPreferences): T {
        val value = preference.getString(key ?: propertyName, default.name)
        return enumConstants.first { it.name == value }
    }

    @SuppressLint("CommitPrefEdits")
    override fun setToPreference(propertyName: String, value: T, preference: SharedPreferences) {
        preference.edit().putString(key ?: propertyName, value.name).execute(commitByDefault)
    }

    override fun setToEditor(propertyName: String, value: T, editor: SharedPreferences.Editor) {
        editor.putString(key ?: propertyName, value.name)
    }
}
