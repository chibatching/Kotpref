package com.chibatching.kotprefsample.preferencesupport

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.chibatching.kotprefsample.R

class PreferenceSupportSampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preference_support_fragment_sample)
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
