package com.chibatching.kotpref.gsonpref

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.chibatching.kotpref.Kotpref
import com.chibatching.kotpref.execute
import com.chibatching.kotpref.pref.AbstractPref
import java.lang.reflect.Type

class GsonPref<T : Any>(
    private val targetType: Type,
    private val default: T,
    override val key: String?,
    private val commitByDefault: Boolean
) : AbstractPref<T>() {

    override fun getFromPreference(propertyName: String, preference: SharedPreferences): T {
        return preference.getString(key ?: propertyName, null)?.let { json ->
            deserializeFromJson(json) ?: default
        } ?: default
    }

    @SuppressLint("CommitPrefEdits")
    override fun setToPreference(propertyName: String, value: T, preference: SharedPreferences) {
        serializeToJson(value).let { json ->
            preference.edit().putString(key ?: propertyName, json).execute(commitByDefault)
        }
    }

    override fun setToEditor(propertyName: String, value: T, editor: SharedPreferences.Editor) {
        serializeToJson(value).let { json ->
            editor.putString(key ?: propertyName, json)
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
