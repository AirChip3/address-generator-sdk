package com.airchip3.blockchain.common

import com.airchip3.blockchain.blockchains.binance.BinanceAddressService
import com.airchip3.blockchain.blockchains.bitcoin.BitcoinAddressService

import com.airchip3.blockchain.blockchains.ethereum.EthereumAddressService
import com.airchip3.blockchain.blockchains.tron.TronAddressService
import com.airchip3.blockchain.blockchains.xrp.XrpAddressService
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
    Arbitrum("ARBITRUM-ONE", "ETH", "Arbitrum"),
    ArbitrumTestnet("ARBITRUM/test", "ETH", "Arbitrum Testnet"),
    Binance("BINANCE", "BNB", "BNB Beacon Chain"),
    BinanceTestnet("BINANCE/test", "BNB", "BNB Beacon Chain Testnet"),
    BSC("BSC", "BNB", "BNB Smart Chain"),
    BSCTestnet("BSC/test", "BNB", "BNB Smart Chain Testnet"),
    Bitcoin("BTC", "BTC", "Bitcoin"),
    BitcoinTestnet("BTC/test", "BTC", "Bitcoin Testnet"),
    Dogecoin("DOGE", "DOGE", "Dogecoin"),
    Ethereum("ETH", "ETH", "Ethereum"),
    EthereumTestnet("ETH/test", "ETH", "Ethereum Testnet"),
    EthereumClassic("ETC", "ETC", "Ethereum Classic"),
    EthereumClassicTestnet("ETC/test", "ETC", "Ethereum Classic Testnet"),
    Tron("TRON", "TRX", "Tron"),
    TronTestnet("TRON/test", "TRX", "Tron Testnet"),
    XRP("XRP", "XRP", "XRP Ledger"),
    Polygon("POLYGON", "MATIC", "Polygon"),
    PolygonTestnet("POLYGON/test", "MATIC", "Polygon Testnet"),
    ;

    fun decimals(): Int = when (this) {
        Unknown -> 0
        Tron, TronTestnet,
        XRP,
        -> 6
        Bitcoin, BitcoinTestnet,
        Binance, BinanceTestnet,
        Dogecoin,

        -> 8
        Arbitrum, ArbitrumTestnet,
        Ethereum, EthereumTestnet,
        EthereumClassic, EthereumClassicTestnet,
        BSC, BSCTestnet,
        Polygon, PolygonTestnet,
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
            Arbitrum, ArbitrumTestnet,
            Ethereum, EthereumTestnet,
            EthereumClassic, EthereumClassicTestnet,
            BSC, BSCTestnet,
            Polygon, PolygonTestnet
            -> EthereumAddressService()
            Binance -> BinanceAddressService()
            BinanceTestnet -> BinanceAddressService(true)
            Tron, TronTestnet -> TronAddressService()
            XRP -> XrpAddressService()
            Unknown -> throw Exception("unsupported blockchain")
        }
    }
}