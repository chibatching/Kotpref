package com.chibatching.kotpref.pref

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.chibatching.kotpref.execute
import kotlin.reflect.KProperty

internal class BooleanPref(
    val default: Boolean,
    override val key: String?,
    private val commitByDefault: Boolean
) : AbstractPref<Boolean>() {

    override fun getFromPreference(property: KProperty<*>, preference: SharedPreferences): Boolean {
        return preference.getBoolean(preferenceKey, default)
    }

    @SuppressLint("CommitPrefEdits")
    override fun setToPreference(
        property: KProperty<*>,
        value: Boolean,
        preference: SharedPreferences
    ) {
        preference.edit().putBoolean(preferenceKey, value).execute(commitByDefault)
    }

    override fun setToEditor(
        property: KProperty<*>,
        value: Boolean,
        editor: SharedPreferences.Editor
    ) {
        editor.putBoolean(preferenceKey, value)
    }
}
