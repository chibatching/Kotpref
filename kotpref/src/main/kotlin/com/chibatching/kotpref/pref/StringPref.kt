package com.chibatching.kotpref.pref

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.chibatching.kotpref.KotprefModel
import com.chibatching.kotpref.execute
import kotlin.reflect.KProperty

/**
 * Delegate string shared preferences property.
 * @param default default string value
 * @param key custom preferences key
 * @param commitByDefault commit this property instead of apply
 */
public fun KotprefModel.stringPref(
    default: String = "",
    key: String? = null,
    commitByDefault: Boolean = commitAllPropertiesByDefault
): AbstractPref<String> = StringPref(default, key, commitByDefault)

/**
 * Delegate string shared preferences property.
 * @param default default string value
 * @param key custom preferences key resource id
 * @param commitByDefault commit this property instead of apply
 */
public fun KotprefModel.stringPref(
    default: String = "",
    key: Int,
    commitByDefault: Boolean = commitAllPropertiesByDefault
): AbstractPref<String> =
    stringPref(default, context.getString(key), commitByDefault)

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
