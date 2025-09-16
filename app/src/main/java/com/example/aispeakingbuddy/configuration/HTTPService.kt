package com.example.aispeakingbuddy.configuration

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object HTTPService {
    private var retrofit: Retrofit? = null

    fun getInstance(): Retrofit {
        val gson = GsonBuilder().setLenient().create()

        retrofit = Retrofit.Builder()
            .baseUrl(Constant.APP_PATH()) // base URL chung
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        return retrofit!!
    }
}