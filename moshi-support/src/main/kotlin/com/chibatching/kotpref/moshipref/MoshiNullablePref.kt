package com.chibatching.kotpref.moshipref

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.chibatching.kotpref.Kotpref
import com.chibatching.kotpref.execute
import com.chibatching.kotpref.pref.AbstractPref
import kotlin.reflect.KClass
import kotlin.reflect.KProperty


class MoshiNullablePref<T : Any>(val targetClass: KClass<T>, val default: T?, val key: String?, private val commitByDefault: Boolean) : AbstractPref<T?>() {

    override fun getFromPreference(property: KProperty<*>, preference: SharedPreferences): T? {
        return preference.getString(key ?: property.name, null)?.let { json ->
            deserializeFromJson(json) ?: default
        }
    }

    @SuppressLint("CommitPrefEdits")
    override fun setToPreference(property: KProperty<*>, value: T?, preference: SharedPreferences) {
        serializeToJson(value).let { json ->
            preference.edit().putString(key ?: property.name, json).execute(commitByDefault)
        }
    }

    override fun setToEditor(property: KProperty<*>, value: T?, editor: SharedPreferences.Editor) {
        serializeToJson(value).let { json ->
            editor.putString(key ?: property.name, json)
        }
    }

    private fun serializeToJson(value: T?): String? {
        return Kotpref.moshi.let {
            if (it == null) throw IllegalStateException("Moshi has not been set to Kotpref")

            it.adapter(targetClass.java).toJson(value)
        }
    }

    private fun deserializeFromJson(json: String): T? {
        return Kotpref.moshi.let {
            if (it == null) throw IllegalStateException("Moshi has not been set to Kotpref")

            it.adapter(targetClass.java).fromJson(json)
        }
    }
}
