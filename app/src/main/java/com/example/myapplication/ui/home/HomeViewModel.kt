package com.example.myapplication.ui.home

import android.app.Application
import android.database.sqlite.SQLiteException
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.ui.common.base.BaseViewModel
import com.example.myapplication.data.Login
import com.example.myapplication.data.ReservesListResponse
import com.example.myapplication.network.onError
import com.example.myapplication.network.onSuccess
import com.example.myapplication.repository.HomeRepository
import com.example.myapplication.ui.signin.SignInNavigationAction
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class HomeViewModel (private val repository: HomeRepository) : BaseViewModel(){

    private val _navigationEvent: MutableSharedFlow<HomeNavigationAction> = MutableSharedFlow()
    val navigationEvent: SharedFlow<HomeNavigationAction> = _navigationEvent

    private val _reservesSportDateEvent: MutableSharedFlow<ReservesListResponse> = MutableSharedFlow()
    val reservesSportDateEvent: SharedFlow<ReservesListResponse> = _reservesSportDateEvent

    init {
        Log.d("ttt", "123123")
    }

    // 날짜 별로 약속 조회
    fun reservesDateRetrofit(reservationDate: String) = viewModelScope.launch {

            baseViewModelScope.launch {
                repository.retrofitReservesSportDate(reservationDate)
                    .onSuccess {
                        _reservesSportDateEvent.emit(it)
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

    fun onClickedSearch() {
        baseViewModelScope.launch {
            _navigationEvent.emit(HomeNavigationAction.NavigateToSearch)
        }
    }


    // factory pattern
    class Factory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HomeViewModel(HomeRepository.getInstance(application)!!) as T
        }
    }


}