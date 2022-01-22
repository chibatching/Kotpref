package com.chibatching.kotpref.pref

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.chibatching.kotpref.KotprefModel
import com.chibatching.kotpref.execute
import kotlin.reflect.KProperty

/**
 * Delegate long shared preferences property.
 * @param default default long value
 * @param key custom preferences key
 * @param commitByDefault commit this property instead of apply
 */
public fun KotprefModel.longPref(
    default: Long = 0L,
    key: String? = null,
    commitByDefault: Boolean = commitAllPropertiesByDefault
): AbstractPref<Long> = LongPref(default, key, commitByDefault)

/**
 * Delegate long shared preferences property.
 * @param default default long value
 * @param key custom preferences key resource id
 * @param commitByDefault commit this property instead of apply
 */
public fun KotprefModel.longPref(
    default: Long = 0L,
    key: Int,
    commitByDefault: Boolean = commitAllPropertiesByDefault
): AbstractPref<Long> = longPref(default, context.getString(key), commitByDefault)

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
