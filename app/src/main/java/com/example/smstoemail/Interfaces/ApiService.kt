package com.example.smstoemail.Interfaces

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("/validate-and-create-user")
    fun validateAndCreateUser(@Query("idToken") idToken: String): Call<ResponseBody>
}