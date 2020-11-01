package com.chibatching.kotpref

import android.content.Context

/**
 * Kotpref: SharedPreference delegation for Kotlin
 */
public object Kotpref {

    /**
     * Initialize Kotpref singleton object.
     *
     * @param context Application context
     */
    public fun init(context: Context) {
        StaticContextProvider.setContext(context.applicationContext)
    }

    /**
     * Return true if Kotpref singleton object is initialized via [init] function
     */
    public val isInitialized: Boolean
        get() = StaticContextProvider.isInitialized
}
