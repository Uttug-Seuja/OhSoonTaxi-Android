package com.example.myapplication.ui.detail

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.repository.DetailRepository
import com.example.myapplication.repository.SearchRepository
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: DetailRepository) : ViewModel(){
//    private val _retrofitSearchList = MutableLiveData<HashtagName>()
//
//    // LiveData
//    val retrofitSearchList: MutableLiveData<HashtagName>
//        get() = _retrofitSearchList

    fun searchRetrofit(keyword : String) = viewModelScope.launch {
//        retrofitSearchList.value = repository.retrofitSearch(keyword)
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory { // factory pattern
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return DetailViewModel(DetailRepository.getInstance(application)!!) as T
        }
    }


}