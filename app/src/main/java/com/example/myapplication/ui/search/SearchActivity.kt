package com.example.myapplication.ui.search

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.R
import com.example.myapplication.adapter.MatchAdapter
import com.example.myapplication.adapter.MyReservesAdapter
import com.example.myapplication.databinding.ActivitySearchBinding
import com.example.myapplication.ui.common.utils.textChangesToFlow
import com.example.myapplication.data.ReservesListResponseData
import com.example.myapplication.ui.detail.DetailActivity
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class SearchActivity : AppCompatActivity(), MatchAdapter.ItemClickListener {
    private val binding by lazy { ActivitySearchBinding.inflate(layoutInflater) }
    private var myCoroutineJob: Job = Job()
    private val myCoroutineContext: CoroutineContext
        get() = Dispatchers.IO + myCoroutineJob
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            SearchViewModel.Factory(application)
        )[SearchViewModel::class.java]
    }
//    private lateinit var retrofitAdapter: SearchAdapter

    private lateinit var matchAdapter: MatchAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        /**
         * drawer
         *
         * */
        setSupportActionBar(binding.mainToolbar) // 툴바를 액티비티의 앱바로 지정
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // 드로어를 꺼낼 홈 버튼 활성화
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_previous) // 홈버튼 이미지 변경
        supportActionBar?.setDisplayShowTitleEnabled(false) // 툴바에 타이틀 안보이게

        // 키보드 설정
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager


        lifecycleScope.launchWhenStarted {
            viewModel.toastMessage.collect { message ->
                Toast.makeText(this@SearchActivity, message, Toast.LENGTH_SHORT).show()
            }
        }

        // 데이터 바인딩
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setMatchView() // 리사이클러 뷰 연결
        setObserver() // 뷰모델을 관찰합니다.

        /**
         * coroutine flow debounce 통해 반응형 프로그래밍 구현
         * 검색어 입력시 바로바로 api 호출
         */
        binding.etSearchField.apply {
            this.hint = "검색어를 입력해주세요"

            // EditText 에 포커스가 갔을 때 ClearButton 활성화
            this.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {

                    binding.textClearButton.visibility = View.VISIBLE
                } else {

                    binding.textClearButton.visibility = View.GONE


                }
            }


            lifecycleScope.launch(context = myCoroutineContext) {

                // editText 가 변경되었을때
                val editTextFlow = binding.etSearchField.textChangesToFlow()

                editTextFlow
                    // 연산자들
                    // 입려되고 나서 0.3초 뒤에 받는다
                    .debounce(300)
                    // 필터
//                    .filter {
//                        it?.length!! > 0
//                    }
                    .onEach {
//                        Log.d(ContentValues.TAG, "flow로 받는다 $it 11")

                        lifecycleScope.launch {


                            if (!it.isNullOrBlank()) {
                                viewModel.getReservesSearchListRetrofit(it.toString())

                            } else {
                                binding.rvList.visibility = View.GONE
                                binding.noResultCard.visibility = View.VISIBLE
                                binding.textClearButton.visibility = View.GONE


                            }
                        }
                    }
                    .launchIn(this)


            }

        }

        // 검색창 엔터
        binding.etSearchField.setOnKeyListener { _, keyCode, event ->

            if ((event.action == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                // 엔터가 눌릴 때 하고 싶은 일
                binding.etSearchField.clearFocus()
                imm.hideSoftInputFromWindow(binding.etSearchField.windowToken, 0)
                true

            } else {

                false

            }
        }

        // ClearButton 눌렀을 때 쿼리 Clear
        binding.textClearButton.setOnClickListener {
            binding.etSearchField.text.clear()
//            imm.showSoftInput(binding.etSearchField,0)

        }
    }


    private fun setMatchView() {
        matchAdapter = MatchAdapter(this, applicationContext).apply {
            setHasStableIds(true) // 리사이클러 뷰 업데이트 시 깜빡임 방지
        }
        binding.rvList.adapter = matchAdapter
    }

    private fun setObserver() {

        lifecycleScope.launchWhenStarted {
            viewModel.retrofitReservesListResponseEvent.collect {
                if (it.reservesListResponseData.isEmpty()) {
                    binding.rvList.visibility = View.GONE
                    binding.noResultCard.visibility = View.VISIBLE
                } else {
                    matchAdapter.setData(it.reservesListResponseData)
                    binding.rvList.visibility = View.VISIBLE
                    binding.noResultCard.visibility = View.GONE

                }

            }
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


    /**
     * 키보드 이외의 영역을 터치했을 때, 키보드를 숨기는 동작
     */
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val view = currentFocus
        if (view != null && (ev!!.action === KeyEvent.ACTION_UP || MotionEvent.ACTION_MOVE === ev!!.action) &&
            view is EditText && !view.javaClass.name.startsWith("android.webkit.")
        ) {
//            binding.noResultCard.visibility = View.GONE

            val scrcoords = IntArray(2)
            view.getLocationOnScreen(scrcoords)
            val x = ev!!.rawX + view.getLeft() - scrcoords[0]
            val y = ev!!.rawY + view.getTop() - scrcoords[1]
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom()) (this.getSystemService(
                INPUT_METHOD_SERVICE
            ) as InputMethodManager).hideSoftInputFromWindow(
                this.window.decorView.applicationWindowToken, 0
            )
        }

        return super.dispatchTouchEvent(ev)
    }


    private val registerForActivityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val id = result.data?.getStringExtra("id") ?: ""
                val password = result.data?.getStringExtra("password") ?: ""

                Log.d("ttt", id)
                Log.d("ttt", password)

            }
        }


    override fun onItemClickListener(item: ReservesListResponseData, position: Int) {
        // 원하는 화면 연결
        val intent = Intent(this@SearchActivity, DetailActivity::class.java)
        intent.putExtra("reserveId", item.id)
        registerForActivityResult.launch(intent)
    }


    override fun onDestroy() {
        myCoroutineContext.cancel()  // MemoryLeak 방지를 위해 myCoroutineContext 해제
        super.onDestroy()
    }

}