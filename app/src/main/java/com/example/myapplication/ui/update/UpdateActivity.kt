package com.example.myapplication.ui.update

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.example.myapplication.R
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.NavHostActivity
import com.example.myapplication.databinding.ActivityUpdateBinding
import com.example.myapplication.ui.create.CreateNavigationAction
import com.example.myapplication.ui.detail.DetailActivity

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
        lifecycleScope.launchWhenStarted {
            viewModel.toastMessage.collect { message ->
                Toast.makeText(this@UpdateActivity, message, Toast.LENGTH_SHORT).show()
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.navigationEvent.collect {
                when (it) {
                    is UpdateNavigationAction.NavigateToDetail -> {
                        val intent = Intent(this@UpdateActivity, DetailActivity::class.java)
                        intent.putExtra("id", "2022-12-06")
                        intent.putExtra("password", "이건희")
                        setResult(RESULT_OK, intent)
                        finish()
                    }
                }
            }
        }

    }

    private fun setOnClickListener() {

        binding.startBtn.setOnClickListener {
            viewModel.onCreatePromiseClicked()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> { // 메뉴 버튼
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}