package com.airchip3.blockchain.blockchains.binance

import com.airchip3.blockchain.blockchains.binance.client.encoding.Crypto
import com.airchip3.blockchain.common.address.AddressService
import com.airchip3.blockchain.common.card.EllipticCurve
import com.airchip3.blockchain.common.extensions.calculateRipemd160
import com.airchip3.blockchain.common.extensions.calculateSha256
import com.airchip3.blockchain.common.extensions.toCompressedPublicKey
import org.bitcoinj.core.Bech32

class BinanceAddressService(private val testNet: Boolean = false) : AddressService() {
    override fun makeAddress(walletPublicKey: ByteArray, curve: EllipticCurve?): String {
        val publicKeyHash = walletPublicKey.toCompressedPublicKey().calculateSha256().calculateRipemd160()
        return if (testNet) {
            Bech32.encode("tbnb", Crypto.convertBits(publicKeyHash, 0,
                    publicKeyHash.size, 8, 5, false)
            )
        } else {
            Bech32.encode("bnb", Crypto.convertBits(publicKeyHash, 0,
                    publicKeyHash.size, 8, 5, false)
            )
        }
    }

    override fun validate(address: String): Boolean {
        return try {
            Crypto.decodeAddress(address)
            if (testNet) {
                address.startsWith("tbnb1")
            } else {
                address.startsWith("bnb1")
            }
        } catch (exception: Exception) {
            false
        }
    }
}

enum class BinanceChain(val value: String) {
    Nile("Binance-Chain-Nile"),
    Tigris("Binance-Chain-Tigris");

    companion object {
        fun getChain(testNet: Boolean): BinanceChain = if (testNet) Nile else Tigris
    }
}
