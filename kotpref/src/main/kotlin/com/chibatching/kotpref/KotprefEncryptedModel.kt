package com.chibatching.kotpref

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.SystemClock
import androidx.annotation.RequiresApi
import com.chibatching.kotpref.pref.*
import java.lang.Exception
import java.security.SecureRandom
import java.security.Security
import java.util.LinkedHashSet
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty

@RequiresApi(Build.VERSION_CODES.KITKAT)
abstract class KotprefEncriptedModel(
    private val contextProvider: ContextProvider = StaticContextProvider
) : KotprefModel(contextProvider) {

    constructor(context: Context) : this(object : ContextProvider {
        override fun getApplicationContext(): Context {
            return context.applicationContext
        }
    })

    @SuppressLint("CommitPrefEdits")
    override fun beginBulkEdit() {
        kotprefInTransaction = true
        kotprefTransactionStartTime = SystemClock.uptimeMillis()
        kotprefEditor =
                (kotprefPreference as KotprefEncryptedPreferences).edit() as KotprefPreferences.KotprefEditor
    }

    /**
     * Internal shared preferences.
     * This property will be initialized on use.
     */
    override val kotprefPreference: KotprefPreferences by lazy {
        val sharedPreferences = context.getSharedPreferences(kotprefName, kotprefMode)
        KotprefEncryptedPreferences(sharedPreferences, cryptoSession)
    }

    private val cryptoSession: CryptoSession by lazy {
        val androidKeyStore = "AndroidKeyStore"
        val s = kotprefName + androidKeyStore
        val iVector = String(s.toByteArray().slice(0..11).toByteArray())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            CryptoSessionM(kotprefName, androidKeyStore, iVector)
        } else {
            CryptoSessionPreM(context, kotprefName, androidKeyStore)
        }
    }
    /**
     * SharedPreferences instance exposed.
     * Use carefully when during bulk edit, it may cause inconsistent with internal data of Kotpref.
     */
    val preferences: SharedPreferences
        get() = kotprefPreference.preferences


}

