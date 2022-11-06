package com.example.myapplication.ui.create

import android.app.Activity.RESULT_OK
import android.app.Application
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentCreateBinding
import com.example.myapplication.databinding.FragmentHomeBinding
import com.example.myapplication.ui.create.CreateGenderActivity.Companion.INTENT_EXTRA_MSG
import com.example.myapplication.ui.home.HomeFragment
import com.example.myapplication.ui.home.HomeViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*



class CreateFragment  : Fragment() {

    private var _binding: FragmentCreateBinding? = null
    private val binding get() = _binding!!
    private val viewModel by lazy { ViewModelProvider(this, CreateViewModel.Factory(application = Application()))[CreateViewModel::class.java] }

    private var title : String? = null
    private var sports : String? = null
    private var place : String? = null
    private var recruit : String? = null
    private var gender : String? = null
    private var matchingDate : String? = null
    private var matchingStartTime : String? = null
    private var matchingEndTime : String? = null
    private var content : String? = null
    private var member : Int? = null
    private val genderMap = hashMapOf<String, String>("남녀 모두" to "ALL", "남자만" to "MAN", "여자만" to "WOMAN" )

    var historyTitle: String? = null
    var historyMood: String? = null
    var historyComment: String? = null
    var historyPlaceTitle: String? = null
    var historyUserID: Int? = null
    var historyCreatedAt: String? = null  // historyDate + historyTime
    var historyDate: String? = null
    var historyTime: String? = null

    // Date, Time Picker 기본 값으로 사용될 현재 날짜 및 시각 가져옴
    private val calendarInstance = Calendar.getInstance()
    private val year = calendarInstance.get(Calendar.YEAR)
    private val month = calendarInstance.get(Calendar.MONTH)
    private val day = calendarInstance.get(Calendar.DAY_OF_MONTH)
    private val hour = calendarInstance.get(Calendar.HOUR_OF_DAY)
    private val minute = calendarInstance.get(Calendar.MINUTE)


    fun newInstance(): CreateFragment {
        return CreateFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("ttt", "onCreate 실행")

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        _binding = FragmentCreateBinding.inflate(inflater, container, false)
        binding.viewmodel = viewModel


        return binding.root


    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListener()
    }

    private fun setOnClickListener(){

        binding.startBtn.setOnClickListener {
            viewModel.onCreatePromiseClicked()
        }

        binding.genderSelectBtn.setOnClickListener {
            CreateGenderActivity.createIntent(requireActivity(), "A Child Callback OK!!").also {
                genderForResult.launch(it)
            }

        }

        binding.seatSelectBtn.setOnClickListener {
            CreateSeatActivity.createIntent(requireActivity(), "A Child Callback OK!!").also {
                seatForResult.launch(it)
            }
        }


        // 히스토리 날짜 입력
        binding.dateSelectBtn.setOnClickListener {
            val datePicker = DatePickerDialog(
                requireActivity(),
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                    historyDate =
                        year.toString() + "-"
                    //DatePicker 특성 상 한 자리 날짜 입력에 대한 대응을 해줘야 함
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

        // 히스토리 시작 시간 입력
        binding.reservationTimeBtn.setOnClickListener {
            val timePicker = TimePickerDialog(
                requireActivity(), R.style.TimePicker,
                TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                    //TimePicker 특성 상 한자리 시간 입력에 대한 대응을 해줘야 함
                    historyTime =
                        if (hourOfDay < 10) "0${hourOfDay}시"
                        else hourOfDay.toString() + "시"
                    historyTime +=
                        if (minute < 10) " 0${minute}분"
                        else " ${minute}분"
                    binding.reservationTimeText.text =historyTime.toString()

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

    private val seatForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                RESULT_OK -> {
                    binding.seatText.text = result.data!!.getStringExtra("seatType")
                }
            }
        }

}