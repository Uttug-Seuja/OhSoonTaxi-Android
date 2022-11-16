package com.example.myapplication.ui.detail

import android.app.Application
import android.database.sqlite.SQLiteException
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.common.base.BaseViewModel
import com.example.myapplication.data.Participation
import com.example.myapplication.data.ReservesInfo
import com.example.myapplication.data.ReservesSportDate
import com.example.myapplication.network.onError
import com.example.myapplication.network.onSuccess
import com.example.myapplication.repository.DetailRepository
import com.example.myapplication.repository.SearchRepository
import com.example.myapplication.ui.home.HomeNavigationAction
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import java.io.IOException

class DetailViewModel(private val repository: DetailRepository) : BaseViewModel() {

    private val _navigationEvent: MutableSharedFlow<DetailNavigationAction> = MutableSharedFlow()
    val navigationEvent: SharedFlow<DetailNavigationAction> = _navigationEvent

    private val _retrofitReservesInfoEvent: MutableSharedFlow<ReservesInfo> = MutableSharedFlow()
    val retrofitReservesInfoEvent: SharedFlow<ReservesInfo> = _retrofitReservesInfoEvent

    init {
        reservesInfoRetrofit(1000)
    }

    fun reservesInfoRetrofit(reserveId: Int) = viewModelScope.launch {

        baseViewModelScope.launch {
            repository.retrofitReservesInfo(reserveId)
                .onSuccess {
                    _retrofitReservesInfoEvent.emit(it)
                }
                .onError { e ->
                    Log.d("ttt", e.toString())
                    when (e) {
                        is IOException -> _toastMessage.emit(e.toString())
                        else -> _toastMessage.emit("시스템 에러가 발생 하였습니다.")
                    }
                }
        }
    }

    fun deleteReservesRetrofit(reserveId: Int) = viewModelScope.launch {

        baseViewModelScope.launch {
            repository.retrofitDeleteReserves(reserveId)
                .onSuccess {
                    _navigationEvent.emit(DetailNavigationAction.NavigateToBackNav)
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

    fun deleteParticipationRetrofit(participation: Participation) = viewModelScope.launch {

        baseViewModelScope.launch {
            repository.retrofitDeleteParticipation(participation)
                .onSuccess {
                    reservesInfoRetrofit(participation.reserveId)

                }
                .onError { e ->
                    Log.d("ttt", e.toString())
                    when (e) {
                        is IOException -> _exception.emit(e)
                        else -> _exception.emit(e)
                    }
                }
        }
    }

    fun postParticipationRetrofit(participation: Participation) = viewModelScope.launch {

        baseViewModelScope.launch {
            repository.retrofitPostParticipation(participation)
                .onSuccess {
                    reservesInfoRetrofit(participation.reserveId)

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
            return DetailViewModel(DetailRepository.getInstance(application)!!) as T
        }
    }
}