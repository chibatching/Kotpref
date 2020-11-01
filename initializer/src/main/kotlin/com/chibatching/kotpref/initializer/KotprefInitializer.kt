package com.chibatching.kotpref.initializer

import android.content.Context
import androidx.startup.Initializer
import com.chibatching.kotpref.Kotpref

public class KotprefInitializer : Initializer<Kotpref> {
    override fun create(context: Context): Kotpref {
        Kotpref.init(context)
        return Kotpref
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}
