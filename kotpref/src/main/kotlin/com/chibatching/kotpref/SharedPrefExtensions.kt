package com.chibatching.kotpref

import android.content.SharedPreferences


/**
 * Extension to choose between {@link SharedPreferences.Editor.commit} and {@link SharedPreferences.Editor.apply}
 * @param synchronous save to sharedPref file instantly
 */
fun SharedPreferences.Editor.execute(synchronous: Boolean) {
    if (synchronous) commit() else apply()
}
