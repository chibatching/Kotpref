package com.chibatching.kotprefsample.encrypt

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.chibatching.kotpref.KotprefModel
import com.chibatching.kotpref.PreferencesProvider
import com.chibatching.kotpref.pref.stringPref

class EncryptionSamplePref(
    context: Context
) : KotprefModel(context, preferencesProvider) {

    companion object {
        private var encryptedSharedPreference: SharedPreferences? = null
        private val preferencesProvider = PreferencesProvider { context, name, mode ->
            encryptedSharedPreference ?: run {
                // EncryptedSharedPreferences supports above SDK 23
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    context.getSharedPreferences(name, mode)
                } else {
                    val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
                    EncryptedSharedPreferences.create(
                        name,
                        masterKeyAlias,
                        context,
                        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                    )
                }.also {
                    encryptedSharedPreference = it
                }
            }
        }
    }

    var token by stringPref()
}
