package com.example.myapplication.ui.my_info.my_usage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityMyCreateBinding
import com.example.myapplication.databinding.ActivityMyUsageBinding
import com.example.myapplication.ui.my_info.my_create.MyCreateViewModel

class MyUsageActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMyUsageBinding.inflate(layoutInflater) }
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            MyUsageViewModel.Factory(application)
        )[MyUsageViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.viewmodel = viewModel

        setSupportActionBar(binding.mainToolbar) // 툴바를 액티비티의 앱바로 지정
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // 드로어를 꺼낼 홈 버튼 활성화
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_previous) // 홈버튼 이미지 변경
        supportActionBar?.setDisplayShowTitleEnabled(false) // 툴바에 타이틀 안보이게

        lifecycleScope.launchWhenStarted {
            viewModel.toastMessage.collect { message ->
                Toast.makeText(this@MyUsageActivity, message, Toast.LENGTH_SHORT).show()
            }
        }


    }

    //액션버튼 클릭 했을 때
    override fun onOptionsItemSelected(item: MenuItem): Boolean {


        when (item.itemId) {
            android.R.id.home -> { // 메뉴 버튼
                finish()
            }

        }
        return super.onOptionsItemSelected(item)
    }
}