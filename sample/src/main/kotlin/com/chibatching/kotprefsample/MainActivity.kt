package com.chibatching.kotprefsample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.chibatching.kotpref.Kotpref
import java.util.TreeSet

public class MainActivity : AppCompatActivity() {

    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Kotpref.init(this)

        Log.d(javaClass.getSimpleName(), "User name: ${UserInfo.name}")
        Log.d(javaClass.getSimpleName(), "User age: ${UserInfo.age}")
        Log.d(javaClass.getSimpleName(), "User high score: ${UserInfo.highScore}")
        Log.d(javaClass.getSimpleName(), "User rate: ${UserInfo.rate}")
        UserInfo.prizes.forEachIndexed { i, s -> Log.d(javaClass.getSimpleName(), "prize[$i]: ${s}") }

        UserInfo.name = "chibatching"
        UserInfo.age = 30
        UserInfo.highScore = 49219902
        UserInfo.rate = 49.21F
        UserInfo.prizes.add("Bronze")
        UserInfo.prizes.add("Silver")
        UserInfo.prizes.add("Gold")

        Log.d(javaClass.getSimpleName(), "User name: ${UserInfo.name}")
        Log.d(javaClass.getSimpleName(), "User age: ${UserInfo.age}")
        Log.d(javaClass.getSimpleName(), "User high score: ${UserInfo.highScore}")
        Log.d(javaClass.getSimpleName(), "User rate: ${UserInfo.rate}")
        UserInfo.prizes.forEachIndexed { i, s -> Log.d(javaClass.getSimpleName(), "prize[$i]: ${s}") }
    }
}
