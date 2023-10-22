package com.airchip3.blockchain.common

import com.airchip3.blockchain.blockchains.binance.BinanceAddressService
import com.airchip3.blockchain.blockchains.bitcoin.BitcoinAddressService

import com.airchip3.blockchain.blockchains.ethereum.EthereumAddressService
import com.airchip3.blockchain.blockchains.tron.TronAddressService
import com.airchip3.blockchain.common.address.Address
import com.airchip3.blockchain.common.address.AddressService
import com.airchip3.blockchain.common.address.MultisigAddressProvider
import com.airchip3.blockchain.common.card.EllipticCurve


enum class Blockchain(
    val id: String,
    val currency: String,
    val fullName: String
) {
    Unknown("", "", ""),
    Binance("BINANCE", "BNB", "BNB Beacon Chain"),
    BinanceTestnet("BINANCE/test", "BNB", "BNB Beacon Chain Testnet"),
    Bitcoin("BTC", "BTC", "Bitcoin"),
    BitcoinTestnet("BTC/test", "BTC", "Bitcoin Testnet"),
    Dogecoin("DOGE", "DOGE", "Dogecoin"),
    Ethereum("ETH", "ETH", "Ethereum"),
    EthereumTestnet("ETH/test", "ETH", "Ethereum Testnet"),
    Tron("TRON", "TRX", "Tron"),
    TronTestnet("TRON/test", "TRX", "Tron Testnet"),
    ;

    fun decimals(): Int = when (this) {
        Unknown -> 0
        Tron, TronTestnet,
        -> 6
        Bitcoin, BitcoinTestnet,
        Binance, BinanceTestnet,
        Dogecoin,
        -> 8
        Ethereum, EthereumTestnet,
        -> 18
    }

    fun makeAddresses(
        walletPublicKey: ByteArray,
        curve: EllipticCurve = EllipticCurve.Secp256k1,
        pairPublicKey: ByteArray? = null,
    ): Set<Address> {
        return if (pairPublicKey != null) {
            (getAddressService() as? MultisigAddressProvider)
                ?.makeMultisigAddresses(walletPublicKey, pairPublicKey) ?: emptySet()
        } else {
            getAddressService().makeAddresses(walletPublicKey, curve)
        }
    }

    fun validateAddress(address: String): Boolean = getAddressService().validate(address)

    private fun getAddressService(): AddressService {
        return when (this) {
            Bitcoin, BitcoinTestnet,
            Dogecoin,
            -> BitcoinAddressService(this)
            Ethereum, EthereumTestnet,
            -> EthereumAddressService()
            Binance -> BinanceAddressService()
            BinanceTestnet -> BinanceAddressService(true)
            Tron, TronTestnet -> TronAddressService()
            Unknown -> throw Exception("unsupported blockchain")
        }
    }

//    fun getChainId(): Int? {
//        return when (this) {
//            Ethereum -> Chain.Mainnet.id
//            EthereumTestnet -> Chain.Goerli.id
//            else -> null
//        }
//    }
}