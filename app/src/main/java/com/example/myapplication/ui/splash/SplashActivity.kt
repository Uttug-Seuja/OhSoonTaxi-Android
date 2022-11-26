package com.example.myapplication.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.ui.NavHostActivity
import com.example.myapplication.ui.common.GlobalApplication
import com.example.myapplication.databinding.ActivitySplashBinding
import com.example.myapplication.ui.signin.SigninActivity
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySplashBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.lifecycleOwner = this



        Handler(Looper.getMainLooper()).postDelayed({

            lifecycleScope.launch {
                GlobalApplication.getInstance().getDataStore().userUid.collect { it ->
                    if (it != "") {
                        val intent = Intent(this@SplashActivity, NavHostActivity::class.java)
                        startActivity(intent)
                        finish()

                    } else {

                        val intent = Intent(this@SplashActivity, SigninActivity::class.java)
                        startActivity(intent)
                        finish()

                    }
                }


            }

            /**
             * 이미 로그인이 되어 있을 수도 있기 때문에 !!!!!!!!!!
             * 로그인을 해본다!!!!!!!!!
             * */


        }, 500)
    }

}