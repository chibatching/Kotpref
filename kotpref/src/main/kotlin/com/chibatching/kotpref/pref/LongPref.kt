package com.chibatching.kotpref.pref

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.chibatching.kotpref.execute

internal class LongPref(
    val default: Long,
    override val key: String?,
    private val commitByDefault: Boolean
) : AbstractPref<Long>() {

    override fun getFromPreference(propertyName: String, preference: SharedPreferences): Long {
        return preference.getLong(key ?: propertyName, default)
    }

    @SuppressLint("CommitPrefEdits")
    override fun setToPreference(
        propertyName: String,
        value: Long,
        preference: SharedPreferences
    ) {
        preference.edit().putLong(key ?: propertyName, value).execute(commitByDefault)
    }

    override fun setToEditor(
        propertyName: String,
        value: Long,
        editor: SharedPreferences.Editor
    ) {
        editor.putLong(key ?: propertyName, value)
    }
}
