package com.example.myapplication.ui.common.utils


import android.app.Application
import com.example.myapplication.ui.common.base.DataStoreModule

class MyApplication : Application(){

    private lateinit var dataStore : DataStoreModule

    companion object {
        private lateinit var sampleApplication: MyApplication
        fun getInstance() : MyApplication = sampleApplication
    }

    override fun onCreate() {
        super.onCreate()
        sampleApplication = this
        dataStore = DataStoreModule(this)
    }

    fun getDataStore() : DataStoreModule = dataStore
}