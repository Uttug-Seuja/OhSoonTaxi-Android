package com.example.myapplication.ui.my_info

import android.app.Application
import android.database.sqlite.SQLiteException
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.ui.common.base.BaseViewModel
import com.example.myapplication.data.MyInfoResponse
import com.example.myapplication.data.ReservesListResponse
import com.example.myapplication.network.onError
import com.example.myapplication.network.onSuccess
import com.example.myapplication.repository.MyInfoRepository
import com.example.myapplication.ui.home.HomeNavigationAction
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class MyInfoViewModel(private val repository: MyInfoRepository) : BaseViewModel() {

    private val _navigationEvent: MutableSharedFlow<MyInfoNavigationAction> = MutableSharedFlow()
    val navigationEvent: SharedFlow<MyInfoNavigationAction> = _navigationEvent

    private val _retrofitGetMyInfoEvent: MutableSharedFlow<MyInfoResponse> = MutableSharedFlow()
    val retrofitGetMyInfoEvent: SharedFlow<MyInfoResponse> = _retrofitGetMyInfoEvent


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

    // 내 정보
    fun retrofitGetUserMyInfo(userUid : String) = viewModelScope.launch {

        baseViewModelScope.launch {
            repository.retrofitGetUserMyInfo(userUid)
                .onSuccess {
                    _retrofitGetMyInfoEvent.emit(it)
                }
                .onError { e ->
                    Log.d("ttt", e.toString())
                    when (e) {
                        is SQLiteException -> _toastMessage.emit("데이터 베이스 에러가 발생하였습니다.")
                        else -> _toastMessage.emit("시스템 에러가 발생 하였습니다.")
                    }
                }
        }
    }

    // 로그아웃
    fun postUserSignOutRetrofit(userUid : String) = viewModelScope.launch {

        baseViewModelScope.launch {
            repository.retrofitPostUserSignOut(userUid)
                .onSuccess {
                    _navigationEvent.emit(MyInfoNavigationAction.NavigateToSignIn)

                }
                .onError { e ->
                    Log.d("ttt", e.toString())
                    when (e) {
                        is SQLiteException -> _toastMessage.emit("데이터 베이스 에러가 발생하였습니다.")
                        else -> _toastMessage.emit("시스템 에러가 발생 하였습니다.")
                    }
                }
        }
    }
    class Factory(private val application: Application) :
        ViewModelProvider.Factory { // factory pattern
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MyInfoViewModel(MyInfoRepository.getInstance(application)!!) as T
        }
    }
}