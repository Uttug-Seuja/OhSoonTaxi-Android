package com.example.myapplication.ui.signup

import android.app.Application
import android.database.sqlite.SQLiteException
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.common.base.BaseViewModel
import com.example.myapplication.data.User
import com.example.myapplication.network.BaseResult
import com.example.myapplication.network.onError
import com.example.myapplication.network.onSuccess
import com.example.myapplication.repository.UserRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class SignupViewModel(private val repository: UserRepository) : BaseViewModel() {

    private val _navigationEvent: MutableSharedFlow<SignupNavigationAction> = MutableSharedFlow()
    val navigationEvent: SharedFlow<SignupNavigationAction> = _navigationEvent

    var userIdEvent: MutableStateFlow<String> = MutableStateFlow<String>("")
    var userPasswordEvent: MutableStateFlow<String> = MutableStateFlow<String>("")
    var userNameEvent: MutableStateFlow<String> = MutableStateFlow<String>("")
    var userStudentIdEvent: MutableStateFlow<String> = MutableStateFlow<String>("")
    var userGenderEvent: MutableStateFlow<String> = MutableStateFlow<String>("")
    var userPhoneNumberEvent: MutableStateFlow<String> = MutableStateFlow<String>("")
    var userIdDoubleCheckEvent: Boolean = false

    // 회원가입
    fun signUpRetrofit() = viewModelScope.launch {
        if (userIdEvent.value.isNotEmpty() && userPasswordEvent.value.isNotEmpty() && userNameEvent.value.isNotEmpty()
            && userStudentIdEvent.value.isNotEmpty() && userGenderEvent.value.isNotEmpty() && userPhoneNumberEvent.value.isNotEmpty()
            && userIdDoubleCheckEvent
        ) {

            val signup = User(
                uid = userIdEvent.value,
                password = userPasswordEvent.value,
                name = userNameEvent.value,
                phoneNum = userPhoneNumberEvent.value,
                schoolNum = userStudentIdEvent.value,
                sex = userGenderEvent.value,
            )

            baseViewModelScope.launch {
                repository.retrofitSignUp(signup)
                    .onSuccess {
                        _toastMessage.emit("회원가입을 성공적으로 했습니다")
                        _navigationEvent.emit(SignupNavigationAction.NavigateToSignIn)
                    }
                    .onError { e ->
                        Log.d("ttt", e.toString())
                        when (e) {
                            is SQLiteException -> _toastMessage.emit("데이터 베이스 에러가 발생하였습니다.")
                            else -> _toastMessage.emit("시스템 에러가 발생 하였습니다.")
                        }
                    }
            }


        } else {
            baseViewModelScope.launch {
                _toastMessage.emit("빈 곳 없이 작성해주세요")
            }
        }
    }

    // 아이디 중복확인
    fun postUsersCheckUniqueRetrofit() {
        if (userIdEvent.value.isNotEmpty()) {
            userIdDoubleCheckEvent = true
            baseViewModelScope.launch {
                Log.d("Ttt", userIdEvent.value)
                repository.retrofitPostUsersCheckUnique(userIdEvent.value)
                    .onSuccess {
                        _toastMessage.emit("아이디를 사용할 수 있습니다.")
                        userIdDoubleCheckEvent = true
                    }
                    .onError { e ->
                        Log.d("ttt", e.toString())
                        when (e) {
                            is SQLiteException -> _toastMessage.emit("데이터 베이스 에러가 발생하였습니다.")
                            else -> _toastMessage.emit("아이디를 사용할 수 없습니다.")
                        }
                    }
            }
        } else {
            userIdDoubleCheckEvent = false
            baseViewModelScope.launch {
                _toastMessage.emit("아이디를 입력해주세요")
            }
        }

    }

    // factory pattern
    class Factory(val application: Application) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SignupViewModel(UserRepository.getInstance(application)!!) as T
        }
    }

}