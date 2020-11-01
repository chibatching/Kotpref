package com.chibatching.kotpref

import android.content.SharedPreferences

/**
 * Extension to choose between [SharedPreferences.Editor.commit] and [SharedPreferences.Editor.apply]
 * @param synchronous save to sharedPref file instantly
 */
public fun SharedPreferences.Editor.execute(synchronous: Boolean) {
    if (synchronous) commit() else apply()
}
