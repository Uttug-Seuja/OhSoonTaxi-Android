package com.example.myapplication.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.myapplication.databinding.ActivitySplashBinding
import com.example.myapplication.ui.signin.SigninActivity

class SplashActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySplashBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.lifecycleOwner = this



        Handler(Looper.getMainLooper()).postDelayed({

//            val memberId = MyApplication.prefs.getString("memberId", "")
            /**
             * 이미 로그인이 되어 있을 수도 있기 때문에 !!!!!!!!!!
             * 로그인을 해본다!!!!!!!!!
             * */
//            if (memberId != ""){
//                val intent = Intent(this, MainActivity::class.java)
//                startActivity(intent)
//                finish()
//
//            }else{

                val intent = Intent(this, SigninActivity::class.java)
                startActivity(intent)
                finish()

//            }

        }, 500)
    }

}