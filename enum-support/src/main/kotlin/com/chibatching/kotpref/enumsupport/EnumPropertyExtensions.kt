package com.chibatching.kotpref.enumsupport

import com.chibatching.kotpref.KotprefModel
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KClass


/**
 * Delegate enum-based shared preference property storing and recalling by the enum value's name as a string.
 * @param enumClass Enum class to define the property
 * @param default default enum value
 * @param key custom preference key
 */
fun <T : Enum<*>> KotprefModel.enumValuePref(enumClass: KClass<T>, default: T, key: String? = null)
        : ReadWriteProperty<KotprefModel, T> = EnumValuePref(enumClass, default, key)

/**
 * Delegate enum-based shared preference property storing and recalling by the enum value's name as a string.
 * @param enumClass Enum class to define the property
 * @param default default enum value
 * @param key custom preference key resource id
 */
fun <T : Enum<*>> KotprefModel.enumValuePref(enumClass: KClass<T>, default: T, key: Int)
        : ReadWriteProperty<KotprefModel, T> = EnumValuePref(enumClass, default, context.getString(key))

/**
 * Delegate enum-based shared preference property storing and recalling by the enum value's name as a string.
 * @param enumClass Enum class to define the property
 * @param default default enum value
 * @param key custom preference key
 */
fun <T : Enum<*>> KotprefModel.nullableEnumValuePref(enumClass: KClass<T>, default: T? = null, key: String? = null)
        : ReadWriteProperty<KotprefModel, T?> = EnumNullableValuePref(enumClass, default, key)

/**
 * Delegate enum-based shared preference property storing and recalling by the enum value's name as a string.
 * @param enumClass Enum class to define the property
 * @param default default enum value
 * @param key custom preference key resource id
 */
fun <T : Enum<*>> KotprefModel.enumNullableValuePref(enumClass: KClass<T>, default: T? = null, key: Int)
        : ReadWriteProperty<KotprefModel, T?> = EnumNullableValuePref(enumClass, default, context.getString(key))

/**
 * Delegate enum-based shared preference property storing and recalling by the enum value's ordinal as an integer.
 * @param enumClass Enum class to define the property
 * @param default default enum value
 * @param key custom preference key
 */
fun <T : Enum<*>> KotprefModel.enumOrdinalPref(enumClass: KClass<T>, default: T, key: String? = null)
        : ReadWriteProperty<KotprefModel, T> = EnumOrdinalPref(enumClass, default, key)

/**
 * Delegate enum-based shared preference property storing and recalling by the enum value's ordinal as an integer.
 * @param enumClass Enum class to define the property
 * @param default default enum value
 * @param key custom preference key resource id
 */
fun <T : Enum<*>> KotprefModel.enumOrdinalPref(enumClass: KClass<T>, default: T, key: Int)
        : ReadWriteProperty<KotprefModel, T> = EnumOrdinalPref(enumClass, default, context.getString(key))
