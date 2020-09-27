package com.chibatching.kotpref

import android.content.Context

fun interface ContextProvider {
    fun getApplicationContext(): Context
}
