package com.chibatching.kotprefsample

import com.chibatching.kotpref.KotprefModel
import java.util.*


object UserInfo : KotprefModel() {
    var gameLevel: GameLevel by enumValuePrefVar(GameLevel::class, GameLevel.NORMAL)
    var name: String by stringPrefVar()
    var code: String? by stringNullablePrefVar()
    var age: Int by intPrefVar()
    var highScore: Long by longPrefVar()
    var rate: Float by floatPrefVar()
    val prizes: MutableSet<String> by stringSetPrefVal {
        val set = TreeSet<String>()
        set.add("Beginner")
        return@stringSetPrefVal set
    }
}