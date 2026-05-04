package com.example.islamiapp.network

import com.example.islamiapp.model.radio.RadioResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {

    @GET("radios")
    suspend fun getRadios(
        @Query("language") language: String = "eng"
    ): Response<RadioResponse>
}