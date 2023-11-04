package com.example.blockchain_sdk
import androidx.appcompat.app.AppCompatActivity
import android.R
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import com.airchip3.blockchain.common.Blockchain
import com.airchip3.blockchain.common.extensions.toCompressedPublicKey
import com.example.blockchain_sdk.databinding.ActivityMainBinding
import com.example.blockchain_sdk.extensions.hexToBytes
import com.example.blockchain_sdk.extensions.onItemSelected

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var selectedBlockchain = getTestedBlockchains()[0]
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        initWalletsBlockchainContainer()
//        val publicKey = "04F2CB38C984F291E2FF64ECE08E4C59DC78716B55F9E4734F946ACCD6AB56354DC9B1AFE7904E782451CDC8DAAC6569BD6F5D8035195F7EB955FB17B0C98B37BC".hexToBytes()
//        val walletAddresses = Blockchain.Tron.makeAddresses(publicKey.toCompressedPublicKey())
//        walletAddresses.forEach { it ->
//            Log.d("TAG","bitcoin walletAddress:${it.value},type:${it.type}")
//        }

    }

    private fun getTestedBlockchains(): List<Blockchain> {
//        return listOf(
//            Blockchain.Gnosis,
//            Blockchain.Arbitrum,
//            Blockchain.Fantom,
//        )
        return Blockchain.values().toMutableList().apply { remove(Blockchain.Unknown) }.toList()
    }

    private fun initBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun initWalletsBlockchainContainer() = with(binding) {

        fun initSpBlockchain() = with(containerSelectWalletWithBlockchain) {
            val supportedBlockchains = getTestedBlockchains()
            val blockchainsAdapter = ArrayAdapter(
                this@MainActivity,
                R.layout.simple_spinner_item,
                supportedBlockchains.map { it }
            )
            blockchainsAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
            spSelectBlockchain.adapter = blockchainsAdapter
            spSelectBlockchain.onItemSelected<Blockchain> { blockchain, _ ->
                selectedBlockchain = blockchain
            }
        }

        fun initBtnLoadWallet() {
            containerSelectWalletWithBlockchain.btnLoadWallet.setOnClickListener {
                resetWalletAddress()
                val publicKey = "04F2CB38C984F291E2FF64ECE08E4C59DC78716B55F9E4734F946ACCD6AB56354DC9B1AFE7904E782451CDC8DAAC6569BD6F5D8035195F7EB955FB17B0C98B37BC".hexToBytes()
                val walletAddresses = selectedBlockchain.makeAddresses(publicKey.toCompressedPublicKey())
                containerSelectWalletWithBlockchain.tvBlockchainAddresses.text = walletAddresses.first().value
            }
        }

        initSpBlockchain()
        initBtnLoadWallet()
    }

    private fun resetWalletAddress() = with(binding) {
        containerSelectWalletWithBlockchain.tvBlockchainAddresses.text = ""
    }

}