package com.chibatching.kotpref

import android.content.Context

public fun interface ContextProvider {
    public fun getApplicationContext(): Context
}
