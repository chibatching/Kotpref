package com.chibatching.kotpref

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.preference.PreferenceManager
import android.security.KeyPairGeneratorSpec
import android.util.Base64
import androidx.annotation.RequiresApi
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.math.BigInteger
import java.security.Key
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.SecureRandom
import java.util.*
import javax.crypto.Cipher
import javax.crypto.CipherInputStream
import javax.crypto.CipherOutputStream
import javax.crypto.spec.SecretKeySpec
import javax.security.auth.x500.X500Principal

@RequiresApi(api = Build.VERSION_CODES.KITKAT)

class CryptoSessionPreM(
    private val context: Context,
    private val keyAlias: String,
    private val androidKeyStore: String
) : CryptoSession {
    private val preferences: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(
            context
        )
    }
    private val RSA_MODE = "RSA/ECB/PKCS1Padding"
    private val AES_MODE = "AES/ECB/PKCS7Padding"
    private val ENCRYPTED_KEY = "${keyAlias}_androidKeyStore"

    init {
        initKeyStore(context)
    }

    private lateinit var keyStore:KeyStore

    private fun initKeyStore(context: Context) {
        keyStore = KeyStore.getInstance(androidKeyStore)
        keyStore.load(null)
        // Generate the RSA key pairs
        if (!keyStore.containsAlias(keyAlias)) {
            // Generate a key pair for encryption
            val start = Calendar.getInstance()
            val end = Calendar.getInstance()
            end.add(Calendar.YEAR, 1)
            val spec = KeyPairGeneratorSpec.Builder(context)
                .setAlias(keyAlias)
                .setSubject(X500Principal("CN=$keyAlias, O=Android Authority , C=COMPANY"))
                .setSerialNumber(BigInteger.TEN)
                .setStartDate(start.time)
                .setEndDate(end.time)
                .build()
            val kpg = KeyPairGenerator.getInstance("RSA", androidKeyStore)
            kpg.initialize(spec)
            kpg.generateKeyPair()
            prepareAESKey()
        }
    }

    @Throws(Exception::class)
    private fun rsaEncrypt(secret: ByteArray): ByteArray {
        val privateKeyEntry = keyStore.getEntry(keyAlias, null) as KeyStore.PrivateKeyEntry
        // Encrypt the text
        val inputCipher = Cipher.getInstance(RSA_MODE, "AndroidOpenSSL")
        inputCipher.init(Cipher.ENCRYPT_MODE, privateKeyEntry.certificate.publicKey)

        val outputStream = ByteArrayOutputStream()
        val cipherOutputStream = CipherOutputStream(outputStream, inputCipher)
        cipherOutputStream.write(secret)
        cipherOutputStream.close()

        return outputStream.toByteArray()
    }

    @Throws(Exception::class)
    private fun rsaDecrypt(encrypted: ByteArray): ByteArray {
        val privateKeyEntry = keyStore.getEntry(keyAlias, null) as KeyStore.PrivateKeyEntry
        val output = Cipher.getInstance(RSA_MODE, "AndroidOpenSSL")
        output.init(Cipher.DECRYPT_MODE, privateKeyEntry.privateKey)
        val cipherInputStream = CipherInputStream(
            ByteArrayInputStream(encrypted), output
        )
        val values = arrayListOf<Byte>()
        var nextByte: Int
        nextByte = cipherInputStream.read()
        while (nextByte != -1) {
            values.add(nextByte.toByte())
            nextByte = cipherInputStream.read()
        }

        val bytes = ByteArray(values.size)
        for (i in bytes.indices) {
            bytes[i] = values[i]
        }
        return bytes
    }

    fun prepareAESKey() {
        var enryptedKeyB64 = preferences.getString(ENCRYPTED_KEY, null)
        if (enryptedKeyB64 == null) {
            val key = ByteArray(16)
            val secureRandom = SecureRandom()
            secureRandom.nextBytes(key)
            val encryptedKey = rsaEncrypt(key)
            enryptedKeyB64 = Base64.encodeToString(encryptedKey, Base64.DEFAULT)
            val edit = preferences.edit()
            edit.putString(ENCRYPTED_KEY, enryptedKeyB64)
            edit.apply()
        }
    }

    @Throws(Exception::class)
    private fun getSecretKey(context: Context): Key {
        val enryptedKeyB64 = preferences.getString(ENCRYPTED_KEY, null)
        // need to check null, omitted here
        val encryptedKey = Base64.decode(enryptedKeyB64, Base64.DEFAULT)
        val key = rsaDecrypt(encryptedKey)
        return SecretKeySpec(key, "AES")
    }

    private fun encrypt(context: Context, input: ByteArray): String {
        val c = Cipher.getInstance(AES_MODE, "BC")
        c.init(Cipher.ENCRYPT_MODE, getSecretKey(context))
        val encodedBytes = c.doFinal(input)
        return Base64.encodeToString(encodedBytes, Base64.DEFAULT)
    }


    private fun decrypt(context: Context, encrypted: ByteArray): ByteArray {
        val toDecript = Base64.decode(encrypted, Base64.DEFAULT)
        val c = Cipher.getInstance(AES_MODE, "BC")
        c.init(Cipher.DECRYPT_MODE, getSecretKey(context))
        return c.doFinal(toDecript)
    }

    override fun encryptData(data: String): String {
        return encrypt(context, data.toByteArray())
    }

    override fun decryptData(data: String): String {
        return String(decrypt(context, data.toByteArray()))
    }

}