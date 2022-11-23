package com.example.myapplication.repository

import android.app.Application
import com.example.myapplication.data.*
import com.example.myapplication.network.BaseResult
import com.example.myapplication.network.RetrofitObject
import com.example.myapplication.network.handleResult

class CreateRepository(application: Application) {

    // 게시글 생성
    suspend fun retrofitReservesCreation(userUid : String, reservesCreation: ReservesCreation): BaseResult<Unit> {

        return handleResult{ RetrofitObject.getRetrofitService.postReservesAdd(userUid, reservesCreation)}

    }

    // 게시글 수정
    suspend fun retrofitReservesEdit(reservesEdit: ReservesEdit, userId: String): BaseResult<Unit> {
        return handleResult{ RetrofitObject.getRetrofitService.postReservesEdit(userId, reservesEdit)}
    }

    // 경기 참여
    suspend fun retrofitPostParticipation(
        userId: String,
        participation: Participation
    ): BaseResult<Unit> {
        return handleResult {
            RetrofitObject.getRetrofitService.postParticipationAdd(
                userId,
                participation
            )
        }
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