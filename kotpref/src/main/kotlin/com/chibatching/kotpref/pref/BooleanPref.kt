package com.chibatching.kotpref.pref

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.chibatching.kotpref.execute

internal class BooleanPref(
    val default: Boolean,
    override val key: String?,
    private val commitByDefault: Boolean
) : AbstractPref<Boolean>() {

    override fun getFromPreference(propertyName: String, preference: SharedPreferences): Boolean {
        return preference.getBoolean(key ?: propertyName, default)
    }

    @SuppressLint("CommitPrefEdits")
    override fun setToPreference(
        propertyName: String,
        value: Boolean,
        preference: SharedPreferences
    ) {
        preference.edit().putBoolean(key ?: propertyName, value).execute(commitByDefault)
    }

    override fun setToEditor(
        propertyName: String,
        value: Boolean,
        editor: SharedPreferences.Editor
    ) {
        editor.putBoolean(key ?: propertyName, value)
    }
}
