package com.example.myapplication.ui.update

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.R
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.databinding.ActivityUpdateBinding

class UpdateActivity : AppCompatActivity() {

    private val binding by lazy { ActivityUpdateBinding.inflate(layoutInflater) }

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            UpdateViewModel.Factory(application)
        )[UpdateViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.viewmodel = viewModel

        setSupportActionBar(binding.mainToolbar) // 툴바를 액티비티의 앱바로 지정
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // 드로어를 꺼낼 홈 버튼 활성화
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_previous) // 홈버튼 이미지 변경
        supportActionBar?.setDisplayShowTitleEnabled(false) // 툴바에 타이틀 안보이게


        setOnClickListener()

    }

    private fun setOnClickListener() {

        binding.startBtn.setOnClickListener {
            viewModel.onCreatePromiseClicked()
        }


    }

}