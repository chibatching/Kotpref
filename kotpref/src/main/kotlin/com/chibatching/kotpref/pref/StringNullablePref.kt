package com.chibatching.kotpref.pref

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.chibatching.kotpref.KotprefModel
import com.chibatching.kotpref.execute
import kotlin.reflect.KProperty

/**
 * Delegate nullable string shared preferences property.
 * @param default default string value
 * @param key custom preferences key
 * @param commitByDefault commit this property instead of apply
 */
public fun KotprefModel.nullableStringPref(
    default: String? = null,
    key: String? = null,
    commitByDefault: Boolean = commitAllPropertiesByDefault
): AbstractPref<String?> =
    StringNullablePref(default, key, commitByDefault)

/**
 * Delegate nullable string shared preferences property.
 * @param default default string value
 * @param key custom preferences key resource id
 * @param commitByDefault commit this property instead of apply
 */
public fun KotprefModel.nullableStringPref(
    default: String? = null,
    key: Int,
    commitByDefault: Boolean = commitAllPropertiesByDefault
): AbstractPref<String?> =
    nullableStringPref(default, context.getString(key), commitByDefault)

internal class StringNullablePref(
    val default: String?,
    override val key: String?,
    private val commitByDefault: Boolean
) : AbstractPref<String?>() {

    override fun getFromPreference(property: KProperty<*>, preference: SharedPreferences): String? {
        return preference.getString(preferenceKey, default)
    }

    @SuppressLint("CommitPrefEdits")
    override fun setToPreference(
        property: KProperty<*>,
        value: String?,
        preference: SharedPreferences
    ) {
        preference.edit().putString(preferenceKey, value).execute(commitByDefault)
    }

    override fun setToEditor(
        property: KProperty<*>,
        value: String?,
        editor: SharedPreferences.Editor
    ) {
        editor.putString(preferenceKey, value)
    }
}
