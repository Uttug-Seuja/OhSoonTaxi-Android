package com.example.myapplication.ui.create

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.Promise
import com.example.myapplication.repository.CreateRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CreateViewModel(private val repository: CreateRepository) : ViewModel() {


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


    fun onCreatePromiseClicked() {
        viewModelScope.launch {
            Log.d("ttt", titleEvent.value.toString())
            Log.d("ttt", reservationTimeEvent.value.toString())
            Log.d("ttt", genderEvent.value.toString())

            if (titleEvent.value.isNotEmpty() && startPlaceEvent.value.isNotEmpty() && destinationEvent.value.isNotEmpty() && seatEvent.value.isNotEmpty()
                && genderEvent.value != "모집 성별" && dateEvent.value != "탑승 날짜" && reservationTimeEvent.value != "탑승 시간"
            ) {
                val promise = Promise(
                    title = titleEvent.value,
                    startPlace = startPlaceEvent.value,
                    destination = destinationEvent.value,
                    seat = seatEvent.value,
                    gender = genderEvent.value,
                    date = dateEvent.value,
                    reservationTime = reservationTimeEvent.value

                )
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