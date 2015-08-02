package com.chibatching.kotprefsample

import android.content.Context
import com.chibatching.kotpref.Kotpref


public object AppPreferences {

    // context
    var context: Context? = null
        set(c: Context?) { $context = c?.getApplicationContext() }
        get() { if ($context != null) { return $context } else { throw IllegalStateException("Context has not yet initialized") }}

    // User information
    var userName: String by Kotpref.stringPrefVar({ context!! }, preferenceName = "userInfo")
    var userNickName: String by Kotpref.stringPrefVar({ context!! }, preferenceName = "userInfo")
    var userAge: Int by Kotpref.intPrefVar({ context!! }, preferenceName = "userInfo")

    // App settings
    var enableFunc1: Boolean by Kotpref.booleanPrefVar({ context!! }, default = true, preferenceName = "appSetting")
    var threshold: Long by Kotpref.longPrefVar({ context!! }, default = -1, preferenceName = "appSetting")
}
