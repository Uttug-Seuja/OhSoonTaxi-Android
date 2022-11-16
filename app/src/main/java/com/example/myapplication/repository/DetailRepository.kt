package com.example.myapplication.repository

import android.app.Application
import com.example.myapplication.data.Participation
import com.example.myapplication.data.ReservesEdit
import com.example.myapplication.data.ReservesInfo
import com.example.myapplication.network.BaseResult
import com.example.myapplication.network.RetrofitObject
import com.example.myapplication.network.handleResult
import com.google.gson.JsonObject

class DetailRepository(application: Application) {

    // 경기 정보
    suspend fun retrofitReservesInfo(reserveId: Int): BaseResult<ReservesInfo> {
        return handleResult{ RetrofitObject.getRetrofitService.getReservesInfo(reserveId)}
    }
    // 경기 삭제
    suspend fun retrofitDeleteReserves(reserveId: Int): BaseResult<Unit> {
        return handleResult{ RetrofitObject.getRetrofitService.getReservesInfo(reserveId)}
    }

    // 경기 참여
    suspend fun retrofitPostParticipation(participation: Participation): BaseResult<Unit> {
        return handleResult{ RetrofitObject.getRetrofitService.postParticipation(participation)}
    }

    // 경기 참여 취소
    suspend fun retrofitDeleteParticipation(participation: Participation): BaseResult<Unit> {
        return handleResult{ RetrofitObject.getRetrofitService.deleteParticipation(participation)}
    }


    // singleton pattern
    companion object {
        private var instance: DetailRepository? = null

        fun getInstance(application: Application): DetailRepository? {
            if (instance == null) instance = DetailRepository(application)
            return instance
        }
    }


}