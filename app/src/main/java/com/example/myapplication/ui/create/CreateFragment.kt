package com.example.myapplication.ui.create

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
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentCreateBinding
import com.example.myapplication.databinding.FragmentHomeBinding
import com.example.myapplication.ui.home.HomeFragment
import com.example.myapplication.ui.home.HomeViewModel
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
    private val sportsMap = hashMapOf<String, String>("축구" to "SOCCER", "풋살" to "FUTSAL", "런닝" to "RUNNING", "농구" to "BASKETBALL" )

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
//        viewModel = ViewModelProvider(this).get(CreateViewModel::class.java)




        setOnClickListener()
        return binding.root


    }

    private fun setOnClickListener(){



        binding.seatText.setOnClickListener {
            startActivity( Intent(activity, CreateSeatActivity::class.java))
        }

        binding.genderSelectBtn.setOnClickListener {
            startActivity( Intent(activity, CreateGenderActivity::class.java))
        }

        // 히스토리 날짜 입력
        binding.dateSelectBtn.setOnClickListener {
            val datePicker = DatePickerDialog(
                activity!!,
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
        binding.editReservationTime.setOnClickListener {
            val timePicker = TimePickerDialog(
                activity!!, R.style.TimePicker,
                TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                    //TimePicker 특성 상 한자리 시간 입력에 대한 대응을 해줘야 함
                    historyTime =
                        if (hourOfDay < 10) "0${hourOfDay}시"
                        else hourOfDay.toString() + "시"
                    historyTime +=
                        if (minute < 10) " 0${minute}분"
                        else " ${minute}분"
                    binding.editReservationTime.text =historyTime.toString()

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
}