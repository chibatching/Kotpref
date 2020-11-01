package com.chibatching.kotpref.gsonpref

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.chibatching.kotpref.Kotpref
import com.chibatching.kotpref.execute
import com.chibatching.kotpref.pref.AbstractPref
import java.lang.reflect.Type
import kotlin.reflect.KProperty

public class GsonPref<T : Any>(
    private val targetType: Type,
    private val default: () -> T,
    override val key: String?,
    private val commitByDefault: Boolean
) : AbstractPref<T>() {

    public constructor(
        targetType: Type,
        default: T,
        key: String?,
        commitByDefault: Boolean
    ) : this(
        targetType, { default }, key, commitByDefault
    )

    override fun getFromPreference(property: KProperty<*>, preference: SharedPreferences): T {
        return preference.getString(preferenceKey, null)?.let { json ->
            deserializeFromJson(json) ?: default()
        } ?: default()
    }

    @SuppressLint("CommitPrefEdits")
    override fun setToPreference(property: KProperty<*>, value: T, preference: SharedPreferences) {
        serializeToJson(value).let { json ->
            preference.edit().putString(preferenceKey, json).execute(commitByDefault)
        }
    }

    override fun setToEditor(property: KProperty<*>, value: T, editor: SharedPreferences.Editor) {
        serializeToJson(value).let { json ->
            editor.putString(preferenceKey, json)
        }
    }

    private fun serializeToJson(value: T?): String? {
        return Kotpref.gson.let {
            if (it == null) throw IllegalStateException("Gson has not been set to Kotpref")

            it.toJson(value)
        }
    }

    private fun deserializeFromJson(json: String): T? {
        return Kotpref.gson.let {
            if (it == null) throw IllegalStateException("Gson has not been set to Kotpref")

            it.fromJson(json, targetType)
        }
    }
}
