package com.example.myapplication.network

import android.util.Log
import com.example.myapplication.ui.common.GlobalApplication
import com.example.myapplication.ui.common.utils.API
import com.example.myapplication.ui.common.utils.API.POST_USERS_CHECK_UNIQUE
import com.example.myapplication.ui.common.utils.API.POST_USERS_SIGNIN
import com.example.myapplication.ui.common.utils.API.POST_USERS_SIGNUP
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object RetrofitObject {
    private var authorizationToken: String = ""
    private var refreshToken: String = ""

    init {
        CoroutineScope(Dispatchers.IO).launch {
            GlobalApplication.getInstance().getDataStore().authorization.collect { it ->
                authorizationToken = it
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            GlobalApplication.getInstance().getDataStore().refreshToken.collect { it ->
                refreshToken = it
            }
        }
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .addInterceptor {

            val original = it.request()
            if (original.url.encodedPath.equals(POST_USERS_SIGNIN, true)) {
                val response = it.proceed(original)

                CoroutineScope(Dispatchers.IO).launch {
                    GlobalApplication.getInstance().getDataStore()
                        .setAuthorization(response.headers["Authorization"].toString())
                }

                CoroutineScope(Dispatchers.IO).launch {
                    GlobalApplication.getInstance().getDataStore()
                        .setRefreshToken(response.headers["RefreshToken"].toString())

                }

                Log.d("tttdasd1123", authorizationToken)
                Log.d("tttdasd2123", refreshToken)

                response
            } else if (original.url.encodedPath.equals(
                    POST_USERS_CHECK_UNIQUE,
                    true
                ) || original.url.encodedPath.equals(POST_USERS_SIGNUP, true)
            ) {


                val response = it.proceed(original)
                response

            } else {

                Log.d("tttdasd1", authorizationToken)
                Log.d("tttdasd2", refreshToken)
                val request = original.newBuilder().apply {
                    addHeader("Authorization", authorizationToken)
                        .addHeader("RefreshToken", refreshToken)
                }.build()

                val response = it.proceed(request)



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