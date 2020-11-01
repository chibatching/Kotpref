package com.chibatching.kotpref.pref

import com.chibatching.kotpref.KotprefModel
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

public abstract class AbstractStringSetPref :
    ReadOnlyProperty<KotprefModel, MutableSet<String>>,
    PreferenceProperty {

    public abstract val key: String?

    private lateinit var property: KProperty<*>

    override val propertyName: String
        get() = property.name

    override val preferenceKey: String
        get() = key ?: property.name

    public operator fun provideDelegate(
        thisRef: KotprefModel,
        property: KProperty<*>
    ): ReadOnlyProperty<KotprefModel, MutableSet<String>> {
        this.property = property
        thisRef.kotprefProperties[property.name] = this
        return this
    }
}
