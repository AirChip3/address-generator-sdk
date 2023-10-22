package com.airchip3.blockchain.blockchains.tron


import com.airchip3.blockchain.blockchains.ethereum.EthereumUtils.Companion.toKeccak
import com.airchip3.blockchain.common.address.AddressService
import com.airchip3.blockchain.blockchains.tron.libs.Base58Check
import com.airchip3.blockchain.common.card.EllipticCurve
import com.airchip3.blockchain.common.extensions.decodeBase58
import com.airchip3.blockchain.common.extensions.toByteArray
import com.airchip3.blockchain.common.extensions.toDecompressedPublicKey
import com.airchip3.blockchain.common.extensions.toHexString

class TronAddressService : AddressService() {
    override fun makeAddress(walletPublicKey: ByteArray, curve: EllipticCurve?): String {
        val decompressedPublicKey = walletPublicKey.toDecompressedPublicKey()
        val data = decompressedPublicKey.drop(1).toByteArray()
        val hash = data.toKeccak()

        val addressData = PREFIX.toByteArray(1) + hash.takeLast(ADDRESS_LENGTH-1).toByteArray()
        return Base58Check.bytesToBase58(addressData)
    }

    override fun validate(address: String): Boolean {
        val decoded = address.decodeBase58(checked = true) ?: return false
        return decoded.count() == ADDRESS_LENGTH &&
                decoded.toHexString().startsWith(PREFIX.toByteArray(1).toHexString())
    }

    companion object {
        private const val PREFIX = 0x41
        private const val ADDRESS_LENGTH = 21

        fun toHexForm(base58String: String, length: Int? = null): String? {
            val hex = base58String.decodeBase58(checked = true)?.toHexString() ?: return null
            return if (length != null) {
                hex.padStart(length, '0')
            } else {
                hex
            }
        }
    }
}