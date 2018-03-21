package com.chibatching.kotpref.pref

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.os.Build
import com.chibatching.kotpref.KotprefModel
import com.chibatching.kotpref.execute
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
internal class StringSetPref(
    val default: () -> Set<String>,
    override val key: String?,
    private val commitByDefault: Boolean
) : ReadOnlyProperty<KotprefModel, MutableSet<String>>, PreferenceKey {

    private var stringSet: MutableSet<String>? = null
    private var lastUpdate: Long = 0L

    override operator fun getValue(
        thisRef: KotprefModel,
        property: KProperty<*>
    ): MutableSet<String> {
        if (stringSet == null || lastUpdate < thisRef.kotprefTransactionStartTime) {
            val prefSet = thisRef.kotprefPreference.getStringSet(key ?: property.name, null)
                ?.let { HashSet(it) }
            stringSet = PrefMutableSet(
                thisRef,
                prefSet ?: default.invoke().toMutableSet(),
                key ?: property.name
            )
            lastUpdate = System.currentTimeMillis()
        }
        return stringSet!!
    }

    internal inner class PrefMutableSet(
        val kotprefModel: KotprefModel,
        val set: MutableSet<String>,
        val key: String
    ) : MutableSet<String> by set {

        init {
            addAll(set)
        }

        private var transactionData: MutableSet<String>? = null
            get() {
                field = field ?: set.toMutableSet()
                return field
            }

        internal fun syncTransaction() {
            synchronized(this) {
                transactionData?.let {
                    set.clear()
                    set.addAll(it)
                    transactionData = null
                }
            }
        }

        @SuppressLint("CommitPrefEdits")
        override fun add(element: String): Boolean {
            if (kotprefModel.kotprefInTransaction) {
                val result = transactionData!!.add(element)
                kotprefModel.kotprefEditor!!.putStringSet(key, this)
                return result
            }
            val result = set.add(element)
            kotprefModel.kotprefPreference.edit().putStringSet(key, set).execute(commitByDefault)
            return result
        }

        @SuppressLint("CommitPrefEdits")
        override fun addAll(elements: Collection<String>): Boolean {
            if (kotprefModel.kotprefInTransaction) {
                val result = transactionData!!.addAll(elements)
                kotprefModel.kotprefEditor!!.putStringSet(key, this)
                return result
            }
            val result = set.addAll(elements)
            kotprefModel.kotprefPreference.edit().putStringSet(key, set).execute(commitByDefault)
            return result
        }

        @SuppressLint("CommitPrefEdits")
        override fun remove(element: String): Boolean {
            if (kotprefModel.kotprefInTransaction) {
                val result = transactionData!!.remove(element)
                kotprefModel.kotprefEditor!!.putStringSet(key, this)
                return result
            }
            val result = set.remove(element)
            kotprefModel.kotprefPreference.edit().putStringSet(key, set).execute(commitByDefault)
            return result
        }

        @SuppressLint("CommitPrefEdits")
        override fun removeAll(elements: Collection<String>): Boolean {
            if (kotprefModel.kotprefInTransaction) {
                val result = transactionData!!.removeAll(elements)
                kotprefModel.kotprefEditor!!.putStringSet(key, this)
                return result
            }
            val result = set.removeAll(elements)
            kotprefModel.kotprefPreference.edit().putStringSet(key, set).execute(commitByDefault)
            return result
        }

        @SuppressLint("CommitPrefEdits")
        override fun retainAll(elements: Collection<String>): Boolean {
            if (kotprefModel.kotprefInTransaction) {
                val result = transactionData!!.retainAll(elements)
                kotprefModel.kotprefEditor!!.putStringSet(key, this)
                return result
            }
            val result = set.retainAll(elements)
            kotprefModel.kotprefPreference.edit().putStringSet(key, set).execute(commitByDefault)
            return result
        }

        @SuppressLint("CommitPrefEdits")
        override fun clear() {
            if (kotprefModel.kotprefInTransaction) {
                val result = transactionData!!.clear()
                kotprefModel.kotprefEditor!!.putStringSet(key, this)
                return result
            }
            set.clear()
            kotprefModel.kotprefPreference.edit().putStringSet(key, set).execute(commitByDefault)
        }

        override fun contains(element: String): Boolean {
            if (kotprefModel.kotprefInTransaction) {
                return element in transactionData!!
            }
            return element in set
        }

        override fun containsAll(elements: Collection<String>): Boolean {
            if (kotprefModel.kotprefInTransaction) {
                return transactionData!!.containsAll(elements)
            }
            return set.containsAll(elements)
        }

        override fun iterator(): MutableIterator<String> {
            return if (kotprefModel.kotprefInTransaction) {
                kotprefModel.kotprefEditor!!.putStringSet(key, this@PrefMutableSet)
                KotprefMutableIterator(transactionData!!.iterator(), true)
            } else {
                KotprefMutableIterator(set.iterator(), false)
            }
        }

        override val size: Int
            get() {
                if (kotprefModel.kotprefInTransaction) {
                    return transactionData!!.size
                }
                return set.size
            }

        private inner class KotprefMutableIterator(
            val baseIterator: MutableIterator<String>, val inTransaction: Boolean
        ) : MutableIterator<String> by baseIterator {

            @SuppressLint("CommitPrefEdits")
            override fun remove() {
                baseIterator.remove()
                if (!inTransaction) {
                    kotprefModel.kotprefPreference.edit().putStringSet(key, set)
                        .execute(commitByDefault)
                }
            }
        }
    }
}
