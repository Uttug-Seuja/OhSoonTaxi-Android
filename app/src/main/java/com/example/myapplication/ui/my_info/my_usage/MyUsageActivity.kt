package com.example.myapplication.ui.my_info.my_usage

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.R
import com.example.myapplication.adapter.MatchAdapter
import com.example.myapplication.adapter.MyReservesAdapter
import com.example.myapplication.common.GlobalApplication
import com.example.myapplication.data.ReservesListResponseData
import com.example.myapplication.databinding.ActivityMyUsageBinding
import com.example.myapplication.ui.detail.DetailActivity
import kotlinx.coroutines.launch

class MyUsageActivity : AppCompatActivity(), MyReservesAdapter.ItemClickListener {
    private val binding by lazy { ActivityMyUsageBinding.inflate(layoutInflater) }
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            MyUsageViewModel.Factory(application)
        )[MyUsageViewModel::class.java]
    }


    private lateinit var myReservesAdapter: MyReservesAdapter
    private var userUid: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.viewmodel = viewModel

        lifecycleScope.launch {
            GlobalApplication.getInstance().getDataStore().userUid.collect { it ->
                userUid = it

            }
        }


        setSupportActionBar(binding.mainToolbar) // 툴바를 액티비티의 앱바로 지정
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // 드로어를 꺼낼 홈 버튼 활성화
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_previous) // 홈버튼 이미지 변경
        supportActionBar?.setDisplayShowTitleEnabled(false) // 툴바에 타이틀 안보이게
        setMatchView()
        setObserver()
        lifecycleScope.launchWhenStarted {
            viewModel.toastMessage.collect { message ->
                Toast.makeText(this@MyUsageActivity, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setMatchView() {
        myReservesAdapter = MyReservesAdapter(this, applicationContext).apply {
            setHasStableIds(true) // 리사이클러 뷰 업데이트 시 깜빡임 방지
        }
        binding.rvList.adapter = myReservesAdapter
    }

    private fun setObserver() {

        viewModel.reservesListParticipationsRetrofit(userUid!!)

        lifecycleScope.launchWhenStarted {
            viewModel.retrofitReservesListResponseEvent.collect {
                if (it.reservesListResponseData.isEmpty()) {
                    binding.rvList.visibility = View.GONE
                    binding.noResultCard.visibility = View.VISIBLE
                } else {
                    myReservesAdapter.setData(it.reservesListResponseData)
                    binding.rvList.visibility = View.VISIBLE
                    binding.noResultCard.visibility = View.GONE

                }

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

    private val registerForActivityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val id = result.data?.getStringExtra("id") ?: ""
                val password = result.data?.getStringExtra("password") ?: ""
                viewModel.reservesListParticipationsRetrofit(userUid!!)

                Log.d("ttt", id)
                Log.d("ttt", password)

            }
        }

    override fun onItemClickListener(item: ReservesListResponseData, position: Int) {
        // 원하는 화면 연결
        val intent = Intent(this@MyUsageActivity, DetailActivity::class.java)
        intent.putExtra("reserveId", item.id)
        registerForActivityResult.launch(intent)
    }
}