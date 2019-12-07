package com.chibatching.kotpref.pref

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.chibatching.kotpref.execute
import kotlin.reflect.KProperty

internal class StringPref(
    val default: String,
    override val key: String?,
    private val commitByDefault: Boolean
) : AbstractPref<String>() {

    override fun getFromPreference(property: KProperty<*>, preference: SharedPreferences): String {
        return requireNotNull(preference.getString(preferenceKey, default))
    }

    @SuppressLint("CommitPrefEdits")
    override fun setToPreference(
        property: KProperty<*>,
        value: String,
        preference: SharedPreferences
    ) {
        preference.edit().putString(preferenceKey, value).execute(commitByDefault)
    }

    override fun setToEditor(
        property: KProperty<*>,
        value: String,
        editor: SharedPreferences.Editor
    ) {
        editor.putString(preferenceKey, value)
    }
}
