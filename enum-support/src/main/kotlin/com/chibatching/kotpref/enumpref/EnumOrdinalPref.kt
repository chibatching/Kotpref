package com.chibatching.kotpref.enumpref

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.chibatching.kotpref.execute
import com.chibatching.kotpref.pref.AbstractPref
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

public class EnumOrdinalPref<T : Enum<*>>(
    enumClass: KClass<T>,
    private val default: T,
    override val key: String?,
    private val commitByDefault: Boolean
) : AbstractPref<T>() {
    private val enumConstants = enumClass.java.enumConstants

    override fun getFromPreference(property: KProperty<*>, preference: SharedPreferences): T {
        val value = preference.getInt(preferenceKey, default.ordinal)
        return enumConstants!!.first { it.ordinal == value }
    }

    @SuppressLint("CommitPrefEdits")
    override fun setToPreference(property: KProperty<*>, value: T, preference: SharedPreferences) {
        preference.edit().putInt(preferenceKey, value.ordinal).execute(commitByDefault)
    }

    override fun setToEditor(property: KProperty<*>, value: T, editor: SharedPreferences.Editor) {
        editor.putInt(preferenceKey, value.ordinal)
    }
}
