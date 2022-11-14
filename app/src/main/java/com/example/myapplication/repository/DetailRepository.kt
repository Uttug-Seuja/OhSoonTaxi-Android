package com.example.myapplication.repository

import android.app.Application

class DetailRepository(application: Application) {

    // singleton pattern
    companion object {
        private var instance: DetailRepository? = null

        fun getInstance(application: Application): DetailRepository? {
            if (instance == null) instance = DetailRepository(application)
            return instance
        }
    }


}