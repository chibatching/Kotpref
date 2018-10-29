package com.chibatching.kotpref

import android.content.SharedPreferences


internal class KotprefEncryptedPreferences(
    preferences: SharedPreferences,
    private val cryptoSession: CryptoSession
) : KotprefPreferences(preferences) {

    override fun edit(): SharedPreferences.Editor {
        return EncryptedKotprefEditor(preferences.edit(), cryptoSession)
    }

    internal class EncryptedKotprefEditor(
        editor: SharedPreferences.Editor,
        val cryptoSession: CryptoSession
    ) : KotprefPreferences.KotprefEditor(editor) {

        override fun putStringSet(
            key: String?,
            values: MutableSet<String>?
        ): SharedPreferences.Editor {
            val encryptedValues =
                values?.asSequence()?.map { cryptoSession.encryptData(it) }?.toMutableSet()
            return super.putStringSet(key, encryptedValues)
        }

        override fun putLong(key: String?, value: Long): SharedPreferences.Editor {
            val encryptedValue = cryptoSession.encryptData(value.toString())
            return saveValue(key, encryptedValue)
        }

        override fun putInt(key: String?, value: Int): SharedPreferences.Editor {
            val encryptedValue = cryptoSession.encryptData(value.toString())
            return saveValue(key, encryptedValue)
        }

        override fun putBoolean(key: String?, value: Boolean): SharedPreferences.Editor {
            val encryptedValue = cryptoSession.encryptData(value.toString())
            return saveValue(key, encryptedValue)
        }

        override fun putFloat(key: String?, value: Float): SharedPreferences.Editor {
            val encryptedValue = cryptoSession.encryptData(value.toString())
            return saveValue(key, encryptedValue)
        }

        override fun putString(key: String?, value: String?): SharedPreferences.Editor {
            val encryptedValue = value?.let { cryptoSession.encryptData(it) }
            return saveValue(key, encryptedValue)
        }

        private fun saveValue(
            key: String?,
            encryptedValue: String?
        ) = super.putString(key, encryptedValue)
    }

    override fun getBoolean(key: String?, defValue: Boolean): Boolean {
        val defValueString = defValue.toString()
        val encryptedValue = getEncryptedValue(key, defValueString)
        val decryptedValue =
            encryptedValue
                ?.takeIf { encryptedValue != defValueString }
                ?.let {
                    cryptoSession.decryptData(
                        it
                    )
                } ?: defValueString
        return decryptedValue.toBoolean()
    }

    private fun getEncryptedValue(key: String?, defValue: String?): String? {
        return super.getString(key, defValue)
    }

    override fun getInt(key: String?, defValue: Int): Int {
        val defValueString = defValue.toString()
        val encryptedValue = getEncryptedValue(key, defValueString)
        val decryptedValue =
            encryptedValue
                ?.takeIf { encryptedValue != defValueString }
                ?.let {
                    cryptoSession.decryptData(
                        it
                    )
                } ?: defValueString
        return decryptedValue.toInt()
    }

    override fun getLong(key: String?, defValue: Long): Long {
        val defValueString = defValue.toString()
        val encryptedValue = getEncryptedValue(key, defValueString)
        val decryptedValue =
            encryptedValue
                ?.takeIf { encryptedValue != defValueString }
                ?.let {
                    cryptoSession.decryptData(
                        it
                    )
                } ?: defValueString
        return decryptedValue.toLong()
    }

    override fun getFloat(key: String?, defValue: Float): Float {
        val defValueString = defValue.toString()
        val encryptedValue = getEncryptedValue(key, defValueString)
        val decryptedValue =
            encryptedValue
                ?.takeIf { encryptedValue != defValueString }
                ?.let {
                    cryptoSession.decryptData(
                        it
                    )
                } ?: defValueString
        return decryptedValue.toFloat()
    }

    override fun getStringSet(key: String?, defValues: MutableSet<String>?): MutableSet<String>? {
        val stringSet = super.getStringSet(key, defValues)
        return stringSet?.asSequence()?.map { encryptedValue ->
            cryptoSession.decryptData(encryptedValue)
        }?.toMutableSet()
    }

    override fun getString(key: String?, defValue: String?): String? {
        val defValueString = defValue.toString()
        val encryptedValue = getEncryptedValue(key, defValueString)
        val decryptedValue =
            encryptedValue
                ?.takeIf { encryptedValue != defValueString }
                ?.let {
                    cryptoSession.decryptData(
                        it
                    )
                } ?: defValueString
        return decryptedValue
    }
}
