package com.chibatching.kotpref

import android.content.Context

public object Kotpref {

    // context
    var context: Context? = null
        get() = if ($context != null) { $context } else { throw IllegalStateException("Context has not yet set") }
        private set

    /**
     * Initialize Kotpref singleton object
     */
    public fun init(context: Context) {
        this.context = context.getApplicationContext()
    }
}