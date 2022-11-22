package com.example.myapplication.repository

import android.app.Application
import android.util.Log
import com.example.myapplication.data.ReservesListResponse
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

    // 날짜 별로 약속 조회
    suspend fun retrofitReservesSportDate(reservationDate: String): BaseResult<ReservesListResponse> {

        return handleResult{ RetrofitObject.getRetrofitService.getReservesList(reservationDate)}
    }


}