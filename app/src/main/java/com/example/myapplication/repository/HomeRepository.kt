package com.example.myapplication.repository

import android.app.Application
import android.util.Log


class HomeRepository (application : Application) {


    // singleton pattern
    companion object {
        private var instance: HomeRepository? = null

        fun getInstance(application : Application): HomeRepository? {
            if (instance == null) instance = HomeRepository(application)
            return instance
        }
    }

    // Use Retrofit
    suspend fun retrofitReservesSportDate(sport: String, today: String) {
//        val response = RetrofitObject.getRetrofitService.getReservesSportDate(sport, today)
        Log.d("ttt", today.toString())

        Log.d("ttt", "tttt")


//        return if (response.isSuccessful) response.body() as ReservesSportDate else ReservesSportDate(ArrayList())

    }



}