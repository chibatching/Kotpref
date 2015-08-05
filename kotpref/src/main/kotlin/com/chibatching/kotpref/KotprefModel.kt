package com.chibatching.kotpref

import android.content.Context
import android.content.SharedPreferences
import kotlin.properties.Delegates
import kotlin.properties.ReadWriteProperty


public open class KotprefModel() {

    open val kotprefName: String = javaClass.getSimpleName()
    open val kotprefMode: Int = Context.MODE_PRIVATE

    val kotprefPreference: SharedPreferences by Delegates.lazy {
        Kotpref.context!!.getSharedPreferences(kotprefName, kotprefMode)
    }

    public fun clear() {
        kotprefPreference.edit().clear().apply()
    }

    protected fun stringPrefVar(default: String = "")
            : ReadWriteProperty<KotprefModel, String> = StringPrefVar(default)

    protected fun intPrefVar(default: Int = 0)
            : ReadWriteProperty<KotprefModel, Int> = IntPrefVar(default)

    protected fun longPrefVar(default: Long = 0L)
            : ReadWriteProperty<KotprefModel, Long> = LongPrefVar(default)

    protected fun floatPrefVar(default: Float = 0F)
            : ReadWriteProperty<KotprefModel, Float> = FloatPrefVar(default)

    protected fun booleanPrefVar(default: Boolean = false)
            : ReadWriteProperty<KotprefModel, Boolean> = BooleanPrefVar(default)

    protected fun stringSetPrefVar(default: Set<String>)
            : ReadWriteProperty<KotprefModel, MutableSet<String>> = StringSetPrefVar(default)
}
