package com.chibatching.kotpref.enumpref

import com.chibatching.kotpref.KotprefModel
import kotlin.properties.ReadWriteProperty


/**
 * Delegate enum-based shared preference property storing and recalling by the enum value's name as a string.
 * @param default default enum value
 * @param key custom preference key
 */
inline fun <reified T : Enum<*>> KotprefModel.enumValuePref(default: T, key: String? = null)
        : ReadWriteProperty<KotprefModel, T> = EnumValuePref(T::class, default, key)

/**
 * Delegate enum-based shared preference property storing and recalling by the enum value's name as a string.
 * @param default default enum value
 * @param key custom preference key resource id
 */
inline fun <reified T : Enum<*>> KotprefModel.enumValuePref(default: T, key: Int)
        : ReadWriteProperty<KotprefModel, T> = EnumValuePref(T::class, default, context.getString(key))

/**
 * Delegate enum-based shared preference property storing and recalling by the enum value's name as a string.
 * @param default default enum value
 * @param key custom preference key
 */
inline fun <reified T : Enum<*>> KotprefModel.nullableEnumValuePref(default: T? = null, key: String? = null)
        : ReadWriteProperty<KotprefModel, T?> = EnumNullableValuePref(T::class, default, key)

/**
 * Delegate enum-based shared preference property storing and recalling by the enum value's name as a string.
 * @param default default enum value
 * @param key custom preference key resource id
 */
inline fun <reified T : Enum<*>> KotprefModel.enumNullableValuePref(default: T? = null, key: Int)
        : ReadWriteProperty<KotprefModel, T?> = EnumNullableValuePref(T::class, default, context.getString(key))

/**
 * Delegate enum-based shared preference property storing and recalling by the enum value's ordinal as an integer.
 * @param default default enum value
 * @param key custom preference key
 */
inline fun <reified T : Enum<*>> KotprefModel.enumOrdinalPref(default: T, key: String? = null)
        : ReadWriteProperty<KotprefModel, T> = EnumOrdinalPref(T::class, default, key)

/**
 * Delegate enum-based shared preference property storing and recalling by the enum value's ordinal as an integer.
 * @param default default enum value
 * @param key custom preference key resource id
 */
inline fun <reified T : Enum<*>> KotprefModel.enumOrdinalPref(default: T, key: Int)
        : ReadWriteProperty<KotprefModel, T> = EnumOrdinalPref(T::class, default, context.getString(key))
