package com.chibatching.kotpref

import android.content.Context
import android.content.SharedPreferences

interface PreferencesOpener {
    fun openPreferences(context: Context, name: String, mode: Int): SharedPreferences
}

internal fun defaultPreferenceOpener(): PreferencesOpener = DefaultOpener()

private class DefaultOpener : PreferencesOpener {
    override fun openPreferences(context: Context, name: String, mode: Int): SharedPreferences {
        return context.getSharedPreferences(name, mode)
    }
}