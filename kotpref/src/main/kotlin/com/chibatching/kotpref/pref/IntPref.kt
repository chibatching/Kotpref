package com.chibatching.kotpref.pref

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.chibatching.kotpref.execute

internal class IntPref(
    val default: Int,
    override val key: String?,
    private val commitByDefault: Boolean
) : AbstractPref<Int>() {

    override fun getFromPreference(propertyName: String, preference: SharedPreferences): Int {
        return preference.getInt(key ?: propertyName, default)
    }

    @SuppressLint("CommitPrefEdits")
    override fun setToPreference(
        propertyName: String,
        value: Int,
        preference: SharedPreferences
    ) {
        preference.edit().putInt(key ?: propertyName, value).execute(commitByDefault)
    }

    override fun setToEditor(propertyName: String, value: Int, editor: SharedPreferences.Editor) {
        editor.putInt(key ?: propertyName, value)
    }
}
