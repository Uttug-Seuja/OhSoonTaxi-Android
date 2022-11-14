package com.example.myapplication.repository

import android.app.Application
import android.util.Log
import com.example.myapplication.data.ReservesSportDate
import com.example.myapplication.network.BaseResult
import com.example.myapplication.network.RetrofitObject
import com.example.myapplication.network.handleResult


class HomeRepository (application : Application) {


    // singleton pattern
    companion object {
        private var instance: HomeRepository? = null

        fun getInstance(application : Application): HomeRepository? {
            if (instance == null) instance = HomeRepository(application)
            return instance
        }
    }

    suspend fun retrofitReservesSportDate(sport: String, today: String): BaseResult<ReservesSportDate> {

        return handleResult{ RetrofitObject.getRetrofitService.getReservesSportDate(sport, today)}
    }


}