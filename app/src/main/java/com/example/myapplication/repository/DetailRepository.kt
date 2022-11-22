package com.example.myapplication.repository

import android.app.Application
import com.example.myapplication.data.*
import com.example.myapplication.network.BaseResult
import com.example.myapplication.network.RetrofitObject
import com.example.myapplication.network.handleResult
import com.google.gson.JsonObject

class DetailRepository(application: Application) {

    // 경기 정보
    suspend fun retrofitReserves(reserveId: Int): BaseResult<ReservesResponse> {
        return handleResult{ RetrofitObject.getRetrofitService.getReserves(reserveId)}
    }

    // 경기 삭제
    suspend fun retrofitDeleteReserves(userUid: String, reserveId: Int): BaseResult<Unit> {
        return handleResult{ RetrofitObject.getRetrofitService.deleteReserves(userUid, reserveId)}
    }

    // 경기 상태
    suspend fun retrofitGetParticipationCheck(userUid: String, reservationId: Int): BaseResult<Unit> {
        return handleResult{ RetrofitObject.getRetrofitService.getParticipationCheck(userUid, reservationId)}
    }

    // 경기 참여
    suspend fun retrofitPostParticipation(userId: String, participation: Participation): BaseResult<Unit> {
        return handleResult{ RetrofitObject.getRetrofitService.postParticipationAdd(userId, participation)}
    }

    // 경기 참여 취소
    suspend fun retrofitDeleteParticipation(userUid: String, reservationId: Int): BaseResult<Unit> {
        return handleResult{ RetrofitObject.getRetrofitService.deleteParticipation(userUid, reservationId)}
    }

    suspend fun retrofitGetReservesPassphrase(userId: String, reserveId: Int): BaseResult<PassphraseResponse> {
        return handleResult{ RetrofitObject.getRetrofitService.getReservesPassphrase(userId, reserveId)}
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