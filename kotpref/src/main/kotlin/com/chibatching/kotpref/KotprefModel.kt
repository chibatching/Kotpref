package com.chibatching.kotpref

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import kotlin.properties.Delegates


public open class KotprefModel() {

    open val kotprefName: String = javaClass.getSimpleName()
    open val kotprefMode: Int = Context.MODE_PRIVATE

    val kotprefPreference: SharedPreferences by Delegates.lazy {
        Kotpref.context!!.getSharedPreferences(kotprefName, kotprefMode)
    }

    public fun clear() {
        kotprefPreference.edit().clear().apply()
    }
}
