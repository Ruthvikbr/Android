package com.example.paymentintegration

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.wallet.*

class MainActivity : AppCompatActivity() {

    private lateinit var gPayButton: Button
    private lateinit var gPayUPIButton: Button
    private lateinit var paymentsClient: PaymentsClient // client using which we can make google pay api calls
    private val loadPaymentDataRequestCode = 1003
    private val GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user"
    private val GOOGLE_PAY_REQUEST_CODE = 123
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        gPayButton = findViewById(R.id.payUsingGoogle)
        gPayUPIButton = findViewById(R.id.payUsingUpi)
        paymentsClient = PaymentsUtil.createPaymentsClient(this)

        val isReadyToPay =
            IsReadyToPayRequest.fromJson(PaymentsUtil.googlePayBaseConfiguration.toString())
        val readyToPayTask = paymentsClient.isReadyToPay(isReadyToPay)
        readyToPayTask.addOnCompleteListener { task ->
            try {
                task.getResult(ApiException::class.java)?.let(::setGooglePayAvailable)
            } catch (exception: ApiException) {
                Log.d("API exception", "" + exception)
            }
        }

        gPayUPIButton.setOnClickListener {
            payUsingUPI(this)
        }

    }

    private fun setGooglePayAvailable(available: Boolean) {
        if (available) {
            gPayButton.visibility = View.VISIBLE
            gPayButton.setOnClickListener {
                makeGPayRequest()
            }
        }
    }


    private fun makeGPayRequest() {
        val paymentDataRequest =
            PaymentDataRequest.fromJson(PaymentsUtil.paymentDataRequestJson.toString())
        AutoResolveHelper.resolveTask(
            paymentsClient.loadPaymentData(paymentDataRequest), this, loadPaymentDataRequestCode
        )
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            loadPaymentDataRequestCode -> {
                when (resultCode) {
                    Activity.RESULT_OK ->
                        PaymentData.getFromIntent(data)?.let(::handlePaymentSuccess)

                    Activity.RESULT_CANCELED -> {
                        // The user cancelled without selecting a payment method.
                    }

                    AutoResolveHelper.RESULT_ERROR -> {
                        AutoResolveHelper.getStatusFromIntent(data)?.let {
                            handleError(it.statusCode)
                        }
                    }
                }
            }
            GOOGLE_PAY_REQUEST_CODE -> {
                when (resultCode) {
                    Activity.RESULT_OK ->
                        PaymentData.getFromIntent(data)?.let(::handlePaymentSuccess)

                    Activity.RESULT_CANCELED -> {
                        // The user cancelled without selecting a payment method.
                    }

                    AutoResolveHelper.RESULT_ERROR -> {
                        AutoResolveHelper.getStatusFromIntent(data)?.let {
                            handleError(it.statusCode)
                        }
                    }
                }
            }
        }
    }

    private fun handleError(statusCode: Int) {
        Log.d("TAG", "" + statusCode)
    }

    private fun handlePaymentSuccess(paymentData: PaymentData) {
        Log.d("TAG", "" + paymentData)
//        val paymentMethodToken = paymentData
//            .getJSONObject("tokenizationData")
//            .getString("token")


    }

    private fun payUsingUPI(activity: Activity) {
        val uri = Uri.Builder()
            .scheme("upi")
            .authority("pay")
            .appendQueryParameter("pa", "merchant UPI ID")
            .appendQueryParameter("pn", "Sample merchant")
            .appendQueryParameter("tn", "Transaction description")
            .appendQueryParameter("am", "100")
            .appendQueryParameter("cu", "INR")
            .build()
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = uri
        intent.setPackage(GOOGLE_PAY_PACKAGE_NAME)
        activity.startActivityForResult(intent, GOOGLE_PAY_REQUEST_CODE)
    }
}