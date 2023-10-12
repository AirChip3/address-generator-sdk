package com.airchip3.blockchain.blockchains.ethereum


import com.airchip3.blockchain.common.address.AddressService
import com.airchip3.blockchain.common.card.EllipticCurve
import com.airchip3.blockchain.common.extensions.toDecompressedPublicKey
import org.kethereum.crypto.toAddress
import org.kethereum.erc55.hasValidERC55ChecksumOrNoChecksum
import org.kethereum.erc55.withERC55Checksum
import org.kethereum.model.Address
import org.kethereum.model.PublicKey


class EthereumAddressService : AddressService() {
    override fun makeAddress(walletPublicKey: ByteArray, curve: EllipticCurve?): String =
        PublicKey(
            walletPublicKey.toDecompressedPublicKey().sliceArray(1..64)
        ).toAddress().withERC55Checksum().hex

    override fun validate(address: String): Boolean = Address(address).hasValidERC55ChecksumOrNoChecksum()
}