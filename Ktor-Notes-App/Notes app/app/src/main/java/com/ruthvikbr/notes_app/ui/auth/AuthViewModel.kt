package com.ruthvikbr.notes_app.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ruthvikbr.notes_app.repositories.NoteRepository
import com.ruthvikbr.notes_app.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel() {

    private val registrationStatus = MutableLiveData<Resource<String>>()
    val registerStatus: LiveData<Resource<String>> = registrationStatus

    private val _loginStatus = MutableLiveData<Resource<String>>()
    val loginStatus: LiveData<Resource<String>> = _loginStatus

    fun login(email:String,password:String){
        _loginStatus.postValue(Resource.loading(null))
        if(email.isEmpty() || password.isEmpty() ){
            _loginStatus.postValue(Resource.error("Please enter all the fields",null))
            return
        }
        viewModelScope.launch {
            val result = repository.login(email,password)
            _loginStatus.postValue(result)
        }
    }

    fun register(email:String,password:String,confirmPassword:String){
        registrationStatus.postValue(Resource.loading(null))
        if(email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()){
            registrationStatus.postValue(Resource.error("Please enter all the fields",null))
            return
        } else if(password != confirmPassword){
            registrationStatus.postValue(Resource.error("Passwords do not match",null))
            return
        }
        viewModelScope.launch {
            val result = repository.register(email,password)
            registrationStatus.postValue(result)
        }
    }

}