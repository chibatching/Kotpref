package com.chibatching.kotpref

import android.content.Context
import android.content.SharedPreferences

interface PreferencesProvider {
    fun get(context: Context, name: String, mode: Int): SharedPreferences
}

@Deprecated(
    "PreferencesOpener was renamed to PreferencesProvider.",
    ReplaceWith("PreferencesProvider")
)
interface PreferencesOpener : PreferencesProvider {

    override fun get(context: Context, name: String, mode: Int): SharedPreferences {
        return openPreferences(context, name, mode)
    }

    fun openPreferences(context: Context, name: String, mode: Int): SharedPreferences
}

internal fun defaultPreferenceProvider(): PreferencesProvider = DefaultPreferencesProvider

private object DefaultPreferencesProvider : PreferencesProvider {
    override fun get(context: Context, name: String, mode: Int): SharedPreferences {
        return context.getSharedPreferences(name, mode)
    }
}
