package com.chibatching.kotpref.pref

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.chibatching.kotpref.KotprefModel
import com.chibatching.kotpref.execute
import kotlin.reflect.KProperty

/**
 * Delegate int shared preferences property.
 * @param default default int value
 * @param key custom preferences key
 * @param commitByDefault commit this property instead of apply
 */
public fun KotprefModel.intPref(
    default: Int = 0,
    key: String? = null,
    commitByDefault: Boolean = commitAllPropertiesByDefault
): AbstractPref<Int> = IntPref(default, key, commitByDefault)

/**
 * Delegate int shared preferences property.
 * @param default default int value
 * @param key custom preferences key resource id
 * @param commitByDefault commit this property instead of apply
 */
public fun KotprefModel.intPref(
    default: Int = 0,
    key: Int,
    commitByDefault: Boolean = commitAllPropertiesByDefault
): AbstractPref<Int> =
    intPref(default, context.getString(key), commitByDefault)

internal class IntPref(
    val default: Int,
    override val key: String?,
    private val commitByDefault: Boolean
) : AbstractPref<Int>() {

    override fun getFromPreference(property: KProperty<*>, preference: SharedPreferences): Int {
        return preference.getInt(preferenceKey, default)
    }

    @SuppressLint("CommitPrefEdits")
    override fun setToPreference(
        property: KProperty<*>,
        value: Int,
        preference: SharedPreferences
    ) {
        preference.edit().putInt(preferenceKey, value).execute(commitByDefault)
    }

    override fun setToEditor(property: KProperty<*>, value: Int, editor: SharedPreferences.Editor) {
        editor.putInt(preferenceKey, value)
    }
}
