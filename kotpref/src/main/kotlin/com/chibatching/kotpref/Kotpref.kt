package com.chibatching.kotpref

import android.content.Context

/**
 * Kotpref: SharedPreference delegation for Kotlin
 */
object Kotpref {

    /**
     * Initialize Kotpref singleton object.
     *
     * @param context Application context
     */
    fun init(context: Context) {
        StaticContextProvider.setContext(context.applicationContext)
    }

    /**
     * Return true if Kotpref singleton object is initialized via [init] function
     */
    val isInitialized
        get() = StaticContextProvider.isInitialized
}
