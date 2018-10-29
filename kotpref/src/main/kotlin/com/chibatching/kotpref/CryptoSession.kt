package com.chibatching.kotpref

import java.security.KeyStoreException

interface CryptoSession{
    fun encryptData(data:String):String
    fun decryptData(data:String):String
}
