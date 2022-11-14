package com.example.myapplication.repository

import android.app.Application
import android.util.Log
import com.example.myapplication.data.*
import com.example.myapplication.mapper.toDomain
import com.example.myapplication.network.BaseResult
import com.example.myapplication.network.RetrofitObject
import com.example.myapplication.network.handleResult
import com.google.gson.annotations.SerializedName

class CreateRepository(application: Application) {

    suspend fun retrofitReservesEdit(reservesEdit: ReservesEdit): BaseResult<Unit> {
        return handleResult{ RetrofitObject.getRetrofitService.postReservesEdit(reservesEdit)}


    }

    suspend fun retrofitReservesCreation(reservesCreation: ReservesCreation): BaseResult<Unit> {

        return handleResult{ RetrofitObject.getRetrofitService.postReservesCreation(reservesCreation)}

    }


    // singleton pattern
    companion object {
        private var instance: CreateRepository? = null

        fun getInstance(application: Application): CreateRepository? {
            if (instance == null) instance = CreateRepository(application)
            return instance
        }
    }


}