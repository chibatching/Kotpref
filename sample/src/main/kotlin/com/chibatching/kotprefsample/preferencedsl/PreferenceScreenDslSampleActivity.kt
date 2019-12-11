package com.chibatching.kotprefsample.preferencedsl

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.chibatching.kotprefsample.R

class PreferenceScreenDslSampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preference_dsl_fragment_sample)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        if (super.onSupportNavigateUp()) {
            return true
        }
        finish()
        return true
    }
}
