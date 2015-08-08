package com.chibatching.kotpref

import java.util.*
import kotlin.properties.ReadWriteProperty


private class StringPrefVar(val default: String) : ReadWriteProperty<KotprefModel, String> {

    override fun get(thisRef: KotprefModel, desc: PropertyMetadata): String {
        return thisRef.kotprefPreference.getString(desc.name, default)
    }

    override fun set(thisRef: KotprefModel, desc: PropertyMetadata, value: String) {
        thisRef.kotprefPreference.edit().putString(desc.name, value).apply()
    }
}

private class IntPrefVar(val default: Int) : ReadWriteProperty<KotprefModel, Int> {

    override fun get(thisRef: KotprefModel, desc: PropertyMetadata): Int {
        return thisRef.kotprefPreference.getInt(desc.name, default)
    }

    override fun set(thisRef: KotprefModel, desc: PropertyMetadata, value: Int) {
        thisRef.kotprefPreference.edit().putInt(desc.name, value).apply()
    }
}

private class LongPrefVar(val default: Long) : ReadWriteProperty<KotprefModel, Long> {

    override fun get(thisRef: KotprefModel, desc: PropertyMetadata): Long {
        return thisRef.kotprefPreference.getLong(desc.name, default)
    }

    override fun set(thisRef: KotprefModel, desc: PropertyMetadata, value: Long) {
        thisRef.kotprefPreference.edit().putLong(desc.name, value).apply()
    }
}

private class FloatPrefVar(val default: Float) : ReadWriteProperty<KotprefModel, Float> {

    override fun get(thisRef: KotprefModel, desc: PropertyMetadata): Float {
        return thisRef.kotprefPreference.getFloat(desc.name, default)
    }

    override fun set(thisRef: KotprefModel, desc: PropertyMetadata, value: Float) {
        thisRef.kotprefPreference.edit().putFloat(desc.name, value).apply()
    }
}

private class BooleanPrefVar(val default: Boolean) : ReadWriteProperty<KotprefModel, Boolean> {

    override fun get(thisRef: KotprefModel, desc: PropertyMetadata): Boolean {
        return thisRef.kotprefPreference.getBoolean(desc.name, default)
    }

    override fun set(thisRef: KotprefModel, desc: PropertyMetadata, value: Boolean) {
        thisRef.kotprefPreference.edit().putBoolean(desc.name, value).apply()
    }
}
