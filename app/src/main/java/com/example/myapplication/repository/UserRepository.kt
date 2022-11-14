package com.example.myapplication.repository

import android.app.Application

class UserRepository (application: Application) {

    // singleton pattern
    companion object {
        private var instance: UserRepository? = null

        fun getInstance(application: Application): UserRepository? {
            if (instance == null) instance = UserRepository(application)
            return instance
        }
    }


}