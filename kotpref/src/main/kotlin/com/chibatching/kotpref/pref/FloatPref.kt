package com.chibatching.kotpref.pref

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.chibatching.kotpref.KotprefModel
import com.chibatching.kotpref.execute
import kotlin.reflect.KProperty

/**
 * Delegate float shared preferences property.
 * @param default default float value
 * @param key custom preferences key
 * @param commitByDefault commit this property instead of apply
 */
public fun KotprefModel.floatPref(
    default: Float = 0F,
    key: String? = null,
    commitByDefault: Boolean = commitAllPropertiesByDefault
): AbstractPref<Float> = FloatPref(default, key, commitByDefault)

/**
 * Delegate float shared preferences property.
 * @param default default float value
 * @param key custom preferences key resource id
 * @param commitByDefault commit this property instead of apply
 */
public fun KotprefModel.floatPref(
    default: Float = 0F,
    key: Int,
    commitByDefault: Boolean = commitAllPropertiesByDefault
): AbstractPref<Float> = floatPref(default, context.getString(key), commitByDefault)

internal class FloatPref(
    val default: Float,
    override val key: String?,
    private val commitByDefault: Boolean
) : AbstractPref<Float>() {

    override fun getFromPreference(property: KProperty<*>, preference: SharedPreferences): Float {
        return preference.getFloat(preferenceKey, default)
    }

    @SuppressLint("CommitPrefEdits")
    override fun setToPreference(
        property: KProperty<*>,
        value: Float,
        preference: SharedPreferences
    ) {
        preference.edit().putFloat(preferenceKey, value).execute(commitByDefault)
    }

    override fun setToEditor(
        property: KProperty<*>,
        value: Float,
        editor: SharedPreferences.Editor
    ) {
        editor.putFloat(preferenceKey, value)
    }
}
