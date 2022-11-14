package com.example.myapplication.ui.signin

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.Login
import com.example.myapplication.data.*
import com.example.myapplication.repository.UserRepository
import kotlinx.coroutines.launch

class SigninViewModel(private val repository: UserRepository) : ViewModel(){
    private val _retrofitSignInText = MutableLiveData<Int>()

    val retrofitSignInText: MutableLiveData<Int>
        get() = _retrofitSignInText



    fun signInRetrofit(login : Login) = viewModelScope.launch{
//        _retrofitSignInText.value = repository.retrofitSignIn(login)

    }


    // factory pattern
    class Factory(val application: Application) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SigninViewModel(UserRepository.getInstance(application)!!) as T
        }
    }
}