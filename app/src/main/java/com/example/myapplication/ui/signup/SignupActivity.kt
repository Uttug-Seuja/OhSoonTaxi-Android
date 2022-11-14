package com.example.myapplication.ui.signup

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.databinding.ActivitySignupBinding
import com.example.myapplication.ui.signin.SigninActivity

class SignupActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySignupBinding.inflate(layoutInflater) }
    private val viewModel by lazy { ViewModelProvider(this, SignupViewModel.Factory(application))[SignupViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        // 사용자 남자 성별 클릭
        binding.manBtn.setOnClickListener{


            viewModel.userGenderEvent.value = "MAN"
            binding.manBtn.strokeColor = Color.parseColor("#007680")
            binding.womanBtn.strokeColor = Color.parseColor("#d9d9d9")
        }

        // 사용자 여자 성별 클릭
        binding.womanBtn.setOnClickListener {

            viewModel.userGenderEvent.value = "WOMAN"
            binding.womanBtn.strokeColor = Color.parseColor("#007680")
            binding.manBtn.strokeColor = Color.parseColor("#d9d9d9")


        }
        lifecycleScope.launchWhenStarted {
            viewModel.navigationEvent.collect {
                when(it){
                    is SignupNavigationAction.NavigateToSignIn ->{
                        startActivity(Intent(this@SignupActivity, SigninActivity::class.java))
                        finish()
                    }
                }

            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.toastMessage.collect { message ->
                Toast.makeText(this@SignupActivity, message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}