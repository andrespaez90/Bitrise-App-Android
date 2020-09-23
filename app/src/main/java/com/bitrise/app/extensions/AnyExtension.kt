package com.bitrise.app.extensions

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