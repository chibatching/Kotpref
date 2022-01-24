package com.chibatching.kotprefsample.injectablecontext

import android.content.Context
import com.chibatching.kotpref.KotprefModel
import com.chibatching.kotpref.pref.stringPref

class InjectableContextSample(context: Context) : KotprefModel(context) {

    var sampleData by stringPref()
}
