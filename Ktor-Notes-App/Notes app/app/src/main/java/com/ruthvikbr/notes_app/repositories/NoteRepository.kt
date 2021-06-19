package com.ruthvikbr.notes_app.repositories

import android.app.Application
import com.ruthvikbr.notes_app.data.local.NoteDao
import com.ruthvikbr.notes_app.data.remote.NoteApi
import com.ruthvikbr.notes_app.data.remote.requests.AccountRequest
import com.ruthvikbr.notes_app.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NoteRepository @Inject constructor(
    private val noteDao: NoteDao,
    private val noteApi: NoteApi,
    private val context: Application
) {
    suspend fun register(email:String,password:String) = withContext(Dispatchers.IO){
        try {
            val response = noteApi.register(AccountRequest(email,password))
            if(response.isSuccessful && response.body()!!.successful){
                Resource.success(response.body()?.message)
            }else{
                Resource.error( response.body()?.message ?:  response.message(),null)
            }
        }catch (e:Exception){
            Resource.error("Something went wrong. Please try again",null)
        }
    }

    suspend fun login(email:String,password:String) = withContext(Dispatchers.IO){
        try {
            val response = noteApi.register(AccountRequest(email,password))
            if(response.isSuccessful && response.body()!!.successful){
                Resource.success(response.body()?.message)
            }else{
                Resource.error(response.body()?.message ?: response.message(),null)
            }
        }catch (e:Exception){
            Resource.error("Something went wrong. Please try again",null)
        }
    }
}