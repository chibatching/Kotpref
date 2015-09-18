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
    fun init(context: Context) {
        this.context = context.applicationContext
    }

    fun <T : KotprefModel> bulk(receiver: T, f: T.() -> Unit) {
        receiver.beginBulkEdit()
        try {
            receiver.f()
        } catch (e: Exception) {
            receiver.cancelBulkEdit()
            throw e
        }
        receiver.commitBulkEdit()
    }
}