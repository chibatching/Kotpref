package com.chibatching.kotpref

import android.content.Context

interface ContextProvider {
    fun getApplicationContext(): Context
}
