package com.example.myapplication.ui.update

import android.app.Application
import android.database.sqlite.SQLiteException
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.ui.common.base.BaseViewModel
import com.example.myapplication.network.onError
import com.example.myapplication.network.onSuccess
import com.example.myapplication.repository.CreateRepository
import com.example.myapplication.data.ReservesCreation
import com.example.myapplication.data.ReservesEdit
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class UpdateViewModel(private val repository: CreateRepository) : BaseViewModel() {

    private val _navigationEvent: MutableSharedFlow<UpdateNavigationAction> = MutableSharedFlow()
    val navigationEvent: SharedFlow<UpdateNavigationAction> = _navigationEvent

    lateinit var titleEvent: MutableStateFlow<String>

    init {

        viewModelScope.launch {
            titleEvent = MutableStateFlow("")

        }
    }


    fun onCreatePromiseClicked(reserveId : Int, userId : String) {
        viewModelScope.launch {
            Log.d("ttt", titleEvent.value.toString())

            if (titleEvent.value.isNotEmpty()) {
                baseViewModelScope.launch {
                    repository.retrofitReservesEdit(
                        ReservesEdit(
                            reservationId = reserveId,
                            title = titleEvent.value

                        ),
                        userId = userId

                    ).onSuccess {
                            _navigationEvent.emit(UpdateNavigationAction.NavigateToDetail)

                        }
                        .onError { e ->
                            Log.d("ttt", e.toString())
                            when (e) {
                                is SQLiteException -> _toastMessage.emit("????????? ????????? ????????? ?????????????????????.")
                                else -> _toastMessage.emit("????????? ????????? ?????? ???????????????.")
                            }
                        }
                }
            } else {
                baseViewModelScope.launch {
                    _toastMessage.emit("??? ??? ?????? ??????????????????")
                }
            }
        }
    }

    // factory pattern
    class Factory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return UpdateViewModel(CreateRepository.getInstance(application)!!) as T
        }
    }


}