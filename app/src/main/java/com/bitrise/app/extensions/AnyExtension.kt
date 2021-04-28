@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.bitrise.app.extensions

import java.text.SimpleDateFormat
import java.util.*

inline fun <T> tryOrDefault(defaultValue: T, blockToTry: () -> T): T = try {
    blockToTry()
} catch (t: Throwable) {
    defaultValue
}

inline fun <T, U, R> Pair<T?, U?>.biLet(body: (T, U) -> R): R? {
    val first = first
    val second = second
    if (first != null && second != null) {
        return body(first, second)
    }
    return null
}

fun String.toCalendar(): Calendar {
    val calendar = Calendar.getInstance()
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
    calendar.time = format.parse(this)
    return calendar
}