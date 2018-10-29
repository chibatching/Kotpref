package com.chibatching.kotpref

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import androidx.annotation.RequiresApi
import java.nio.charset.Charset
import java.security.KeyStore
import java.security.SecureRandom
import java.util.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec


@RequiresApi(api = Build.VERSION_CODES.M)
class CryptoSessionM(
    val keyAlias: String,
    val androidKeyStore: String,
    val iVector: String = "123456789012"
) : CryptoSession {
    private val GCM_IV_LENGTH = 12
    private val GCM_TAG_LENGTH = 16

    private val AESGCMNOPADDING = "AES/GCM/NoPadding"

    private lateinit var keyStore: KeyStore

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Throws(Exception::class)
    private fun generatorKey(alias: String, androidKeyStore: String): SecretKey {
        val keyGenerator =
            KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, androidKeyStore)
        keyGenerator.init(
            KeyGenParameterSpec.Builder(
                alias,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM).setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setRandomizedEncryptionRequired(false)
                .build()
        )
        return keyGenerator.generateKey()
    }

    @Throws(Exception::class)
    private fun getSecretKey(alias: String): SecretKey {
        keyStore = KeyStore.getInstance(androidKeyStore)
        keyStore.load(null)
        return if (keyStore.containsAlias(alias)) {
            val secretKeyEntry = keyStore.getEntry(alias, null) as KeyStore.SecretKeyEntry
            secretKeyEntry.secretKey
        } else {
            generatorKey(alias, androidKeyStore)
        }
    }

    @Throws(Exception::class)
    private fun encrypt(privateString: String, skey: SecretKey): String {
        val iv = ByteArray(GCM_IV_LENGTH)
        SecureRandom().nextBytes(iv)
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val ivSpec = GCMParameterSpec(GCM_TAG_LENGTH * java.lang.Byte.SIZE, iv)
        cipher.init(Cipher.ENCRYPT_MODE, skey, ivSpec)
        val ciphertext = cipher.doFinal(privateString.toByteArray())
        val encrypted = ByteArray(iv.size + ciphertext.size)
        System.arraycopy(iv, 0, encrypted, 0, iv.size)
        System.arraycopy(ciphertext, 0, encrypted, iv.size, ciphertext.size)
        return Base64.encodeToString(encrypted, Base64.DEFAULT)
    }

    @Throws(Exception::class)
    private fun decrypt(encrypted: String, skey: SecretKey): String {
        val decoded = Base64.decode(encrypted, Base64.DEFAULT)
        val iv = Arrays.copyOfRange(decoded, 0, GCM_IV_LENGTH)
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val ivSpec = GCMParameterSpec(GCM_TAG_LENGTH * java.lang.Byte.SIZE, iv)
        cipher.init(Cipher.DECRYPT_MODE, skey, ivSpec)
        val ciphertext = cipher.doFinal(decoded, GCM_IV_LENGTH, decoded.size - GCM_IV_LENGTH)
        return String(ciphertext)
    }

    override fun encryptData(data: String): String {
        val skey = getSecretKey(keyAlias)
        return encrypt(data, skey)
    }

    override fun decryptData(data: String): String {
        val skey = getSecretKey(keyAlias)
        return decrypt(data, skey)
    }

}