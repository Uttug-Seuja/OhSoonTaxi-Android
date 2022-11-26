package com.example.myapplication.ui.detail

import android.app.Application
import android.database.sqlite.SQLiteException
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.ui.common.base.BaseViewModel
import com.example.myapplication.data.*
import com.example.myapplication.network.onError
import com.example.myapplication.network.onSuccess
import com.example.myapplication.repository.DetailRepository
import com.example.myapplication.repository.SearchRepository
import com.example.myapplication.ui.home.HomeNavigationAction
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import java.io.IOException

class DetailViewModel(private val repository: DetailRepository) : BaseViewModel() {

    private val _navigationEvent: MutableSharedFlow<DetailNavigationAction> = MutableSharedFlow()
    val navigationEvent: SharedFlow<DetailNavigationAction> = _navigationEvent

    private val _retrofitReservesEvent: MutableSharedFlow<ReservesResponseData> = MutableSharedFlow()
    val retrofitReservesEvent: SharedFlow<ReservesResponseData> = _retrofitReservesEvent

    private val _retrofitParticipationEvent: MutableSharedFlow<String> = MutableSharedFlow()
    val retrofitParticipationEvent: SharedFlow<String> = _retrofitParticipationEvent

    private val _retrofitPassphraseEvent: MutableSharedFlow<PassphraseResponse> = MutableSharedFlow()
    val retrofitPassphraseEvent: SharedFlow<PassphraseResponse> = _retrofitPassphraseEvent

    var participationCheckEvent: MutableStateFlow<String> = MutableStateFlow("")

    // 게시글 정보 보기
    fun reservesRetrofit(reservationId: Int) = viewModelScope.launch {
        baseViewModelScope.launch {
            repository.retrofitReserves(reservationId)
                .onSuccess {
                    Log.d("Ttt title", it.title.toString())
                    _retrofitReservesEvent.emit(it)
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

    // 게시글 삭제
    fun deleteReservesRetrofit( reserveId: Int, userUid: String) = viewModelScope.launch {

        baseViewModelScope.launch {
            repository.retrofitDeleteReserves(reserveId, userUid)
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

    // 경기 상태 보기
    fun getParticipationCheckRetrofit(reservationId: Int, userUid: String ) = viewModelScope.launch {

        baseViewModelScope.launch {
            repository.retrofitGetParticipationCheck(reservationId, userUid)
                .onSuccess {
                    participationCheckEvent.value = it
                    Log.d("ttt partic", it.toString())
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

    // 참여 삭제
    fun deleteParticipationRetrofit(reservationId: Int, userUid: String) = viewModelScope.launch {

        baseViewModelScope.launch {
            repository.retrofitDeleteParticipation(reservationId, userUid)
                .onSuccess {
                    getParticipationCheckRetrofit(reservationId, userUid)
                    reservesRetrofit(reservationId)


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

    // 참여 신청
    fun postParticipationRetrofit(userId: String, participation: Participation) = viewModelScope.launch {

        baseViewModelScope.launch {
            repository.retrofitPostParticipation(userId, participation)
                .onSuccess {
                    getParticipationCheckRetrofit(participation.reservationId, userId)
                    reservesRetrofit(participation.reservationId)

                }
                .onError { e ->
                    Log.d("ttt", e.toString())
                    when (e) {
                        is SQLiteException -> _toastMessage.emit("데이터 베이스 에러가 발생하였습니다.")
                        else -> _toastMessage.emit("모집 성별이 틀립니다")
                    }
                }
        }
    }

    // 암구호 보기
    fun getReservesPassphraseRetrofit(userId: String, reserveId: Int) = viewModelScope.launch {

        baseViewModelScope.launch {
            repository.retrofitGetReservesPassphrase(userId, reserveId)
                .onSuccess {
                    _retrofitPassphraseEvent.emit(it)

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