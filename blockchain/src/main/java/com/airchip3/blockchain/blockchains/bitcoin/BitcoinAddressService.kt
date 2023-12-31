package com.airchip3.blockchain.blockchains.bitcoin

import com.airchip3.blockchain.common.Blockchain
import com.airchip3.blockchain.common.address.Address
import com.airchip3.blockchain.common.address.AddressService
import com.airchip3.blockchain.common.address.AddressType
import com.airchip3.blockchain.common.address.MultisigAddressProvider
import com.airchip3.blockchain.common.card.EllipticCurve
import com.airchip3.blockchain.common.extensions.calculateRipemd160
import com.airchip3.blockchain.common.extensions.calculateSha256
import com.airchip3.blockchain.common.extensions.toCompressedPublicKey
import org.bitcoinj.core.ECKey
import org.bitcoinj.core.LegacyAddress
import org.bitcoinj.core.NetworkParameters
import org.bitcoinj.core.SegwitAddress
import org.bitcoinj.params.MainNetParams
import org.bitcoinj.params.TestNet3Params
import org.bitcoinj.script.Script
import org.bitcoinj.script.ScriptBuilder
import org.libdohj.params.DogecoinMainNetParams

open class BitcoinAddressService(
        private val blockchain: Blockchain
) : AddressService(), MultisigAddressProvider {

    private val networkParameters: NetworkParameters = when (blockchain) {
        Blockchain.Bitcoin -> MainNetParams()
        Blockchain.BitcoinTestnet -> TestNet3Params()
        Blockchain.Dogecoin -> DogecoinMainNetParams()
        else -> throw Exception(
                "${blockchain.fullName} blockchain is not supported by ${this::class.simpleName}"
        )
    }

    override fun makeAddress(walletPublicKey: ByteArray, curve: EllipticCurve?) =
        makeLegacyAddress(walletPublicKey).value

    override fun validate(address: String): Boolean {
        return validateLegacyAddress(address) || validateSegwitAddress(address)
    }

    override fun makeAddresses(walletPublicKey: ByteArray, curve: EllipticCurve?): Set<Address> {
        return when (blockchain) {
            Blockchain.Bitcoin, Blockchain.BitcoinTestnet -> {
                setOf(makeLegacyAddress(walletPublicKey), makeSegwitAddress(walletPublicKey))
            }
            else -> {
                super.makeAddresses(walletPublicKey, curve)
            }
        }
    }

    internal fun makeLegacyAddress(walletPublicKey: ByteArray): Address {
        val ecPublicKey = ECKey.fromPublicOnly(walletPublicKey)
        val address = LegacyAddress.fromKey(networkParameters, ecPublicKey).toBase58()
        return Address(address, AddressType.Legacy)
    }

    private fun makeSegwitAddress(walletPublicKey: ByteArray): Address {
        val compressedPublicKey = ECKey.fromPublicOnly(walletPublicKey.toCompressedPublicKey())
        val address = SegwitAddress.fromKey(networkParameters, compressedPublicKey).toBech32()
        return Address(address, AddressType.Default)
    }

    private fun validateSegwitAddress(address: String): Boolean {
        return try {
//            if (blockchain == Blockchain.Ducatus) return false
            SegwitAddress.fromBech32(networkParameters, address)
            true
        } catch (e: Exception) {
            false
        }
    }

    internal fun validateLegacyAddress(address: String): Boolean {
        return try {
            LegacyAddress.fromBase58(networkParameters, address)
            true
        } catch (e: Exception) {
            false
        }
    }

    override fun makeMultisigAddresses(
            walletPublicKey: ByteArray, pairPublicKey: ByteArray, curve: EllipticCurve?
    ): Set<Address> = make1of2MultisigAddresses(walletPublicKey, pairPublicKey)

    fun make1of2MultisigAddresses(publicKey1: ByteArray, publicKey2: ByteArray): Set<Address> {
        val script = create1of2MultisigOutputScript(publicKey1, publicKey2)
        val legacyAddress = makeLegacyScriptAddress(script)
        val segwitAddress = makeSegwitScriptAddress(script)
        return setOf(legacyAddress, segwitAddress)
    }

    private fun create1of2MultisigOutputScript(publicKey1: ByteArray, publicKey2: ByteArray): Script {
        val publicKeys = mutableListOf(
                publicKey1.toCompressedPublicKey(),
                publicKey2.toCompressedPublicKey()
        )
        val publicEcKeys = publicKeys.map { ECKey.fromPublicOnly(it) }
        return ScriptBuilder.createRedeemScript(1, publicEcKeys)
    }

    private fun makeLegacyScriptAddress(script: Script): BitcoinScriptAddress {
        val scriptHash = script.program.calculateSha256().calculateRipemd160()
        val address = LegacyAddress.fromScriptHash(networkParameters, scriptHash)
        return BitcoinScriptAddress(script, address.toBase58(), AddressType.Legacy)
    }

    private fun makeSegwitScriptAddress(script: Script): BitcoinScriptAddress {
        val scriptHash = script.program.calculateSha256()
        val address = SegwitAddress.fromHash(networkParameters, scriptHash)
        return BitcoinScriptAddress(script, address.toBech32(), AddressType.Default)
    }
}


class BitcoinScriptAddress(
        val script: Script,
        value: String,
        type: AddressType = AddressType.Default
) : Address(value, type)