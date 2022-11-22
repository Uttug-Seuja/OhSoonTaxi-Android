package com.example.myapplication.repository

import android.app.Application
import android.util.Log
import com.example.myapplication.data.Login
import com.example.myapplication.data.User
import com.example.myapplication.mapper.toDomain
import com.example.myapplication.network.BaseResult
import com.example.myapplication.network.RetrofitObject
import com.example.myapplication.network.handleResult
import com.google.gson.JsonObject

class UserRepository(application: Application) {

    // 로그인
    suspend fun retrofitSignIn(login: Login): BaseResult<Unit> {
//        Log.d(
//            "ttt",
//            handleResult {
//                RetrofitObject.getRetrofitService.postUsersLogin(login).toString()
//            }.toString()
//        )
        return handleResult { RetrofitObject.getRetrofitService.postUsersLogin(login) }

    }

    // 회원가입
    suspend fun retrofitSignUp(user: User): BaseResult<Unit> {

        return handleResult { RetrofitObject.getRetrofitService.postUsersCreation(user) }
    }

    // 아이디 중복확인
    suspend fun retrofitPostUsersCheckUnique(uid: String): BaseResult<Unit> {

//        Log.d("tttt", handleResult {
//            RetrofitObject.getRetrofitService.postUsersCheckUnique(uid)
//        }.toString())
        return handleResult {
            RetrofitObject.getRetrofitService.postUsersCheckUnique(uid)
        }

    }


    // singleton pattern
    companion object {
        private var instance: UserRepository? = null

        fun getInstance(application: Application): UserRepository? {
            if (instance == null) instance = UserRepository(application)
            return instance
        }
    }


}