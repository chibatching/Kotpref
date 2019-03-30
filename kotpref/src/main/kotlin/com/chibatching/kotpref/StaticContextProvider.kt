package com.chibatching.kotpref

import android.content.Context

internal object StaticContextProvider : ContextProvider {

    private var staticContext: Context? = null

    override fun getApplicationContext(): Context {
        return staticContext
            ?: throw IllegalStateException("StaticContextProvider has not been initialized.")
    }

    fun setContext(context: Context) {
        staticContext = context.applicationContext
    }
}
