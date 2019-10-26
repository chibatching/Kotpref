package com.chibatching.kotpref.pref

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.chibatching.kotpref.execute

internal class StringPref(
    val default: String,
    override val key: String?,
    private val commitByDefault: Boolean
) : AbstractPref<String>() {

    override fun getFromPreference(propertyName: String, preference: SharedPreferences): String {
        return requireNotNull(preference.getString(key ?: propertyName, default))
    }

    @SuppressLint("CommitPrefEdits")
    override fun setToPreference(
        propertyName: String,
        value: String,
        preference: SharedPreferences
    ) {
        preference.edit().putString(key ?: propertyName, value).execute(commitByDefault)
    }

    override fun setToEditor(
        propertyName: String,
        value: String,
        editor: SharedPreferences.Editor
    ) {
        editor.putString(key ?: propertyName, value)
    }
}
