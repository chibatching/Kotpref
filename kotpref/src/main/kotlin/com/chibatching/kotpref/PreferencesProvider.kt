package com.chibatching.kotpref

import android.content.Context
import android.content.SharedPreferences

public fun interface PreferencesProvider {
    public fun get(context: Context, name: String, mode: Int): SharedPreferences
}

internal fun defaultPreferenceProvider(): PreferencesProvider = DefaultPreferencesProvider

private object DefaultPreferencesProvider : PreferencesProvider {
    override fun get(context: Context, name: String, mode: Int): SharedPreferences {
        return context.getSharedPreferences(name, mode)
    }
}
