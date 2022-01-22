package com.chibatching.kotpref.pref

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.chibatching.kotpref.KotprefModel
import com.chibatching.kotpref.execute
import kotlin.reflect.KProperty

/**
 * Delegate boolean shared preferences property.
 * @param default default boolean value
 * @param key custom preferences key
 * @param commitByDefault commit this property instead of apply
 */
public fun KotprefModel.booleanPref(
    default: Boolean = false,
    key: String? = null,
    commitByDefault: Boolean = commitAllPropertiesByDefault
): AbstractPref<Boolean> = BooleanPref(default, key, commitByDefault)

/**
 * Delegate boolean shared preferences property.
 * @param default default boolean value
 * @param key custom preferences key resource id
 * @param commitByDefault commit this property instead of apply
 */
public fun KotprefModel.booleanPref(
    default: Boolean = false,
    key: Int,
    commitByDefault: Boolean = commitAllPropertiesByDefault
): AbstractPref<Boolean> = booleanPref(default, context.getString(key), commitByDefault)

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
