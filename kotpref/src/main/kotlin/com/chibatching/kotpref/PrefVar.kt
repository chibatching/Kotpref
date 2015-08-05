package com.chibatching.kotpref

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

private class StringSetPrefVar(val default: Set<String>) : ReadWriteProperty<KotprefModel, MutableSet<String>> {

    var set: MutableSet<String>? = null

    override fun get(thisRef: KotprefModel, desc: PropertyMetadata): MutableSet<String> {
        if (set == null) {
            set = PrefMutableSet(thisRef.kotprefPreference.getStringSet(desc.name, default), desc.name, thisRef)
        }
        return set!!
    }

    override fun set(thisRef: KotprefModel, desc: PropertyMetadata, value: MutableSet<String>) {
        thisRef.kotprefPreference.edit().putStringSet(desc.name, value).apply()
    }
}

private class PrefMutableSet(val set: MutableSet<String>,
                             val key: String,
                             val thisRef: KotprefModel) : MutableSet<String> by set {
    override fun add(e: String): Boolean {
        val result = set.add(e)
        thisRef.kotprefPreference.edit().putStringSet(key, set).apply()
        return result
    }

    override fun addAll(c: Collection<String>): Boolean {
        val result = set.addAll(c)
        thisRef.kotprefPreference.edit().putStringSet(key, set).apply()
        return result
    }

    override fun remove(o: Any?): Boolean {
        val result = set.remove(o)
        thisRef.kotprefPreference.edit().putStringSet(key, set).apply()
        return result
    }

    override fun removeAll(c: Collection<Any?>): Boolean {
        val result = set.removeAll(c)
        thisRef.kotprefPreference.edit().putStringSet(key, set).apply()
        return result
    }

    override fun retainAll(c: Collection<Any?>): Boolean {
        val result = set.retainAll(c)
        thisRef.kotprefPreference.edit().putStringSet(key, set).apply()
        return result
    }

    override fun clear() {
        set.clear()
        thisRef.kotprefPreference.edit().putStringSet(key, set).apply()
    }
}
