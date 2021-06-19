package com.ruthvikbr.notes_app.ui.auth

import android.content.SharedPreferences
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.ruthvikbr.notes_app.R
import com.ruthvikbr.notes_app.data.remote.BasicAuthInterceptor
import com.ruthvikbr.notes_app.ui.BaseFragment
import com.ruthvikbr.notes_app.utils.Constants.KEY_LOGGED_IN_EMAIL
import com.ruthvikbr.notes_app.utils.Constants.KEY_LOGGED_IN_PASSWORD
import com.ruthvikbr.notes_app.utils.Constants.NO_EMAIL
import com.ruthvikbr.notes_app.utils.Constants.NO_PASSWORD
import com.ruthvikbr.notes_app.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_auth.*
import javax.inject.Inject

@AndroidEntryPoint
class AuthFragment : BaseFragment(R.layout.fragment_auth) {

    @Inject
    lateinit var sharedPref:SharedPreferences

    @Inject
    lateinit var basicAuthInterceptor: BasicAuthInterceptor

    private var curEmail:String? = null
    private var curPassword:String? = null

    private val viewModel:AuthViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(isUserLoggedIn()){
            authenticateApi(curEmail?:"",curPassword?:"")
            redirect()
        }

        requireActivity().requestedOrientation = SCREEN_ORIENTATION_PORTRAIT
        subscribeToObservers()
        btnRegister.setOnClickListener {
            val email = etRegisterEmail.text.toString()
            val password = etRegisterPassword.text.toString()
            val confirmPassword = etRegisterPasswordConfirm.text.toString()
            curEmail = email
            curPassword = password
            viewModel.register(email, password, confirmPassword)
        }
        btnLogin.setOnClickListener {
            val email = etLoginEmail.text.toString()
            val password = etLoginPassword.text.toString()
            curEmail = email
            curPassword = password
            viewModel.login(email,password)
        }
    }

    private fun authenticateApi(email:String,password:String){
        basicAuthInterceptor.email = email
        basicAuthInterceptor.password = password
    }

    private fun redirect(){
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.authFragment,true)
            .build()
        findNavController().navigate(
            AuthFragmentDirections.actionAuthFragmentToNoteFragment(),
            navOptions
        )
    }

    private fun isUserLoggedIn():Boolean{
        curEmail = sharedPref.getString(KEY_LOGGED_IN_EMAIL, NO_EMAIL)?: NO_EMAIL
        curPassword = sharedPref.getString(KEY_LOGGED_IN_PASSWORD, NO_PASSWORD)?: NO_PASSWORD
        return curEmail!= NO_EMAIL && curPassword!= NO_PASSWORD
    }

    private fun subscribeToObservers(){
        viewModel.loginStatus.observe(viewLifecycleOwner, Observer { result ->
            result?.let {
                when(result.status){
                    Status.SUCCESS -> {
                        loginProgressBar.visibility = View.GONE
                        showSnackBar(result.data?:"Successfully logged in")
                        sharedPref.edit().putString(KEY_LOGGED_IN_EMAIL,curEmail).apply()
                        sharedPref.edit().putString(KEY_LOGGED_IN_PASSWORD,curPassword).apply()
                        authenticateApi(curEmail?:"",curPassword?:"")
                        redirect()
                    }
                    Status.LOADING -> {
                        loginProgressBar.visibility = View.VISIBLE
                    }
                    Status.ERROR -> {
                        loginProgressBar.visibility = View.GONE
                        showSnackBar(result.message?:"Something went wrong")
                    }
                }
            }
        })

        viewModel.registerStatus.observe(viewLifecycleOwner, Observer { result ->
            result?.let {
                when(result.status){
                    Status.SUCCESS -> {
                        registerProgressBar.visibility = View.GONE
                        showSnackBar(result.data?:"Successfully registered")
                        sharedPref.edit().putString(KEY_LOGGED_IN_EMAIL,curEmail).apply()
                        sharedPref.edit().putString(KEY_LOGGED_IN_PASSWORD,curPassword).apply()
                        authenticateApi(curEmail?:"",curPassword?:"")
                        redirect()
                    }
                    Status.LOADING -> {
                        registerProgressBar.visibility = View.VISIBLE
                    }
                    Status.ERROR -> {
                        registerProgressBar.visibility = View.GONE
                        showSnackBar(result.message?:"Something went wrong")
                    }
                }
            }
        })
    }
}