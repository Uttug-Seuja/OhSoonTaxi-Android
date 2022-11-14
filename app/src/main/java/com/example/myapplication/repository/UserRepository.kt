package com.example.myapplication.repository

import android.app.Application
import android.util.Log
import com.example.myapplication.data.Login
import com.example.myapplication.data.User
import com.example.myapplication.network.BaseResult
import com.example.myapplication.network.RetrofitObject
import com.example.myapplication.network.handleResult

class UserRepository (application: Application) {

    // Use Retrofit
    suspend fun retrofitSignIn(login: Login): BaseResult<Unit> {
        Log.d("ttt repository ", handleResult{ RetrofitObject.getRetrofitService.postUsersLogin(login)}.toString())
        return handleResult{ RetrofitObject.getRetrofitService.postUsersLogin(login)}

    }

    suspend fun retrofitSignUp(user: User): BaseResult<Unit> {

        return handleResult{ RetrofitObject.getRetrofitService.postUsersCreation(user)}
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