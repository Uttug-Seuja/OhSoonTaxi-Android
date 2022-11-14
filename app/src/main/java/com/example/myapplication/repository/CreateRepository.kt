package com.example.myapplication.repository

import android.app.Application

class CreateRepository(application: Application) {

    // singleton pattern
    companion object {
        private var instance: CreateRepository? = null

        fun getInstance(application: Application): CreateRepository? {
            if (instance == null) instance = CreateRepository(application)
            return instance
        }
    }


}