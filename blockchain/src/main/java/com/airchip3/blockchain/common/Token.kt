//package com.airchip3.blockchain.common
//
//import com.squareup.moshi.JsonClass
//import java.util.*
//
//@JsonClass(generateAdapter = true)
//data class Token(
//    val name: String,
//    val symbol: String,
//    val contractAddress: String,
//    val decimals: Int,
//    val id: String? = null
//) {
//    constructor(
//        symbol: String,
//        contractAddress: String,
//        decimals: Int,
//    ) : this(symbol, symbol, contractAddress, decimals)
//
//    override fun equals(other: Any?): Boolean {
//        if (this === other) return true
//        if (javaClass != other?.javaClass) return false
//
//        other as Token
//
//        if (symbol != other.symbol) return false
//        if (contractAddress.toLowerCase(Locale.ROOT) != other.contractAddress.toLowerCase(Locale.ROOT)) {
//            return false
//        }
//
//        return true
//    }
//
//    override fun hashCode(): Int {
//        var result = symbol.hashCode()
//        result = 31 * result + contractAddress.toLowerCase(Locale.ROOT).hashCode()
//        return result
//    }
//}