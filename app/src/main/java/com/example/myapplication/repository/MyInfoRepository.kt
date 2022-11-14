package com.example.myapplication.repository

import android.app.Application

class MyInfoRepository (application: Application) {

    // singleton pattern
    companion object {
        private var instance: MyInfoRepository? = null

        fun getInstance(application: Application): MyInfoRepository? {
            if (instance == null) instance = MyInfoRepository(application)
            return instance
        }
    }


}