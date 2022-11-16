package com.example.myapplication.repository

import android.app.Application
import com.example.myapplication.data.Hashtag
import com.example.myapplication.network.BaseResult
import com.example.myapplication.network.RetrofitObject
import com.example.myapplication.network.handleResult


/**
ViewMovel에서는 로컬데이터인지 원격데이터인지 신경쓰지않고 Repository를 사용할 수 있다.
result.isSuccessful : 통신에 성공했는지의 여부. 이때의 통신은 갔다 왔는가 그자체를 의미하는 것
result.body : 실질적으로 받게되는 데이터입니다. `as Type`으로 객체 타입을 명시. 여기서는 ModelBoard를 받음.
 **/


class SearchRepository(application : Application) {

    // singleton pattern
    companion object {
        private var instance: SearchRepository? = null

        fun getInstance(application: Application): SearchRepository? {
            if (instance == null) instance = SearchRepository(application)
            return instance
        }
    }

    // 경기 검색
    suspend fun retrofitSearch(keyword: String): BaseResult<Hashtag> {
        return handleResult{ RetrofitObject.getRetrofitService.getHashtagName(keyword)}
    }
}