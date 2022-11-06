package com.example.myapplication.network

import com.example.myapplication.utils.API
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitObject {
    var token: String = ""
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .addInterceptor {
            // Request
            val request = it.request()
                .newBuilder()
                .addHeader("Authorization", token)
                .build()

            // Response
            val response = it.proceed(request)
            response
        }.build()


    private val getRetrofit by lazy{
        Retrofit.Builder()
            .baseUrl(API.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val getRetrofitService : RetrofitInterface by lazy{
        getRetrofit.create(RetrofitInterface::class.java)
    }
}