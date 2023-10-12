package com.example.blockchain_sdk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.airchip3.blockchain.common.Blockchain
import com.airchip3.blockchain.common.extensions.toCompressedPublicKey
import com.example.blockchain_sdk.extensions.hexToBytes

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val publicKey = "04F2CB38C984F291E2FF64ECE08E4C59DC78716B55F9E4734F946ACCD6AB56354DC9B1AFE7904E782451CDC8DAAC6569BD6F5D8035195F7EB955FB17B0C98B37BC".hexToBytes()
        val walletAddresses = Blockchain.Bitcoin.makeAddresses(publicKey.toCompressedPublicKey())
        walletAddresses.forEach { it ->
            Log.d("TAG","bitcoin walletAddress:${it.value},type:${it.type}")
        }

    }
}