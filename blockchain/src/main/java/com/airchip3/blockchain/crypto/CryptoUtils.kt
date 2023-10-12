package com.airchip3.blockchain.crypto

import com.airchip3.blockchain.common.card.EllipticCurve


object CryptoUtils {
    fun compressPublicKey(key: ByteArray, curve: EllipticCurve = EllipticCurve.Secp256k1): ByteArray {
        return when (curve) {
            EllipticCurve.Secp256k1 -> Secp256k1.compressPublicKey(key)
            else -> throw UnsupportedOperationException()
        }
    }

    fun decompressPublicKey(key: ByteArray, curve: EllipticCurve = EllipticCurve.Secp256k1): ByteArray {
        return when (curve) {
            EllipticCurve.Secp256k1 -> Secp256k1.decompressPublicKey(key)
            else -> throw UnsupportedOperationException()
        }
    }
}