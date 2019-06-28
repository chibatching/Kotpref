package com.chibatching.kotpref.preference

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.chibatching.kotpref.KotprefModel
import com.chibatching.kotpref.pref.AbstractPref
import com.chibatching.kotpref.pref.BooleanPref
import com.chibatching.kotpref.pref.StringPref
import com.chibatching.kotpref.preference.data.BasePrefData
import com.chibatching.kotpref.preference.data.BooleanPrefData
import com.chibatching.kotpref.preference.data.StringPrefData
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

internal object PreferenceUtil {

    // ------------------------
    // Kotpref getter functions
    // ------------------------

    const val PREF_NAME = "name"

    fun getKotprefObject(intent: Intent) =
        getKotprefObject(intent.getStringExtra(PREF_NAME))

    fun getKotprefObject(bundle: Bundle) =
        getKotprefObject(bundle.getString(PREF_NAME))

    fun putKotprefObject(intent: Intent, kotPref: KotprefModel) =
        intent.putExtra(PREF_NAME, kotPref::class.java.name)

    fun putKotprefObject(bundle: Bundle, kotPref: KotprefModel) =
        bundle.putString(PREF_NAME, kotPref::class.java.name)

    // ------------------------
    // Kreflection functions
    // ------------------------

    private fun getKotprefObject(className: String): KotprefModel {
        return Class.forName(className).kotlin.objectInstance as KotprefModel
    }

    fun getPreferenceDatas(context: Context, kotprefModel: KotprefModel): List<BasePrefData<*>> {
        val list = arrayListOf<BasePrefData<*>>()

        val fields = kotprefModel::class.java.declaredFields

        for (f in fields) {
            if (f.type == ReadWriteProperty::class.java) {
                f.isAccessible = true
                val preference = f.get(kotprefModel) as AbstractPref<*>

                val propertyName = f.name.replace("\$delegate", "")
                val property = kotprefModel::class.memberProperties
                    .firstOrNull { it.name == propertyName } as KProperty1<Any, *>?

                val keyName = preference.key ?: property!!.name
                val label =
                    preference.preferenceLabel?.let { context.getString(it) } ?: propertyName

                when (preference) {
                    is StringPref -> {
                        val value = preference.getValue(kotprefModel, property!!)
                        list.add(StringPrefData(keyName, label, preference.preferenceSummary, value, preference.default))
                    }
                    is BooleanPref -> {
                        val value = preference.getValue(kotprefModel, property!!)
                        list.add(BooleanPrefData(keyName, label, preference.preferenceSummary, value, preference.default))
                    }
                }
            }
        }

        return list
    }
}