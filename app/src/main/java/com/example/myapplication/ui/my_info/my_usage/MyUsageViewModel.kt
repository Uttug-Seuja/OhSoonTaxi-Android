package com.example.myapplication.ui.my_info.my_usage

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.repository.MyInfoRepository

class MyUsageViewModel(private val repository: MyInfoRepository) : ViewModel() {


    class Factory(private val application: Application) :
        ViewModelProvider.Factory { // factory pattern
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MyUsageViewModel(MyInfoRepository.getInstance(application)!!) as T
        }
    }
}