package com.example.blockchain_sdk.extensions

fun String.hexToBytes(): ByteArray {
    return ByteArray(this.length / 2)
    { i ->
        Integer.parseInt(this.substring(2 * i, 2 * i + 2), 16).toByte()
    }
}

inline fun <R> String?.letNotBlank(block: (String) -> R): R? {
    if (isNullOrBlank()) return null

    return block(this)
}