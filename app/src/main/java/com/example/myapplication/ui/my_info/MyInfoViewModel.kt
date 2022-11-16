package com.example.myapplication.ui.my_info

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.common.base.BaseViewModel
import com.example.myapplication.repository.MyInfoRepository
import com.example.myapplication.ui.home.HomeNavigationAction
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class MyInfoViewModel(private val repository: MyInfoRepository) : BaseViewModel() {

    private val _navigationEvent: MutableSharedFlow<MyInfoNavigationAction> = MutableSharedFlow()
    val navigationEvent: SharedFlow<MyInfoNavigationAction> = _navigationEvent


    fun onClickedMyCreate() {
        baseViewModelScope.launch {
            _navigationEvent.emit(MyInfoNavigationAction.NavigateToMyCreate)
        }
    }
    fun onClickedMyUsage() {
        baseViewModelScope.launch {
            _navigationEvent.emit(MyInfoNavigationAction.NavigateToMyUsage)
        }
    }
    fun onClickedMyInterest() {
        baseViewModelScope.launch {
            _navigationEvent.emit(MyInfoNavigationAction.NavigateToMyInterest)
        }
    }

    class Factory(private val application: Application) :
        ViewModelProvider.Factory { // factory pattern
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MyInfoViewModel(MyInfoRepository.getInstance(application)!!) as T
        }
    }
}