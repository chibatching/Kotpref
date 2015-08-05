package com.chibatching.kotpref

import android.content.Context

/**
 * Kotpref: SharedPreference delegation for Kotlin
 */
public object Kotpref {

    /**
     * Internal context. If context is not set, Kotpref will throw [IllegalStateException].
     */
    var context: Context? = null
        get() {
            if ($context != null) {
                return $context
            } else {
                throw IllegalStateException("Kotpref has not been initialized.")
            }
        }
        private set

    /**
     * Initialize Kotpref singleton object
     *
     * @param context Application context
     */
    public fun init(context: Context) {
        this.context = context.getApplicationContext()
    }
}