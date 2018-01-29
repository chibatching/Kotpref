package com.chibatching.kotpref.pref

import android.content.SharedPreferences
import com.chibatching.kotpref.KotprefModel
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


abstract class AbstractPref<T : Any?> : ReadWriteProperty<KotprefModel, T>, PreferenceKey {

    private var lastUpdate: Long = 0
    private var transactionData: Any? = null

    abstract override val key: String?

    override operator fun getValue(thisRef: KotprefModel, property: KProperty<*>): T {
        if (!thisRef.kotprefInTransaction) {
            return getFromPreference(property, thisRef.kotprefPreference)
        }
        if (lastUpdate < thisRef.kotprefTransactionStartTime) {
            transactionData = getFromPreference(property, thisRef.kotprefPreference)
            lastUpdate = System.currentTimeMillis()
        }
        @Suppress("UNCHECKED_CAST")
        return transactionData as T
    }

    override operator fun setValue(thisRef: KotprefModel, property: KProperty<*>, value: T) {
        if (thisRef.kotprefInTransaction) {
            transactionData = value
            lastUpdate = System.currentTimeMillis()
            setToEditor(property, value, thisRef.kotprefEditor!!)
        } else {
            setToPreference(property, value, thisRef.kotprefPreference)
        }
    }

    abstract fun getFromPreference(property: KProperty<*>, preference: SharedPreferences): T
    abstract fun setToPreference(property: KProperty<*>, value: T, preference: SharedPreferences)
    abstract fun setToEditor(property: KProperty<*>, value: T, editor: SharedPreferences.Editor)
}
