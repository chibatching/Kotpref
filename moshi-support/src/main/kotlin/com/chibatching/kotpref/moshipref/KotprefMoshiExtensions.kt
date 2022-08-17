package com.chibatching.kotpref.moshipref

import com.chibatching.kotpref.Kotpref
import com.chibatching.kotpref.KotprefModel
import com.chibatching.kotpref.pref.AbstractPref
import com.squareup.moshi.Moshi

/**
 *  Moshi object to serialize and deserialize delegated property
 */
public var Kotpref.moshi: Moshi?
    get() {
        return KotprefMoshiHolder.moshi
    }
    set(value) {
        KotprefMoshiHolder.moshi = value
    }

/**
 * Delegate shared preferences property serialized and deserialized by moshi
 * @param default default moshi object value
 * @param key custom preferences key
 */
public inline fun <reified T : Any> KotprefModel.moshiPref(
    default: T,
    key: String? = null,
    commitByDefault: Boolean = commitAllPropertiesByDefault
): AbstractPref<T> = MoshiPref(T::class.java, default, key, commitByDefault)

/**
 * Delegate shared preferences property serialized and deserialized by moshi
 * @param default default moshi object value provider function
 * @param key custom preferences key
 */
public inline fun <reified T : Any> KotprefModel.moshiPref(
    key: String? = null,
    commitByDefault: Boolean = commitAllPropertiesByDefault,
    noinline default: () -> T
): AbstractPref<T> = MoshiPref(T::class.java, default, key, commitByDefault)

/**
 * Delegate shared preferences property serialized and deserialized by moshi
 * @param default default moshi object value
 * @param key custom preferences key resource id
 */
public inline fun <reified T : Any> KotprefModel.moshiPref(
    default: T,
    key: Int,
    commitByDefault: Boolean = commitAllPropertiesByDefault
): AbstractPref<T> =
    MoshiPref(T::class.java, default, context.getString(key), commitByDefault)

/**
 * Delegate shared preferences property serialized and deserialized by moshi
 * @param default default moshi object value provider function
 * @param key custom preferences key resource id
 */
public inline fun <reified T : Any> KotprefModel.moshiPref(
    key: Int,
    commitByDefault: Boolean = commitAllPropertiesByDefault,
    noinline default: () -> T
): AbstractPref<T> =
    MoshiPref(T::class.java, default, context.getString(key), commitByDefault)

/**
 * Delegate shared preferences property serialized and deserialized by moshi
 * @param default default moshi object value
 * @param key custom preferences key
 */
public inline fun <reified T : Any> KotprefModel.moshiNullablePref(
    default: T? = null,
    key: String? = null,
    commitByDefault: Boolean = commitAllPropertiesByDefault
): AbstractPref<T?> =
    MoshiNullablePref(T::class.java, default, key, commitByDefault)

/**
 * Delegate shared preferences property serialized and deserialized by moshi
 * @param default default moshi object value provider function
 * @param key custom preferences key
 */
public inline fun <reified T : Any> KotprefModel.moshiNullablePref(
    key: String? = null,
    commitByDefault: Boolean = commitAllPropertiesByDefault,
    noinline default: (() -> T?)
): AbstractPref<T?> =
    MoshiNullablePref(T::class.java, default, key, commitByDefault)

/**
 * Delegate shared preferences property serialized and deserialized by moshi
 * @param default default moshi object value
 * @param key custom preferences key resource id
 */
public inline fun <reified T : Any> KotprefModel.moshiNullablePref(
    default: T? = null,
    key: Int,
    commitByDefault: Boolean = commitAllPropertiesByDefault
): AbstractPref<T?> = MoshiNullablePref(
    T::class.java,
    default,
    context.getString(key),
    commitByDefault
)

/**
 * Delegate shared preferences property serialized and deserialized by moshi
 * @param default default moshi object value provider function
 * @param key custom preferences key resource id
 */
public inline fun <reified T : Any> KotprefModel.moshiNullablePref(
    key: Int,
    commitByDefault: Boolean = commitAllPropertiesByDefault,
    noinline default: (() -> T?)
): AbstractPref<T?> = MoshiNullablePref(
    T::class.java,
    default,
    context.getString(key),
    commitByDefault
)
