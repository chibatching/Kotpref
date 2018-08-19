package com.chibatching.kotprefsample.injectablecontext

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.chibatching.kotprefsample.R
import kotlinx.android.synthetic.main.activity_injectable_context_sample.*

class InjectableContextSampleActivity : AppCompatActivity() {

    private val injectableContextData by lazy {
        InjectableContextSample(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_injectable_context_sample)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        saveButton.setOnClickListener {
            injectableContextData.sampleData = editText.text.toString()
            textView.text = injectableContextData.sampleData
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        if (super.onSupportNavigateUp()) {
            return true
        }
        finish()
        return true
    }
}
