package com.chibatching.kotpref


class PrefMutableSet(val set: MutableSet<String>,
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