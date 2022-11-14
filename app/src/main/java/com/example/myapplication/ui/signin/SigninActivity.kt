package com.example.myapplication.ui.signin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.NavHostActivity
import com.example.myapplication.data.Login
import com.example.myapplication.databinding.ActivitySigninBinding
import com.example.myapplication.ui.signup.SignupActivity
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class SigninActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySigninBinding.inflate(layoutInflater) }
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            SigninViewModel.Factory(application)
        )[SigninViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        lifecycleScope.launchWhenStarted {
            viewModel.navigationEvent.collect {
                when(it){
                    is SignInNavigationAction.NavigateToHome ->{
                        startActivity(Intent(this@SigninActivity, NavHostActivity::class.java))
                        finish()
                    }
                    SignInNavigationAction.NavigateToSignup -> {
                        startActivity(Intent(this@SigninActivity, SignupActivity::class.java))
                    }
                }

            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.toastMessage.collect { message ->
                Toast.makeText(this@SigninActivity, message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}