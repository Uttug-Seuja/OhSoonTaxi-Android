package com.example.myapplication.ui.home

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.ReservesSportDate
import com.example.myapplication.repository.HomeRepository
import kotlinx.coroutines.launch

class HomeViewModel (private val repository: HomeRepository) : ViewModel(){
    private val _reservesSportDateText = MutableLiveData<ReservesSportDate>()


    val reservesSportDateText: MutableLiveData<ReservesSportDate>
        get() = _reservesSportDateText


    fun retrofitReservesInfoRetrofit(sport: String, today: String) = viewModelScope.launch{
//        _reservesSportDateText.value = repository.retrofitReservesSportDate(sport, today)

    }


    // factory pattern
    class Factory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HomeViewModel(HomeRepository.getInstance(application)!!) as T
        }
    }


}