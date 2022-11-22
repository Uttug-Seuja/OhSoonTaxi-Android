package com.example.myapplication.network

import android.util.Log
import com.example.myapplication.common.utils.API
import com.example.myapplication.common.utils.API.POST_USERS_CHECK_UNIQUE
import com.example.myapplication.common.utils.API.POST_USERS_SIGNIN
import com.example.myapplication.common.utils.API.POST_USERS_SIGNUP
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object RetrofitObject {
    var authorizationToken: String = ""
    var refreshToken: String = ""

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .addInterceptor {

            val original = it.request()
            if (original.url.encodedPath.equals(POST_USERS_SIGNUP, true)
                || original.url.encodedPath.equals(POST_USERS_SIGNIN, true)
                || original.url.encodedPath.equals(POST_USERS_CHECK_UNIQUE, true)
            ) {

                val response = it.proceed(original)

                Log.d("tttdasd1111", response.toString())
                Log.d("tttdasd2222", response.headers["Authorization"].toString())
                authorizationToken = response.headers["Authorization"].toString()
                refreshToken = response.headers["RefreshToken"].toString()
                response

            } else {

                val request = original.newBuilder().apply {
                    addHeader("Authorization", authorizationToken)
                        .addHeader("RefreshToken", refreshToken)
                }.build()

                val response = it.proceed(request)

                Log.d("tttdasd1", response.toString())
                Log.d("tttdasd2", response.headers.toString())

                response
            }
            // Request
//            val request = it.request()
//                .newBuilder()
//                .addHeader("Authorization", "Bearer $token")
//                .addHeader("RefreshToken", "Bearer $token")
//                .build()

//            Log.d("tttdasd1",  request.toString())


            // Response
//            val response = it.proceed(original)
//            Log.d("tttdasd",  response.headers.toString())
//
//
//            response
        }.build()


    private val getRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl(API.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val getRetrofitService: RetrofitInterface by lazy {
        getRetrofit.create(RetrofitInterface::class.java)
    }
}