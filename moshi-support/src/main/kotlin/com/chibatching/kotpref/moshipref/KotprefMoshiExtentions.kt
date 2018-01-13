package com.chibatching.kotpref.moshipref

import com.chibatching.kotpref.Kotpref
import com.chibatching.kotpref.KotprefModel
import com.squareup.moshi.Moshi
import kotlin.properties.ReadWriteProperty


/**
 * Moshi object to serialize and deserialize delegated property
 */
var Kotpref.moshi: Moshi?
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
inline fun <reified T : Any> KotprefModel.moshiPref(default: T, key: String? = null, commitByDefault: Boolean = commitAllPropertiesByDefault)
        : ReadWriteProperty<KotprefModel, T> = MoshiPref(T::class, default, key, commitByDefault)

/**
 * Delegate shared preferences property serialized and deserialized by moshi
 * @param default default moshi object value
 * @param key custom preferences key resource id
 */
inline fun <reified T : Any> KotprefModel.moshiPref(default: T, key: Int, commitByDefault: Boolean = commitAllPropertiesByDefault)
        : ReadWriteProperty<KotprefModel, T> = MoshiPref(T::class, default, context.getString(key), commitByDefault)

/**
 * Delegate shared preferences property serialized and deserialized by moshi
 * @param default default moshi object value
 * @param key custom preferences key
 */
inline fun <reified T : Any> KotprefModel.moshiNullablePref(default: T? = null, key: String? = null, commitByDefault: Boolean = commitAllPropertiesByDefault)
        : ReadWriteProperty<KotprefModel, T?> = MoshiNullablePref(T::class, default, key, commitByDefault)

/**
 * Delegate shared preferences property serialized and deserialized by moshi
 * @param default default moshi object value
 * @param key custom preferences key resource id
 */
inline fun <reified T : Any> KotprefModel.moshiNullablePref(default: T? = null, key: Int, commitByDefault: Boolean = commitAllPropertiesByDefault)
        : ReadWriteProperty<KotprefModel, T?> = MoshiNullablePref(T::class, default, context.getString(key), commitByDefault)
