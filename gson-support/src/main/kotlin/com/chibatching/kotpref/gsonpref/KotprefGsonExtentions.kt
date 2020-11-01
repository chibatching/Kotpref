package com.chibatching.kotpref.gsonpref

import com.chibatching.kotpref.Kotpref
import com.chibatching.kotpref.KotprefModel
import com.chibatching.kotpref.pref.AbstractPref
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Gson object to serialize and deserialize delegated property
 */
public var Kotpref.gson: Gson?
    get() {
        return KotprefGsonHolder.gson
    }
    set(value) {
        KotprefGsonHolder.gson = value
    }

/**
 * Delegate shared preferences property serialized and deserialized by gson
 * @param default default gson object value
 * @param key custom preferences key
 */
public inline fun <reified T : Any> KotprefModel.gsonPref(
    default: T,
    key: String? = null,
    commitByDefault: Boolean = commitAllPropertiesByDefault
): AbstractPref<T> = GsonPref(object : TypeToken<T>() {}.type, default, key, commitByDefault)

/**
 * Delegate shared preferences property serialized and deserialized by gson
 * @param default default gson object value provider function
 * @param key custom preferences key
 */
public inline fun <reified T : Any> KotprefModel.gsonPref(
    key: String? = null,
    commitByDefault: Boolean = commitAllPropertiesByDefault,
    noinline default: () -> T
): AbstractPref<T> = GsonPref(object : TypeToken<T>() {}.type, default, key, commitByDefault)

/**
 * Delegate shared preferences property serialized and deserialized by gson
 * @param default default gson object value
 * @param key custom preferences key resource id
 */
public inline fun <reified T : Any> KotprefModel.gsonPref(
    default: T,
    key: Int,
    commitByDefault: Boolean = commitAllPropertiesByDefault
): AbstractPref<T> =
    GsonPref(object : TypeToken<T>() {}.type, default, context.getString(key), commitByDefault)

/**
 * Delegate shared preferences property serialized and deserialized by gson
 * @param default default gson object value provider function
 * @param key custom preferences key resource id
 */
public inline fun <reified T : Any> KotprefModel.gsonPref(
    key: Int,
    commitByDefault: Boolean = commitAllPropertiesByDefault,
    noinline default: () -> T
): AbstractPref<T> =
    GsonPref(object : TypeToken<T>() {}.type, default, context.getString(key), commitByDefault)

/**
 * Delegate shared preferences property serialized and deserialized by gson
 * @param default default gson object value
 * @param key custom preferences key
 */
public inline fun <reified T : Any> KotprefModel.gsonNullablePref(
    default: T? = null,
    key: String? = null,
    commitByDefault: Boolean = commitAllPropertiesByDefault
): AbstractPref<T?> =
    GsonNullablePref(object : TypeToken<T>() {}.type, default, key, commitByDefault)

/**
 * Delegate shared preferences property serialized and deserialized by gson
 * @param default default gson object value provider function
 * @param key custom preferences key
 */
public inline fun <reified T : Any> KotprefModel.gsonNullablePref(
    key: String? = null,
    commitByDefault: Boolean = commitAllPropertiesByDefault,
    noinline default: (() -> T?)
): AbstractPref<T?> =
    GsonNullablePref(object : TypeToken<T>() {}.type, default, key, commitByDefault)

/**
 * Delegate shared preferences property serialized and deserialized by gson
 * @param default default gson object value
 * @param key custom preferences key resource id
 */
public inline fun <reified T : Any> KotprefModel.gsonNullablePref(
    default: T? = null,
    key: Int,
    commitByDefault: Boolean = commitAllPropertiesByDefault
): AbstractPref<T?> = GsonNullablePref(
    object : TypeToken<T>() {}.type,
    default,
    context.getString(key),
    commitByDefault
)

/**
 * Delegate shared preferences property serialized and deserialized by gson
 * @param default default gson object value provider function
 * @param key custom preferences key resource id
 */
public inline fun <reified T : Any> KotprefModel.gsonNullablePref(
    key: Int,
    commitByDefault: Boolean = commitAllPropertiesByDefault,
    noinline default: (() -> T?)
): AbstractPref<T?> = GsonNullablePref(
    object : TypeToken<T>() {}.type,
    default,
    context.getString(key),
    commitByDefault
)
