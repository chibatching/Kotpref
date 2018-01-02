package com.chibatching.kotpref.enumpref

import com.chibatching.kotpref.KotprefModel
import kotlin.properties.ReadWriteProperty


/**
 * Delegate enum-based shared preferences property storing and recalling by the enum value's name as a string.
 * @param default default enum value
 * @param key custom preferences key
 */
inline fun <reified T : Enum<*>> KotprefModel.enumValuePref(default: T, key: String? = null, commitByDefault: Boolean = commitAllPropertiesByDefault)
        : ReadWriteProperty<KotprefModel, T> = EnumValuePref(T::class, default, key, commitByDefault)

/**
 * Delegate enum-based shared preferences property storing and recalling by the enum value's name as a string.
 * @param default default enum value
 * @param key custom preferences key resource id
 */
inline fun <reified T : Enum<*>> KotprefModel.enumValuePref(default: T, key: Int, commitByDefault: Boolean = commitAllPropertiesByDefault)
        : ReadWriteProperty<KotprefModel, T> = EnumValuePref(T::class, default, context.getString(key), commitByDefault)

/**
 * Delegate enum-based shared preferences property storing and recalling by the enum value's name as a string.
 * @param default default enum value
 * @param key custom preferences key
 */
inline fun <reified T : Enum<*>> KotprefModel.nullableEnumValuePref(default: T? = null, key: String? = null, commitByDefault: Boolean = commitAllPropertiesByDefault)
        : ReadWriteProperty<KotprefModel, T?> = EnumNullableValuePref(T::class, default, key, commitByDefault)

/**
 * Delegate enum-based shared preferences property storing and recalling by the enum value's name as a string.
 * @param default default enum value
 * @param key custom preferences key resource id
 */
inline fun <reified T : Enum<*>> KotprefModel.enumNullableValuePref(default: T? = null, key: Int, commitByDefault: Boolean = commitAllPropertiesByDefault)
        : ReadWriteProperty<KotprefModel, T?> = EnumNullableValuePref(T::class, default, context.getString(key), commitByDefault)

/**
 * Delegate enum-based shared preferences property storing and recalling by the enum value's ordinal as an integer.
 * @param default default enum value
 * @param key custom preferences key
 */
inline fun <reified T : Enum<*>> KotprefModel.enumOrdinalPref(default: T, key: String? = null, commitByDefault: Boolean = commitAllPropertiesByDefault)
        : ReadWriteProperty<KotprefModel, T> = EnumOrdinalPref(T::class, default, key, commitByDefault)

/**
 * Delegate enum-based shared preferences property storing and recalling by the enum value's ordinal as an integer.
 * @param default default enum value
 * @param key custom preferences key resource id
 */
inline fun <reified T : Enum<*>> KotprefModel.enumOrdinalPref(default: T, key: Int, commitByDefault: Boolean = commitAllPropertiesByDefault)
        : ReadWriteProperty<KotprefModel, T> = EnumOrdinalPref(T::class, default, context.getString(key), commitByDefault)
