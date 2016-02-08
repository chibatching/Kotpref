package com.chibatching.kotprefsample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.chibatching.kotpref.Kotpref

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Kotpref.init(this)

        Log.d(javaClass.simpleName, "User name: ${UserInfo.name}")
        Log.d(javaClass.simpleName, "User age: ${UserInfo.age}")
        Log.d(javaClass.simpleName, "User high score: ${UserInfo.highScore}")
        Log.d(javaClass.simpleName, "User rate: ${UserInfo.rate}")
        UserInfo.prizes.forEachIndexed { i, s -> Log.d(javaClass.simpleName, "prize[$i]: $s") }

        UserInfo.name = "chibatching"
        UserInfo.age = 30
        UserInfo.highScore = 49219902
        UserInfo.rate = 49.21F
        UserInfo.prizes.add("Bronze")
        UserInfo.prizes.add("Silver")
        UserInfo.prizes.add("Gold")

        Log.d(javaClass.simpleName, "User name: ${UserInfo.name}")
        Log.d(javaClass.simpleName, "User age: ${UserInfo.age}")
        Log.d(javaClass.simpleName, "User high score: ${UserInfo.highScore}")
        Log.d(javaClass.simpleName, "User rate: ${UserInfo.rate}")
        UserInfo.prizes.forEachIndexed { i, s -> Log.d(javaClass.simpleName, "prize[$i]: $s") }

        Kotpref.bulk(UserInfo) {
            name = "chibatching Jr"
            age = 2
            highScore = 3901
            rate = 0.4F
            prizes.clear()
            prizes.add("New Born")
        }

        Log.d(javaClass.simpleName, "User name: ${UserInfo.name}")
        Log.d(javaClass.simpleName, "User age: ${UserInfo.age}")
        Log.d(javaClass.simpleName, "User high score: ${UserInfo.highScore}")
        Log.d(javaClass.simpleName, "User rate: ${UserInfo.rate}")
        UserInfo.prizes.forEachIndexed { i, s -> Log.d(javaClass.simpleName, "prize[$i]: $s") }
    }
}
