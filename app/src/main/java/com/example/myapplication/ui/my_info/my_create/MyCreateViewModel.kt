package com.example.myapplication.ui.my_info.my_create

import android.app.Application
import android.database.sqlite.SQLiteException
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.common.base.BaseViewModel
import com.example.myapplication.data.Hashtag
import com.example.myapplication.network.onError
import com.example.myapplication.network.onSuccess
import com.example.myapplication.repository.DetailRepository
import com.example.myapplication.repository.MyInfoRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class MyCreateViewModel(private val repository: MyInfoRepository) : BaseViewModel() {

    private val _retrofitSearchList: MutableSharedFlow<Hashtag> = MutableSharedFlow()
    val retrofitSearchList: SharedFlow<Hashtag> = _retrofitSearchList

    init {
        Log.d("ttt", "123123")
    }

    fun searchRetrofit(keyword : String) = viewModelScope.launch {

        baseViewModelScope.launch {
            repository.retrofitSearch(keyword)
                .onSuccess {
                    _retrofitSearchList.emit(it)
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
            return MyCreateViewModel(MyInfoRepository.getInstance(application)!!) as T
        }
    }
}

