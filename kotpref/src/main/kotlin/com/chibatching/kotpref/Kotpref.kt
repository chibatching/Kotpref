package com.chibatching.kotpref

import android.annotation.SuppressLint
import android.content.Context

/**
 * Kotpref: SharedPreference delegation for Kotlin
 */
@SuppressLint("StaticFieldLeak")
object Kotpref {

    /**
     * Internal context. If context is not set, Kotpref will throw [IllegalStateException].
     */
    internal var context: Context? = null
        get() {
            return field ?: throw IllegalStateException("Kotpref has not been initialized.")
        }
        private set

    /**
     * Initialize Kotpref singleton object.
     *
     * @param context Application context
     */
    fun init(context: Context) {
        this.context = context.applicationContext
    }
}
