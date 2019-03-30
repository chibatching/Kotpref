package com.chibatching.kotprefsample

import android.app.Application
import com.chibatching.kotpref.Kotpref
import com.chibatching.kotpref.gsonpref.gson
import com.google.gson.Gson

class SampleApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // If you don't use kotpref auto initializer, you should init kotpref.
        // Kotpref.init(this)

        // For gson support module
        Kotpref.gson = Gson()
    }
}
