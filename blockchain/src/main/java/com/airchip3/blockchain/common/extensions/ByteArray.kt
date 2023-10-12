package com.airchip3.blockchain.common.extensions

import com.airchip3.blockchain.crypto.CryptoUtils
import org.bouncycastle.crypto.digests.RIPEMD160Digest
import java.security.MessageDigest

/**
 * Extension functions for [ByteArray].
 */
fun ByteArray.toHexString(): String = joinToString("") { "%02X".format(it) }

fun ByteArray.calculateSha256(): ByteArray = MessageDigest.getInstance("SHA-256").digest(this)

fun ByteArray.toCompressedPublicKey(): ByteArray = CryptoUtils.compressPublicKey(this)

fun ByteArray.toDecompressedPublicKey(): ByteArray = CryptoUtils.decompressPublicKey(this)

fun ByteArray.calculateRipemd160(): ByteArray {
    val digest = RIPEMD160Digest()
    digest.update(this, 0, this.size)
    val out = ByteArray(20)
    digest.doFinal(out, 0)
    return out
}