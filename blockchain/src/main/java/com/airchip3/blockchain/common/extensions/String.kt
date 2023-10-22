package com.airchip3.blockchain.common.extensions

import org.bitcoinj.core.Base58
import java.math.BigDecimal

fun String.decodeBase58(checked: Boolean = false): ByteArray? {
    return try {
        if (checked) Base58.decodeChecked(this) else Base58.decode(this)
    } catch (exception: Exception) {
        null
    }
}



fun String.hexToBigDecimal(): BigDecimal? {
    return removePrefix("0x").toBigIntegerOrNull(16)?.toBigDecimal()
}

fun String?.toBigDecimalOrDefault(default: BigDecimal = BigDecimal.ZERO): BigDecimal =
    this?.toBigDecimalOrNull() ?: default



fun String.remove(vararg symbols: String): String {
    var newString = this
    symbols.forEach { newString = newString.replace(it, "") }
    return newString
}
inline fun <R> String?.letNotBlank(block: (String) -> R): R? {
    if (isNullOrBlank()) return null

    return block(this)
}
