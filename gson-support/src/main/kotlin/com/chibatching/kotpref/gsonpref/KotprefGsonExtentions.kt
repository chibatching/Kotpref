package com.chibatching.kotpref.gsonpref

import com.chibatching.kotpref.Kotpref
import com.chibatching.kotpref.KotprefModel
import com.chibatching.kotpref.pref.AbstractPref
import com.google.gson.Gson
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KClass


/**
 * Gson object to serialize and deserialize delegated property
 */
var Kotpref.gson: Gson?
    get() {
        return KotprefGsonHolder.gson
    }
    set(value) {
        KotprefGsonHolder.gson = value
    }

/**
 * Delegate shared preference property serialized and deserialized by gson
 * @param default default gson object value
 * @param key custom preference key
 */
inline fun <reified T : Any> KotprefModel.gsonPref(default: T, key: String? = null)
        : ReadWriteProperty<KotprefModel, T> = GsonPref(T::class, default, key)

/**
 * Delegate shared preference property serialized and deserialized by gson
 * @param default default gson object value
 * @param key custom preference key resource id
 */
inline fun <reified T : Any> KotprefModel.gsonPref(default: T, key: Int)
        : ReadWriteProperty<KotprefModel, T> = GsonPref(T::class, default, context.getString(key))

/**
 * Delegate shared preference property serialized and deserialized by gson
 * @param default default gson object value
 * @param key custom preference key
 */
inline fun <reified T : Any> KotprefModel.gsonNullablePref(default: T? = null, key: String? = null)
        : ReadWriteProperty<KotprefModel, T?> = GsonNullablePref(T::class, default, key)

/**
 * Delegate shared preference property serialized and deserialized by gson
 * @param default default gson object value
 * @param key custom preference key resource id
 */
inline fun <reified T : Any> KotprefModel.gsonNullablePref(default: T? = null, key: Int)
        : ReadWriteProperty<KotprefModel, T?> = GsonNullablePref(T::class, default, context.getString(key))


internal fun <T> AbstractPref<T>.serializeToJson(value: T?): String? {
    return Kotpref.gson.let {
        if (it == null) throw IllegalStateException("Gson has not been set to Kotpref")

        it.toJson(value)
    }
}

internal fun <T: Any> AbstractPref<T>.deserializeFromJson(json: String, targetClass: KClass<T>): T? {
    return Kotpref.gson.let {
        if (it == null) throw IllegalStateException("Gson has not been set to Kotpref")

        it.fromJson(json, targetClass.java)
    }
}
