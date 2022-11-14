package com.example.myapplication.ui.signup

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.User
import com.example.myapplication.repository.UserRepository
import kotlinx.coroutines.launch

class SignupViewModel(private val repository: UserRepository) : ViewModel(){
    private val _retrofitSignUpText = MutableLiveData<Boolean>()


    val retrofitSignUpText: MutableLiveData<Boolean>
        get() = _retrofitSignUpText


    fun signUpRetrofit(user : User) = viewModelScope.launch{
//        _retrofitSignUpText.value = repository.retrofitSignUp(user)

    }


    // factory pattern

    class Factory(val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SignupViewModel(UserRepository.getInstance(application)!!) as T
        }
    }


}