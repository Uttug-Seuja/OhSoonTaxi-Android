package com.example.myapplication.ui.create

import android.app.Application
import android.database.sqlite.SQLiteException
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.common.base.BaseViewModel
import com.example.myapplication.network.onError
import com.example.myapplication.network.onSuccess
import com.example.myapplication.repository.CreateRepository
import com.example.myapplication.data.ReservesCreation
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CreateViewModel(private val repository: CreateRepository) : BaseViewModel() {

    private val _navigationEvent: MutableSharedFlow<CreateNavigationAction> = MutableSharedFlow()
    val navigationEvent: SharedFlow<CreateNavigationAction> = _navigationEvent

    lateinit var titleEvent: MutableStateFlow<String>
    lateinit var startPlaceEvent: MutableStateFlow<String>
    lateinit var destinationEvent: MutableStateFlow<String>
    lateinit var seatEvent: MutableStateFlow<String>
    lateinit var genderEvent: MutableStateFlow<String>
    lateinit var dateEvent: MutableStateFlow<String>
    lateinit var reservationTimeEvent: MutableStateFlow<String>


    init {

        viewModelScope.launch {
            titleEvent = MutableStateFlow("")
            startPlaceEvent = MutableStateFlow("")
            destinationEvent = MutableStateFlow("")
            seatEvent = MutableStateFlow("좌석 선택")
            genderEvent = MutableStateFlow("모집 성별")
            dateEvent = MutableStateFlow("약속 날짜")
            reservationTimeEvent = MutableStateFlow("약속 시간")

        }
    }


    // 게시글 작성
    fun onCreatePromiseClicked() {
        viewModelScope.launch {
            Log.d("ttt", titleEvent.value.toString())
            Log.d("ttt", reservationTimeEvent.value.toString())
            Log.d("ttt", genderEvent.value.toString())

            if (titleEvent.value.isNotEmpty() && startPlaceEvent.value.isNotEmpty() && destinationEvent.value.isNotEmpty() && seatEvent.value.isNotEmpty()
                && genderEvent.value != "모집 성별" && dateEvent.value != "탑승 날짜" && reservationTimeEvent.value != "탑승 시간"
            ) {

                baseViewModelScope.launch {
                    repository.retrofitReservesCreation(
                        "userUid",
                        ReservesCreation(
                            title = "",
                            reserveDate = "",
                            reserveTime = "",
                            startingPlace = "",
                            destination = "",
                            sex = "",
                            passengerNum = 1,
                            challengeWord = "",
                            countersignWord = "",
                            startLatitude = 0.0,
                            startLongitude = 0.0,
                            finishLatitude = 0.0,
                            finishLongitude = 0.0

                        )
                    )
                        .onSuccess {
                            _navigationEvent.emit(CreateNavigationAction.NavigateToHome)
                        }
                        .onError { e ->
                            Log.d("ttt", e.toString())
                            when (e) {
                                is SQLiteException -> _toastMessage.emit("데이터 베이스 에러가 발생하였습니다.")
                                else -> _toastMessage.emit("시스템 에러가 발생 하였습니다.")
                            }
                        }
                }
            } else {
                baseViewModelScope.launch {
                    _toastMessage.emit("빈 곳 없이 작성해주세요")
                }
            }
        }
    }

    // factory pattern
    class Factory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CreateViewModel(CreateRepository.getInstance(application)!!) as T
        }
    }


}