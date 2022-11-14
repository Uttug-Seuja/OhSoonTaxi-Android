package com.example.myapplication.ui.my_info.my_create

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.repository.DetailRepository
import com.example.myapplication.repository.MyInfoRepository
import kotlinx.coroutines.launch

class MyCreateViewModel(private val repository: MyInfoRepository) : ViewModel() {


    class Factory(private val application: Application) :
        ViewModelProvider.Factory { // factory pattern
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MyCreateViewModel(MyInfoRepository.getInstance(application)!!) as T
        }
    }
}

