package com.chibatching.kotpref.enumpref

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.chibatching.kotpref.execute
import com.chibatching.kotpref.pref.AbstractPref
import kotlin.reflect.KClass

class EnumOrdinalPref<T : Enum<*>>(
    enumClass: KClass<T>,
    private val default: T,
    override val key: String?,
    private val commitByDefault: Boolean
) : AbstractPref<T>() {
    private val enumConstants = enumClass.java.enumConstants

    override fun getFromPreference(propertyName: String, preference: SharedPreferences): T {
        val value = preference.getInt(key ?: propertyName, default.ordinal)
        return enumConstants.first { it.ordinal == value }
    }

    @SuppressLint("CommitPrefEdits")
    override fun setToPreference(propertyName: String, value: T, preference: SharedPreferences) {
        preference.edit().putInt(key ?: propertyName, value.ordinal).execute(commitByDefault)
    }

    override fun setToEditor(propertyName: String, value: T, editor: SharedPreferences.Editor) {
        editor.putInt(key ?: propertyName, value.ordinal)
    }
}
