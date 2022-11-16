package com.example.myapplication.repository

import android.app.Application
import com.example.myapplication.data.Hashtag
import com.example.myapplication.network.BaseResult
import com.example.myapplication.network.RetrofitObject
import com.example.myapplication.network.handleResult

class MyInfoRepository (application: Application) {

    // singleton pattern
    companion object {
        private var instance: MyInfoRepository? = null

        fun getInstance(application: Application): MyInfoRepository? {
            if (instance == null) instance = MyInfoRepository(application)
            return instance
        }
    }

    // 경기 검색
    suspend fun retrofitSearch(keyword: String): BaseResult<Hashtag> {
        return handleResult{ RetrofitObject.getRetrofitService.getHashtagName(keyword)}
    }


}