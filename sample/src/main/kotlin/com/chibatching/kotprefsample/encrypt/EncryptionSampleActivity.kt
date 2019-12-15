package com.chibatching.kotprefsample.encrypt

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.chibatching.kotprefsample.R
import kotlinx.android.synthetic.main.activity_encryption.*
import java.io.File

class EncryptionSampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_encryption)

        val pref = EncryptionSamplePref(this)

        editText.setText(pref.token)
        encryptedText.text = readSharedPrefFile(pref)

        saveButton.setOnClickListener {
            pref.token = editText.text.toString()
            editText.setText(pref.token)

            encryptedText.text = readSharedPrefFile(pref)
        }
    }

    private fun readSharedPrefFile(pref: EncryptionSamplePref): String =
        File(filesDir.parent, "shared_prefs/${pref.kotprefName}.xml").readText()
}
