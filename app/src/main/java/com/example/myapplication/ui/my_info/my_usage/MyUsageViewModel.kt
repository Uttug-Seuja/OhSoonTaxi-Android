package com.example.myapplication.ui.my_info.my_usage

import android.app.Application
import android.database.sqlite.SQLiteException
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.common.base.BaseViewModel
import com.example.myapplication.data.ReservesListResponse
import com.example.myapplication.network.onError
import com.example.myapplication.network.onSuccess
import com.example.myapplication.repository.MyInfoRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class MyUsageViewModel(private val repository: MyInfoRepository) : BaseViewModel() {

    private val _retrofitReservesListResponseEvent: MutableSharedFlow<ReservesListResponse> = MutableSharedFlow()
    val retrofitReservesListResponseEvent: SharedFlow<ReservesListResponse> = _retrofitReservesListResponseEvent

    init {
        Log.d("ttt", "123123")
    }

    // 내가 신청한 게시글
    fun reservesListParticipationsRetrofit(userUid : String) = viewModelScope.launch {

        baseViewModelScope.launch {
            repository.retrofitGetReservesListParticipations(userUid)
                .onSuccess {
                    _retrofitReservesListResponseEvent.emit(it)
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
            return MyUsageViewModel(MyInfoRepository.getInstance(application)!!) as T
        }
    }
}