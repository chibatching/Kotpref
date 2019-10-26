package com.chibatching.kotpref.pref

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.chibatching.kotpref.execute

internal class FloatPref(
    val default: Float,
    override val key: String?,
    private val commitByDefault: Boolean
) : AbstractPref<Float>() {

    override fun getFromPreference(propertyName: String, preference: SharedPreferences): Float {
        return preference.getFloat(key ?: propertyName, default)
    }

    @SuppressLint("CommitPrefEdits")
    override fun setToPreference(
        propertyName: String,
        value: Float,
        preference: SharedPreferences
    ) {
        preference.edit().putFloat(key ?: propertyName, value).execute(commitByDefault)
    }

    override fun setToEditor(
        propertyName: String,
        value: Float,
        editor: SharedPreferences.Editor
    ) {
        editor.putFloat(key ?: propertyName, value)
    }
}
