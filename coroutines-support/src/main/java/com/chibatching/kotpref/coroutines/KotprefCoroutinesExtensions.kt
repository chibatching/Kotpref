package com.chibatching.kotpref.coroutines

import com.chibatching.kotpref.KotprefModel
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import kotlin.reflect.KProperty0

fun <T> KotprefModel.asCoroutine(property: KProperty0<T>): Deferred<T> {
    val deferred = CompletableDeferred<T>()
    deferred.complete(property.get())
    return deferred
}