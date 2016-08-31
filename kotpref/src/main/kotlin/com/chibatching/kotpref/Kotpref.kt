package com.chibatching.kotpref

import android.content.Context

/**
 * Kotpref: SharedPreference delegation for Kotlin
 */
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
     * 1.4.0 or later, calling this function is not necessary to initialize kotpref.
     *
     * @param context Application context
     */
    fun init(context: Context) {
        this.context = context.applicationContext
    }

    @Deprecated(
            message = "",
            replaceWith = ReplaceWith("receiver.bulk(f)")
    )
    fun <T : KotprefModel> bulk(receiver: T, f: T.() -> Unit) {
        receiver.bulk(f)
    }
}
