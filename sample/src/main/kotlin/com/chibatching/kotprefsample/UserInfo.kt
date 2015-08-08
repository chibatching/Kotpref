package com.chibatching.kotprefsample

import com.chibatching.kotpref.*
import java.util.*


public object UserInfo : KotprefModel() {
    var name: String by stringPrefVar()
    var age: Int by intPrefVar()
    var highScore: Long by longPrefVar()
    var threshold: Float by floatPrefVar()
    val prizes: MutableSet<String> by stringSetPrefVal {
        val set = TreeSet<String>()
        set.add("Beginner")
        return@stringSetPrefVal set
    }
}