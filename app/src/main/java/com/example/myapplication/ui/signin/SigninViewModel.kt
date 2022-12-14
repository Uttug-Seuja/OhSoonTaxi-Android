package com.example.myapplication.ui.signin

import android.app.Application
import android.database.sqlite.SQLiteException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.ui.common.GlobalApplication
import com.example.myapplication.ui.common.base.BaseViewModel
import com.example.myapplication.data.Login
import com.example.myapplication.network.onError
import com.example.myapplication.network.onSuccess
import com.example.myapplication.repository.UserRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class SigninViewModel(private val repository: UserRepository) : BaseViewModel() {

    private val _navigationEvent: MutableSharedFlow<SignInNavigationAction> = MutableSharedFlow()
    val navigationEvent: SharedFlow<SignInNavigationAction> = _navigationEvent

    var loginIdEvent: MutableStateFlow<String> = MutableStateFlow<String>("")
    var passwordEvent: MutableStateFlow<String> = MutableStateFlow<String>("")

    // 로그인
    fun signInRetrofit() = viewModelScope.launch {
        if (loginIdEvent.value.isNotEmpty() && passwordEvent.value.isNotEmpty()) {
            val signIn = Login(loginIdEvent.value, passwordEvent.value)

            baseViewModelScope.launch {
                repository.retrofitSignIn(signIn)
                    .onSuccess {
                        baseViewModelScope.launch {
                            GlobalApplication.getInstance().getDataStore()
                                .setUserUid(loginIdEvent.value)
                        }
                        _navigationEvent.emit(SignInNavigationAction.NavigateToHome)
                    }
                    .onError { e ->
                        when (e) {
                            is SQLiteException -> _toastMessage.emit("데이터 베이스 에러가 발생하였습니다.")
                            else -> _toastMessage.emit("아이디 또는 비밀번호가 틀립니다")
                        }
                    }
            }
        } else {
            baseViewModelScope.launch {
                _toastMessage.emit("아이디와 비밀번호를 작성해주세요")
            }
        }
    }

    fun onSignupBtnClicked() {
        baseViewModelScope.launch {
            _navigationEvent.emit(SignInNavigationAction.NavigateToSignup)
        }
    }

    // factory pattern
    class Factory(val application: Application) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SigninViewModel(UserRepository.getInstance(application)!!) as T
        }
    }
}