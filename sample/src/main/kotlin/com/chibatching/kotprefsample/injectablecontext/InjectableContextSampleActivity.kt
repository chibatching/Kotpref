package com.chibatching.kotprefsample.injectablecontext

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.chibatching.kotprefsample.databinding.ActivityInjectableContextSampleBinding

class InjectableContextSampleActivity : AppCompatActivity() {

    private val injectableContextData by lazy {
        InjectableContextSample(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityInjectableContextSampleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.saveButton.setOnClickListener {
            injectableContextData.sampleData = binding.editText.text.toString()
            binding.textView.text = injectableContextData.sampleData
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
