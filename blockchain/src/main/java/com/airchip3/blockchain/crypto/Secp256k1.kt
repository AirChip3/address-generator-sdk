package com.airchip3.blockchain.crypto

import org.bouncycastle.jce.ECNamedCurveTable
import org.bouncycastle.jce.spec.ECParameterSpec


object Secp256k1 {
    internal fun compressPublicKey(key: ByteArray): ByteArray = if (key.size == 65) {
        val publicKeyPoint = createECSpec().curve.decodePoint(key)
        publicKeyPoint.getEncoded(true)
    } else {
        key
    }

    internal fun decompressPublicKey(key: ByteArray): ByteArray = if (key.size == 33) {
        val publicKeyPoint = createECSpec().curve.decodePoint(key)
        publicKeyPoint.getEncoded(false)
    } else {
        key
    }

    private fun createECSpec(): ECParameterSpec = ECNamedCurveTable.getParameterSpec("secp256k1")
}
