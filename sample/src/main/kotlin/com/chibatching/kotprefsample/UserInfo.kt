package com.chibatching.kotprefsample

import com.chibatching.kotpref.KotprefModel
import java.util.*


object UserInfo : KotprefModel() {
    var gameLevel: GameLevel by enumValuePref(GameLevel::class, GameLevel.NORMAL)
    var name: String by stringPref()
    var code: String? by nullableStringPref()
    var age: Int by intPref()
    var highScore: Long by longPref()
    var rate: Float by floatPref()
    val prizes: MutableSet<String> by stringSetPref {
        val set = TreeSet<String>()
        set.add("Beginner")
        return@stringSetPref set
    }
}
