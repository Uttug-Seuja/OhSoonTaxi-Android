package com.example.myapplication.ui.create

import android.app.Application
import android.database.sqlite.SQLiteException
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.myapplication.common.GlobalApplication
import com.example.myapplication.common.base.BaseViewModel
import com.example.myapplication.data.Participation
import com.example.myapplication.network.onError
import com.example.myapplication.network.onSuccess
import com.example.myapplication.repository.CreateRepository
import com.example.myapplication.data.ReservesCreation
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CreateViewModel(private val repository: CreateRepository) : BaseViewModel() {

    private val _navigationEvent: MutableSharedFlow<CreateNavigationAction> = MutableSharedFlow()
    val navigationEvent: SharedFlow<CreateNavigationAction> = _navigationEvent
    private val genderMap =
        hashMapOf<String, String>("남녀 모두" to "ALL", "남자만" to "MAN", "여자만" to "WOMAN")

    lateinit var titleEvent: MutableStateFlow<String>
    lateinit var startPlaceEvent: MutableStateFlow<String>
    lateinit var destinationEvent: MutableStateFlow<String>
    lateinit var seatEvent: MutableStateFlow<String>
    lateinit var genderEvent: MutableStateFlow<String>
    lateinit var dateEvent: MutableStateFlow<String>
    lateinit var reservationTimeEvent: MutableStateFlow<String>
    lateinit var challengeWordEvent: MutableStateFlow<String>
    lateinit var countersignWordEvent: MutableStateFlow<String>
    lateinit var passengerNumEvent: MutableStateFlow<String>

    lateinit var startLatitudeEvent: MutableStateFlow<Double>
    lateinit var startLongitudeEvent: MutableStateFlow<Double>
    lateinit var finishLatitudeEvent: MutableStateFlow<Double>
    lateinit var finishLongitudeEvent: MutableStateFlow<Double>
    lateinit var userUid: String


    init {

        viewModelScope.launch {
            titleEvent = MutableStateFlow("")
            startPlaceEvent = MutableStateFlow("")
            destinationEvent = MutableStateFlow("")
            seatEvent = MutableStateFlow("좌석 선택")
            genderEvent = MutableStateFlow("모집 성별")
            dateEvent = MutableStateFlow("약속 날짜")
            reservationTimeEvent = MutableStateFlow("약속 시간")
            passengerNumEvent = MutableStateFlow("모집 인원을 알려주세요")
            challengeWordEvent = MutableStateFlow("")
            countersignWordEvent = MutableStateFlow("")
            startLatitudeEvent = MutableStateFlow(0.0)
            startLongitudeEvent = MutableStateFlow(0.0)
            finishLatitudeEvent = MutableStateFlow(0.0)
            finishLongitudeEvent = MutableStateFlow(0.0)

        }

        viewModelScope.launch {
            GlobalApplication.getInstance().getDataStore().userUid.collect { it ->
                userUid = it

            }
        }
    }


    // 게시글 작성
    fun onCreatePromiseClicked() {
        baseViewModelScope.launch {
            Log.d("ttt", titleEvent.value.toString())
            Log.d("ttt", reservationTimeEvent.value.toString())
            Log.d("ttt", startPlaceEvent.value.toString())
            Log.d("ttt", destinationEvent.value.toString())
            Log.d("ttt", seatEvent.value.toString())
            Log.d("ttt", genderEvent.value.toString())
            Log.d("ttt", dateEvent.value.toString())
            Log.d("ttt", reservationTimeEvent.value.toString())



            if (titleEvent.value.isNotEmpty() && startPlaceEvent.value.isNotEmpty() && destinationEvent.value.isNotEmpty() && seatEvent.value.isNotEmpty()
                && genderEvent.value != "모집 성별" && dateEvent.value != "탑승 날짜" && reservationTimeEvent.value != "탑승 시간"
            ) {
                repository.retrofitReservesCreation(
                    userUid,
                    ReservesCreation(
                        title = titleEvent.value,
                        reserveDate = dateEvent.value,
                        reserveTime = reservationTimeEvent.value.replace("시 ", ":")
                            .replace("분", ":00"),
                        startingPlace = startPlaceEvent.value,
                        destination = destinationEvent.value,
                        sex = genderMap[genderEvent.value]!!,
                        passengerNum = passengerNumEvent.value.toInt(),
                        challengeWord = challengeWordEvent.value,
                        countersignWord = countersignWordEvent.value,
                        startLatitude = startLatitudeEvent.value,
                        startLongitude = startLongitudeEvent.value,
                        finishLatitude = finishLatitudeEvent.value,
                        finishLongitude = finishLongitudeEvent.value
                    )
                )

//                seatEvent.value[0].code,
                    .onSuccess {
                        _navigationEvent.emit(CreateNavigationAction.NavigateToHome)
//                        postParticipationRetrofit(userUid, seatEvent.value[0])
                    }
                    .onError { e ->
                        Log.d("ttt", e.toString())
                        when (e) {
                            is SQLiteException -> _toastMessage.emit("데이터 베이스 에러가 발생하였습니다.")
                            else -> _toastMessage.emit("시스템 에러가 발생 하였습니다.")
                        }
                    }

            } else {
                baseViewModelScope.launch {
                    _toastMessage.emit("빈 곳 없이 작성해주세요")
                }
            }
        }
    }

    // 참여 신청
    fun postParticipationRetrofit(userId: String, participation: Participation) =
        viewModelScope.launch {

            baseViewModelScope.launch {
                repository.retrofitPostParticipation(userId, participation)
                    .onSuccess {

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

    // factory pattern
    class Factory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CreateViewModel(CreateRepository.getInstance(application)!!) as T
        }
    }


}