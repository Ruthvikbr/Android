package com.example.biometriclogin

import android.content.Context
import android.content.Intent
import android.hardware.biometrics.BiometricManager
import android.hardware.biometrics.BiometricPrompt
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.example.biometriclogin.utils.CIPHERTEXT_WRAPPER
import com.example.biometriclogin.utils.CryptographyManager
import com.example.biometriclogin.utils.SHARED_PREFS_FILENAME

class MainActivity : AppCompatActivity() {

    private lateinit var biometricPrompt: BiometricPrompt
    private val cryptographyManager = CryptographyManager()
    private val ciphertextWrapper
        get() = cryptographyManager.getCiphertextWrapperFromSharedPrefs(
            applicationContext,
            SHARED_PREFS_FILENAME,
            Context.MODE_PRIVATE,
            CIPHERTEXT_WRAPPER
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val useBiometricsTv = findViewById<TextView>(R.id.useBiometrics)
        val canAuthenticate = androidx.biometric.BiometricManager.from(applicationContext).canAuthenticate(
            androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG )

        if (canAuthenticate == BiometricManager.BIOMETRIC_SUCCESS){
            useBiometricsTv.visibility = View.VISIBLE

            useBiometricsTv.setOnClickListener {
                if(ciphertextWrapper!=null){
                    showBiometricPromptForDecryption()
                }else{
                    startActivity(Intent(this,EnableBiometricLoginActivity::class.java))
                }
            }
        } else{
            useBiometricsTv.visibility = View.INVISIBLE
        }

        if (ciphertextWrapper == null) {
            setupForLoginWithPassword()
        }

    }


    private fun setupForLoginWithPassword() {
        TODO("Not yet implemented")
    }


    private fun showBiometricPromptForDecryption() {
        TODO("Not yet implemented")
    }
}