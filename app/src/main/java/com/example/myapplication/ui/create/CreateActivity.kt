package com.example.myapplication.ui.create

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.BuildConfig
import com.example.myapplication.R
import com.example.myapplication.adapter.KakaoLocalAdapter
import com.example.myapplication.data.ModelKakaoLocal
import com.example.myapplication.data.ResultSearchKeyword
import com.example.myapplication.databinding.ActivityCreateBinding
import com.example.myapplication.network.KakaoLocalInterface
import com.example.myapplication.ui.common.utils.textChangesToFlow
import com.example.myapplication.ui.home.HomeNavigationAction
import com.example.myapplication.ui.search.SearchActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.coroutines.CoroutineContext

class CreateActivity : AppCompatActivity() {

    private val binding by lazy { ActivityCreateBinding.inflate(layoutInflater) }

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            CreateViewModel.Factory(application)
        )[CreateViewModel::class.java]
    }

    var historyDate: String? = null
    var historyTime: String? = null

    var startPlaceX: Double? = null
    var startPlaceY: Double? = null
    var destinationX: Double? = null
    var destinationY: Double? = null

    // Date, Time Picker ?????? ????????? ????????? ?????? ?????? ??? ?????? ?????????
    private val calendarInstance = Calendar.getInstance()
    private val year = calendarInstance.get(Calendar.YEAR)
    private val month = calendarInstance.get(Calendar.MONTH)
    private val day = calendarInstance.get(Calendar.DAY_OF_MONTH)
    private val hour = calendarInstance.get(Calendar.HOUR_OF_DAY)
    private val minute = calendarInstance.get(Calendar.MINUTE)


    companion object {
        const val BASE_URL = "https://dapi.kakao.com/"
        const val API_KEY = BuildConfig.KAKAO_MAP_REST_API_KEY  // REST API ???

    }

    private val listItems = arrayListOf<ModelKakaoLocal>()   // ??????????????? ??? ?????????
    private val listAdapter = KakaoLocalAdapter(listItems)    // ??????????????? ??? ?????????
    private var pageNumber = 1      // ?????? ????????? ??????
    private var keyword = ""        // ?????? ?????????
    private var flag = false

    private var startPlaceCoroutineJob: Job = Job()
    private val startPlaceCoroutineContext: CoroutineContext
        get() = Dispatchers.IO + startPlaceCoroutineJob

    private var destinationCoroutineJob: Job = Job()
    private val destinationCoroutineContext: CoroutineContext
        get() = Dispatchers.IO + destinationCoroutineJob

    // ????????? ?????? ?????????
    private lateinit var startPlaceSearchViewEditText: EditText
    private lateinit var destinationSearchViewEditText: EditText


    private var bottomSheetDialog: BottomSheetDialog? = null
    private val seatAnswerList = mutableListOf<Boolean>(false, false, false, false)
    private var seatAnswer = -1

    private val seatSelectButton = mutableListOf(
        R.id.seat1_select_btn,
        R.id.seat2_select_btn,
        R.id.seat3_select_btn,
        R.id.seat4_select_btn
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.viewmodel = viewModel

        setSupportActionBar(binding.mainToolbar) // ????????? ??????????????? ????????? ??????
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // ???????????? ?????? ??? ?????? ?????????
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_previous) // ????????? ????????? ??????
        supportActionBar?.setDisplayShowTitleEnabled(false) // ????????? ????????? ????????????


        setOnClickListener()

        lifecycleScope.launchWhenStarted {
            viewModel.toastMessage.collect { message ->
                Toast.makeText(this@CreateActivity, message, Toast.LENGTH_SHORT).show()
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.navigationEvent.collect {
                when (it) {
                    is CreateNavigationAction.NavigateToHome -> {
                        Toast.makeText(this@CreateActivity, "????????? ????????????", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
        }


        // ????????? ??????
        val imm =
            this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        // ??????????????? ???
        binding.rvStartPlaceList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvStartPlaceList.adapter = listAdapter

        binding.rvDestinationList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvDestinationList.adapter = listAdapter

        // ????????? ????????? ?????? ??? ?????? ????????? ??????
        listAdapter.setItemClickListener(object : KakaoLocalAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int, itemList: ArrayList<ModelKakaoLocal>) {

                if (binding.editStartPlace.isFocused) {
                    binding.rvStartPlaceList.visibility = View.GONE
                    binding.editStartPlace.clearFocus()
                    binding.editStartPlace.setText(itemList[position].name)
                    startPlaceX = itemList[position].x
                    startPlaceY = itemList[position].y
                    viewModel.startLatitudeEvent.value = startPlaceY!!.toDouble()
                    viewModel.startLongitudeEvent.value =startPlaceX!!.toDouble()

                }

                if (binding.editDestination.isFocused) {
                    binding.rvDestinationList.visibility = View.GONE
                    binding.editDestination.clearFocus()
                    binding.editDestination.setText(itemList[position].name)
                    destinationX = itemList[position].x
                    destinationY = itemList[position].y
                    viewModel.finishLatitudeEvent.value = destinationY!!.toDouble()
                    viewModel.finishLongitudeEvent.value =destinationX!!.toDouble()
                }
                imm.hideSoftInputFromWindow(this@CreateActivity.currentFocus?.windowToken, 0)
                listItems.clear()


            }
        })

        binding.editStartPlace.apply {
            this.hint = "????????? ??????????"

            // EditText ??? ???????????? ?????? ??? ClearButton ?????????
            this.setOnFocusChangeListener { v, hasFocus ->
                flag = !hasFocus
            }

            // ??????????????? ?????????????????? ????????????.
            startPlaceSearchViewEditText = binding.editStartPlace

//            binding.rvList.visibility = View.VISIBLE
            GlobalScope.launch(context = startPlaceCoroutineContext) {

                // editText ??? ??????????????????
                val editTextFlow = startPlaceSearchViewEditText.textChangesToFlow()

                editTextFlow
                    // ????????????
                    // ???????????? ?????? 0.2??? ?????? ?????????
                    .debounce(200)
                    .filter {
                        it?.length!! >= 0 && !flag
                    }
                    .onEach {
                        // ?????? ???????????? api ??????
                        keyword = it.toString()
                        pageNumber = 1
                        searchKeyword(keyword, pageNumber, 0)

//                        searchPhotoApiCall(it.toString())
                    }
                    .launchIn(this)
            }

        }


        binding.editDestination.apply {
            this.hint = "????????? ??????????"
            // EditText ??? ???????????? ?????? ??? ClearButton ?????????
            this.setOnFocusChangeListener { v, hasFocus ->
                flag = !hasFocus
            }

            // ??????????????? ?????????????????? ????????????.
            destinationSearchViewEditText = binding.editDestination

//            binding.rvList.visibility = View.VISIBLE
            GlobalScope.launch(context = destinationCoroutineContext) {

                // editText ??? ??????????????????
                val editTextFlow = destinationSearchViewEditText.textChangesToFlow()

                editTextFlow
                    // ????????????
                    // ???????????? ?????? 0.2??? ?????? ?????????
                    .debounce(200)
                    .filter {
                        it?.length!! >= 0 && !flag

                    }
                    .onEach {
                        Log.d(ContentValues.TAG, "flow??? ????????? $it")

                        // ?????? ???????????? api ??????
                        keyword = it.toString()
                        pageNumber = 1
                        searchKeyword(keyword, pageNumber, 1)

//                        searchPhotoApiCall(it.toString())
                    }
                    .launchIn(this)
            }

        }


        val bottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet_seat_layout, null)
        bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog!!.setContentView(bottomSheetView)

        binding.seatSelectBtn.setOnClickListener {


            for (i in 0..3) {
                if (i == seatAnswer) {
                    bottomSheetView.findViewById<View>(seatSelectButton[seatAnswer])
                        .setBackgroundResource(R.drawable.seat_selected)
                    continue

                }
                bottomSheetView.findViewById<View>(seatSelectButton[i])
                    .setBackgroundResource(R.drawable.seat_un_selected)
            }

            bottomSheetDialog!!.show()

        }


        bottomSheetView.findViewById<View>(R.id.seat1_select_btn).setOnClickListener {

            if (seatAnswerList[0]) {
                seatAnswerList[0] = false
                bottomSheetView.findViewById<View>(R.id.seat1_select_btn)
                    .setBackgroundResource(R.drawable.seat_un_selected)
            } else {
                for (i in 0..3) {
                    bottomSheetView.findViewById<View>(seatSelectButton[i])
                        .setBackgroundResource(R.drawable.seat_un_selected)
                    seatAnswerList[i] = false


                }
                seatAnswerList[0] = true
                bottomSheetView.findViewById<View>(seatSelectButton[0])
                    .setBackgroundResource(R.drawable.seat_selected)

            }

        }
        bottomSheetView.findViewById<View>(R.id.seat2_select_btn).setOnClickListener {
            if (seatAnswerList[1]) {
                seatAnswerList[1] = false
                bottomSheetView.findViewById<View>(R.id.seat2_select_btn)
                    .setBackgroundResource(R.drawable.seat_un_selected)
            } else {
                for (i in 0..3) {
                    bottomSheetView.findViewById<View>(seatSelectButton[i])
                        .setBackgroundResource(R.drawable.seat_un_selected)
                    seatAnswerList[i] = false


                }
                seatAnswerList[1] = true
                bottomSheetView.findViewById<View>(seatSelectButton[1])
                    .setBackgroundResource(R.drawable.seat_selected)

            }
        }
        bottomSheetView.findViewById<View>(R.id.seat3_select_btn).setOnClickListener {
            if (seatAnswerList[2]) {
                seatAnswerList[2] = false
                bottomSheetView.findViewById<View>(R.id.seat3_select_btn)
                    .setBackgroundResource(R.drawable.seat_un_selected)

            } else {
                for (i in 0..3) {
                    bottomSheetView.findViewById<View>(seatSelectButton[i])
                        .setBackgroundResource(R.drawable.seat_un_selected)
                    seatAnswerList[i] = false

                }
                seatAnswerList[2] = true
                bottomSheetView.findViewById<View>(seatSelectButton[2])
                    .setBackgroundResource(R.drawable.seat_selected)

            }
        }
        bottomSheetView.findViewById<View>(R.id.seat4_select_btn).setOnClickListener {
            if (seatAnswerList[3]) {
                seatAnswerList[3] = false
                bottomSheetView.findViewById<View>(R.id.seat4_select_btn)
                    .setBackgroundResource(R.drawable.seat_un_selected)
            } else {
                for (i in 0..3) {
                    bottomSheetView.findViewById<View>(seatSelectButton[i])
                        .setBackgroundResource(R.drawable.seat_un_selected)
                    seatAnswerList[i] = false
                }
                seatAnswerList[3] = true
                bottomSheetView.findViewById<View>(seatSelectButton[3])
                    .setBackgroundResource(R.drawable.seat_selected)

            }
        }

        bottomSheetView.findViewById<View>(R.id.seat_answer_btn).setOnClickListener {
            for (i in 0..3) {
                if (seatAnswerList[i]) {
                    binding.seatText.text = "${i + 1}???  ??????"
                    seatAnswer = i
                    bottomSheetDialog!!.dismiss()
                    break
                }

            }
        }


    }

    private fun setOnClickListener() {

        binding.startBtn.setOnClickListener {
            viewModel.onCreatePromiseClicked()
        }

        binding.genderSelectBtn.setOnClickListener {
            CreateGenderActivity.createIntent(this, "A Child Callback OK!!").also {
                genderForResult.launch(it)
            }

        }


        // ???????????? ?????? ??????
        binding.dateSelectBtn.setOnClickListener {
            val datePicker = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                    historyDate =
                        year.toString() + "-"
                    //DatePicker ?????? ??? ??? ?????? ?????? ????????? ?????? ????????? ????????? ???
                    historyDate +=
                        if ((monthOfYear + 1) < 10) "0" + (monthOfYear + 1).toString() + "-"
                        else (monthOfYear + 1).toString() + "-"
                    historyDate +=
                        if (dayOfMonth < 10) "0$dayOfMonth"
                        else "$dayOfMonth"

                    binding.dateText.text = historyDate


                    historyDate += "T"
                }, year, month, day
            )
            datePicker.show()
        }

        // ???????????? ?????? ?????? ??????
        binding.reservationTimeBtn.setOnClickListener {
            val timePicker = TimePickerDialog(
                this, R.style.TimePicker,
                TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                    //TimePicker ?????? ??? ????????? ?????? ????????? ?????? ????????? ????????? ???
                    historyTime =
                        if (hourOfDay < 10) "0${hourOfDay}???"
                        else hourOfDay.toString() + "???"
                    historyTime +=
                        if (minute < 10) " 0${minute}???"
                        else " ${minute}???"
                    binding.reservationTimeText.text = historyTime.toString()

                }, hour, minute, false
            )

            timePicker.show()

            val view: ViewGroup.MarginLayoutParams =
                timePicker.getButton(Dialog.BUTTON_POSITIVE).layoutParams as ViewGroup.MarginLayoutParams
            view.leftMargin = 16
            timePicker.getButton(Dialog.BUTTON_NEGATIVE)
                .setBackgroundColor(Color.parseColor("#00000000"))
            timePicker.getButton(Dialog.BUTTON_NEGATIVE)
                .setTextColor(Color.parseColor("#293263"))

            timePicker.getButton(Dialog.BUTTON_POSITIVE)
                .setBackgroundColor(Color.parseColor("#00000000"))
            timePicker.getButton(Dialog.BUTTON_POSITIVE)
                .setTextColor(Color.parseColor("#293263"))
        }

    }

    private val genderForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                RESULT_OK -> {
                    binding.genderText.text = result.data!!.getStringExtra("genderType")
                }
            }
        }

    // ????????? ?????? ??????
    private fun searchKeyword(keyword: String, page: Int, type: Int) {
        val retrofit = Retrofit.Builder()          // Retrofit ??????
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(KakaoLocalInterface::class.java)            // ?????? ?????????????????? ????????? ??????
        val call = api.getSearchKeyword(API_KEY, keyword, page)    // ?????? ?????? ??????

        // API ????????? ??????
        call.enqueue(object : Callback<ResultSearchKeyword> {
            override fun onResponse(
                call: Call<ResultSearchKeyword>,
                response: Response<ResultSearchKeyword>
            ) {

                // ?????? ??????
                addItemsAndMarkers(response.body(), type)
                Log.d("Ttt", "??????")
            }

            override fun onFailure(call: Call<ResultSearchKeyword>, t: Throwable) {
                // ?????? ??????
                Log.w("LocalSearch", "?????? ??????: ${t.message}")
            }
        })
    }

    // ?????? ?????? ?????? ??????
    @SuppressLint("NotifyDataSetChanged")
    fun addItemsAndMarkers(searchResult: ResultSearchKeyword?, type: Int) {
        if (!searchResult?.documents.isNullOrEmpty()) {

            if (type == 0) binding.rvStartPlaceList.visibility = View.VISIBLE
            else binding.rvDestinationList.visibility = View.VISIBLE


            // ?????? ?????? ??????
            listItems.clear()                   // ????????? ?????????

            for (document in searchResult!!.documents) {

                // ????????? ??????????????? ?????? ??????
                val item = ModelKakaoLocal(
                    document.place_name,
                    document.road_address_name,
                    document.address_name,
                    document.x.toDouble(),
                    document.y.toDouble()
                )
                listItems.add(item)
            }
            listAdapter.notifyDataSetChanged()


        } else {
            // ?????? ?????? ??????
            binding.rvStartPlaceList.visibility = View.GONE
            binding.rvDestinationList.visibility = View.GONE

        }
    }

    //???????????? ?????? ?????? ???
    override fun onOptionsItemSelected(item: MenuItem): Boolean {


        when (item.itemId) {
            android.R.id.home -> { // ?????? ??????
                finish()
            }


        }
        return super.onOptionsItemSelected(item)
    }


    override fun onDestroy() {
        listItems.clear()                   // ????????? ?????????
        startPlaceCoroutineContext.cancel()  // MemoryLeak ????????? ?????? myCoroutineContext ??????
        destinationCoroutineContext.cancel()  // MemoryLeak ????????? ?????? myCoroutineContext ??????

        super.onDestroy()
    }

}