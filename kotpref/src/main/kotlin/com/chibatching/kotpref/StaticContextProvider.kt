package com.chibatching.kotpref

import android.content.Context

internal object StaticContextProvider : ContextProvider {

    private var staticContext: Context? = null

    override fun getApplicationContext(): Context {
        return staticContext
            ?: throw IllegalStateException("Kotpref has not been initialized.")
    }

    fun setContext(context: Context) {
        staticContext = context.applicationContext
    }

    val isInitialized
        get() = staticContext != null
}
