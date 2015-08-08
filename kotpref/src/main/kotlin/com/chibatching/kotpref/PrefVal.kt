package com.chibatching.kotpref

import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty


private class StringSetPrefVal(val default: () -> Set<String>) : ReadOnlyProperty<KotprefModel, MutableSet<String>> {

    var stringSet: MutableSet<String>? = null

    override fun get(thisRef: KotprefModel, desc: PropertyMetadata): MutableSet<String> {
        if (stringSet == null) {
            val prefSet = thisRef.kotprefPreference.getStringSet(desc.name, null)
            stringSet = PrefMutableSet(prefSet ?: default.invoke().toMutableSet(), desc.name, thisRef)
            if (prefSet == null) {
                stringSet?.addAll(stringSet!!)
            }
        }
        return stringSet!!
    }
}
