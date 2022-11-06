package com.example.myapplication.ui.create

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.Promise
import com.example.myapplication.repository.CreateRepository
import com.example.myapplication.utils.MyApplication
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class CreateViewModel(private val repository: CreateRepository) : ViewModel(){

    lateinit var titleEvent: MutableStateFlow<String>
    lateinit var startPlaceEvent: MutableStateFlow<String>
    lateinit var destinationEvent: MutableStateFlow<String>
    lateinit var seatEvent: MutableStateFlow<String>
    lateinit var genderEvent: MutableStateFlow<String>
    lateinit var dateEvent: MutableStateFlow<String>
    lateinit var reservationTimeEvent: MutableStateFlow<String>
    var promiseFlow = MyApplication.getInstance().getDataStore()


//    init {
//        viewModelScope.launch {
//            titleEvent = MutableStateFlow(promiseFlow.promise.first().title)
//            startPlaceEvent = MutableStateFlow(promiseFlow.promise.first().startPlace)
//            destinationEvent = MutableStateFlow(promiseFlow.promise.first().destination)
//            seatEvent = MutableStateFlow(promiseFlow.promise.first().seat)
//            genderEvent = MutableStateFlow(promiseFlow.promise.first().gender)
//            dateEvent = MutableStateFlow(promiseFlow.promise.first().date)
//            reservationTimeEvent = MutableStateFlow(promiseFlow.promise.first().reservationTime)
//
//        }
//    }

    fun onCreatePromiseClicked() {
        viewModelScope.launch {

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