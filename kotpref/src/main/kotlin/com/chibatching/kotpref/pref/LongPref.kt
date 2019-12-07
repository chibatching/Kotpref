package com.chibatching.kotpref.pref

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.chibatching.kotpref.execute
import kotlin.reflect.KProperty

internal class LongPref(
    val default: Long,
    override val key: String?,
    private val commitByDefault: Boolean
) : AbstractPref<Long>() {

    override fun getFromPreference(property: KProperty<*>, preference: SharedPreferences): Long {
        return preference.getLong(preferenceKey, default)
    }

    @SuppressLint("CommitPrefEdits")
    override fun setToPreference(
        property: KProperty<*>,
        value: Long,
        preference: SharedPreferences
    ) {
        preference.edit().putLong(preferenceKey, value).execute(commitByDefault)
    }

    override fun setToEditor(
        property: KProperty<*>,
        value: Long,
        editor: SharedPreferences.Editor
    ) {
        editor.putLong(preferenceKey, value)
    }
}
