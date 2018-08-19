package com.chibatching.kotprefsample.injectablecontext

import android.content.Context
import com.chibatching.kotpref.KotprefModel

class InjectableContextSample(context: Context) : KotprefModel(context) {

    var sampleData by stringPref()
}
