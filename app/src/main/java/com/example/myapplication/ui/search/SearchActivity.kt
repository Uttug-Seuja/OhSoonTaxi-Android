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
        setSupportActionBar(binding.mainToolbar) // ????????? ??????????????? ????????? ??????
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // ???????????? ?????? ??? ?????? ?????????
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_previous) // ????????? ????????? ??????
        supportActionBar?.setDisplayShowTitleEnabled(false) // ????????? ????????? ????????????

        // ????????? ??????
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager


        lifecycleScope.launchWhenStarted {
            viewModel.toastMessage.collect { message ->
                Toast.makeText(this@SearchActivity, message, Toast.LENGTH_SHORT).show()
            }
        }

        // ????????? ?????????
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setMatchView() // ??????????????? ??? ??????
        setObserver() // ???????????? ???????????????.

        /**
         * coroutine flow debounce ?????? ????????? ??????????????? ??????
         * ????????? ????????? ???????????? api ??????
         */
        binding.etSearchField.apply {
            this.hint = "???????????? ??????????????????"

            // EditText ??? ???????????? ?????? ??? ClearButton ?????????
            this.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {

                    binding.textClearButton.visibility = View.VISIBLE
                } else {

                    binding.textClearButton.visibility = View.GONE


                }
            }


            lifecycleScope.launch(context = myCoroutineContext) {

                // editText ??? ??????????????????
                val editTextFlow = binding.etSearchField.textChangesToFlow()

                editTextFlow
                    // ????????????
                    // ???????????? ?????? 0.3??? ?????? ?????????
                    .debounce(300)
                    // ??????
//                    .filter {
//                        it?.length!! > 0
//                    }
                    .onEach {
//                        Log.d(ContentValues.TAG, "flow??? ????????? $it 11")

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

        // ????????? ??????
        binding.etSearchField.setOnKeyListener { _, keyCode, event ->

            if ((event.action == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                // ????????? ?????? ??? ?????? ?????? ???
                binding.etSearchField.clearFocus()
                imm.hideSoftInputFromWindow(binding.etSearchField.windowToken, 0)
                true

            } else {

                false

            }
        }

        // ClearButton ????????? ??? ?????? Clear
        binding.textClearButton.setOnClickListener {
            binding.etSearchField.text.clear()
//            imm.showSoftInput(binding.etSearchField,0)

        }
    }


    private fun setMatchView() {
        matchAdapter = MatchAdapter(this, applicationContext).apply {
            setHasStableIds(true) // ??????????????? ??? ???????????? ??? ????????? ??????
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
            android.R.id.home -> { // ?????? ??????
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    /**
     * ????????? ????????? ????????? ???????????? ???, ???????????? ????????? ??????
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
        // ????????? ?????? ??????
        val intent = Intent(this@SearchActivity, DetailActivity::class.java)
        intent.putExtra("reserveId", item.id)
        registerForActivityResult.launch(intent)
    }


    override fun onDestroy() {
        myCoroutineContext.cancel()  // MemoryLeak ????????? ?????? myCoroutineContext ??????
        super.onDestroy()
    }

}