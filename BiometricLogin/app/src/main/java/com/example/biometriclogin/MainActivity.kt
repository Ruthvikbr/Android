package com.example.biometriclogin

import android.content.Intent
import android.hardware.biometrics.BiometricManager
import android.hardware.biometrics.BiometricPrompt
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity() {
    private val TAG = "BIOMETRIC RESULT TAG"
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var errorTv:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val useBiometricsBtn = findViewById<Button>(R.id.button)
        errorTv = findViewById(R.id.errorTv)
        val canAuthenticate =
            androidx.biometric.BiometricManager.from(applicationContext).canAuthenticate(
                androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
            )

        if (canAuthenticate == BiometricManager.BIOMETRIC_SUCCESS) {

            useBiometricsBtn.setOnClickListener {
                showBiometricPrompt()
            }
        } else {
            useBiometricsBtn.text = "Biometric login unavailable on your device"
        }
    }

    private fun showBiometricPrompt() {
        val biometricPrompt =
           createBiometricPrompt(this)
        val promptInfo = createPromptInfo(this)
        biometricPrompt.authenticate(promptInfo)
    }

    private fun navigate(){
        startActivity(Intent(this,HomeActivity::class.java))
    }

    private fun createBiometricPrompt(
        activity: AppCompatActivity,

        ): androidx.biometric.BiometricPrompt {
        val executor = ContextCompat.getMainExecutor(activity)
        val callback = object : androidx.biometric.BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)

                errorTv.visibility = View.VISIBLE
            }
            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                errorTv.visibility = View.VISIBLE
                errorTv.text = "User rejected login"
            }
            override fun onAuthenticationSucceeded(result: androidx.biometric.BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                errorTv.visibility = View.GONE
                navigate()
            }
        }
        return androidx.biometric.BiometricPrompt(activity, executor, callback)
    }

    private fun createPromptInfo(activity: AppCompatActivity): androidx.biometric.BiometricPrompt.PromptInfo =
        androidx.biometric.BiometricPrompt.PromptInfo.Builder().apply {
            setTitle(activity.getString(R.string.prompt_info_title))
            setConfirmationRequired(false)
            setNegativeButtonText("Cancel")
        }.build()



}