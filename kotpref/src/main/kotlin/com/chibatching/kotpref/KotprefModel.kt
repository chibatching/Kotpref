package com.chibatching.kotpref

import android.content.Context
import android.content.SharedPreferences
import kotlin.properties.Delegates
import kotlin.properties.ReadWriteProperty


public open class KotprefModel() {
    /**
     * Preference file name
     */
    open val kotprefName: String = javaClass.getSimpleName()
    /**
     * Preference read/write mode
     */
    open val kotprefMode: Int = Context.MODE_PRIVATE
    /**
     * Internal shared preference.
     * This property will be initialized on use.
     */
    val kotprefPreference: SharedPreferences by Delegates.lazy {
        Kotpref.context!!.getSharedPreferences(kotprefName, kotprefMode)
    }

    public fun clear() {
        kotprefPreference.edit().clear().apply()
    }

    /**
     * Delegate string shared preference property.
     * @param default default string value
     */
    protected fun stringPrefVar(default: String = "")
            : ReadWriteProperty<KotprefModel, String> = StringPrefVar(default)

    /**
     * Delegate Int shared preference property.
     * @param default default int value
     */
    protected fun intPrefVar(default: Int = 0)
            : ReadWriteProperty<KotprefModel, Int> = IntPrefVar(default)

    /**
     * Delegate long shared preference property.
     * @param default default long value
     */
    protected fun longPrefVar(default: Long = 0L)
            : ReadWriteProperty<KotprefModel, Long> = LongPrefVar(default)

    /**
     * Delegate float shared preference property.
     * @param default default float value
     */
    protected fun floatPrefVar(default: Float = 0F)
            : ReadWriteProperty<KotprefModel, Float> = FloatPrefVar(default)

    /**
     * Delegate boolean shared preference property.
     * @param default default boolean value
     */
    protected fun booleanPrefVar(default: Boolean = false)
            : ReadWriteProperty<KotprefModel, Boolean> = BooleanPrefVar(default)

    /**
     * Delegate string set shared preference property.
     * @param default default string set value
     */
    protected fun stringSetPrefVar(default: Set<String>)
            : ReadWriteProperty<KotprefModel, MutableSet<String>> = StringSetPrefVar(default)
}
