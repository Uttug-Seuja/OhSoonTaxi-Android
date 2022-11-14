package com.example.myapplication.ui.signin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.NaviHostActivity
import com.example.myapplication.data.Login
import com.example.myapplication.databinding.ActivitySigninBinding

class SigninActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySigninBinding.inflate(layoutInflater) }
    private val viewModel by lazy { ViewModelProvider(this,
        SigninViewModel.Factory(application)
    )[SigninViewModel::class.java] }

    private var userId : String? = null
    private var userPassword : String? = null
    private var memberId : String? = null
    private var userGender : Boolean? = null
    private var flag : Boolean = false
    private var googleEmail: String? = null


    private var userNameFlag : Boolean = false
    private var userBirthDayFlag : Boolean = false
    private var userGenderFlag : Boolean = false
    private var resultCheckFlag : Boolean = false

    private var totalFlag = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this





        // 사용자 아이디
        binding.idEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {

                userId = binding.idEditText.text.toString()


            }
        })

        // 사용자 비밀번호
        binding.passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {

                userPassword = binding.passwordEditText.text.toString()


            }
        })

        // 다음 버튼
        binding.loginBtn.setOnClickListener {
            startActivity( Intent(this@SigninActivity, NaviHostActivity::class.java))
            finish()

            userId = binding.idEditText.text.toString()
            userPassword = binding.passwordEditText.text.toString()
            Log.d("ttt",userNameFlag.toString())
            Log.d("ttt name",userId.toString())

//            registerLogic()
//            keyBordHide()
            binding.idEditText.clearFocus()
            binding.passwordEditText.clearFocus()

            viewModel.signInRetrofit(Login(userId.toString(), userPassword.toString()))

            // 로그인 로직
            viewModel.retrofitSignInText.value.let {
                viewModel.retrofitSignInText.observe(this){
//                    if (it.dataInt != -1){
//                        MyApplication.prefs.setString("memberId", it.dataInt.toString())
                        startActivity( Intent(this@SigninActivity, NaviHostActivity::class.java))
                        finish()
//                    }
                }
            }


        }
    }
}