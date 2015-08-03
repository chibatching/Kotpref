package com.chibatching.kotpref

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import kotlin.properties.Delegates
import kotlin.properties.ReadWriteProperty

public object Kotpref {

    // context
    var context: Context? = null
        get() { if ($context != null) { return $context } else { throw IllegalStateException("Context has not yet set") }}
        private set

    /**
     * Initialize Kotpref singleton object
     */
    public fun init(context: Context) {
        this.context = context.getApplicationContext()
    }
}


public fun KotprefModel.stringPrefVar(default: String = "")
        : ReadWriteProperty<KotprefModel, String> = StringPrefVar(default)

public fun KotprefModel.intPrefVar(default: Int = 0)
        : ReadWriteProperty<KotprefModel, Int> = IntPrefVar(default)

public fun KotprefModel.longPrefVar(default: Long = 0L)
        : ReadWriteProperty<KotprefModel, Long> = LongPrefVar(default)

public fun KotprefModel.floatPrefVar(default: Float = 0F)
        : ReadWriteProperty<KotprefModel, Float> = FloatPrefVar(default)

public fun KotprefModel.booleanPrefVar(default: Boolean = false)
        : ReadWriteProperty<KotprefModel, Boolean> = BooleanPrefVar(default)

public fun KotprefModel.stringSetPrefVar(default: Set<String>)
        : ReadWriteProperty<KotprefModel, MutableSet<String>> = StringSetPrefVar(default)

