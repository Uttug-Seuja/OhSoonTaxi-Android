package com.example.myapplication.repository

import android.app.Application
import com.example.myapplication.data.MyInfoResponse
import com.example.myapplication.data.ReservesListResponse
import com.example.myapplication.network.BaseResult
import com.example.myapplication.network.RetrofitObject
import com.example.myapplication.network.handleResult

class MyInfoRepository(application: Application) {

    // singleton pattern
    companion object {
        private var instance: MyInfoRepository? = null

        fun getInstance(application: Application): MyInfoRepository? {
            if (instance == null) instance = MyInfoRepository(application)
            return instance
        }
    }

    // 내 정보
    suspend fun retrofitGetUserMyInfo(userUid: String): BaseResult<MyInfoResponse> {
        return handleResult { RetrofitObject.getRetrofitService.getUserMyInfo(userUid) }
    }

    // 내가 만든 게시글 조회
    suspend fun retrofitGetReservesListReservations(userUid: String): BaseResult<ReservesListResponse> {
        return handleResult { RetrofitObject.getRetrofitService.getReservesListReservations(userUid) }
    }

    // 내가 참여한 게시글 조회
    suspend fun retrofitGetReservesListParticipations(userUid: String): BaseResult<ReservesListResponse> {
        return handleResult {
            RetrofitObject.getRetrofitService.getReservesListParticipations(
                userUid
            )
        }
    }

    // 내가 참여한 게시글 조회
    suspend fun retrofitPostUserSignOut(userUid: String): BaseResult<Unit> {
        return handleResult { RetrofitObject.getRetrofitService.postUserSignOut(userUid) }
    }

}