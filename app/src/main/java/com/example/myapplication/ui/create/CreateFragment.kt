package com.example.myapplication.ui.create

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.Application
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.BuildConfig
import com.example.myapplication.R
import com.example.myapplication.adapter.KakaoLocalAdapter
import com.example.myapplication.data.ModelKakaoLocal
import com.example.myapplication.data.ResultSearchKeyword
import com.example.myapplication.databinding.FragmentCreateBinding
import com.example.myapplication.network.KakaoLocalInterface
import com.example.myapplication.utils.textChangesToFlow
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.collections.ArrayList
import kotlin.coroutines.CoroutineContext


class CreateFragment : Fragment() {

    private var _binding: FragmentCreateBinding? = null
    private val binding get() = _binding!!
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            CreateViewModel.Factory(application = Application())
        )[CreateViewModel::class.java]
    }

    private var title: String? = null
    private var sports: String? = null
    private var place: String? = null
    private var recruit: String? = null
    private var gender: String? = null
    private var matchingDate: String? = null
    private var matchingStartTime: String? = null
    private var matchingEndTime: String? = null
    private var content: String? = null
    private var member: Int? = null
    private val genderMap =
        hashMapOf<String, String>("남녀 모두" to "ALL", "남자만" to "MAN", "여자만" to "WOMAN")

    var historyTitle: String? = null
    var historyMood: String? = null
    var historyComment: String? = null
    var historyPlaceTitle: String? = null
    var historyUserID: Int? = null
    var historyCreatedAt: String? = null  // historyDate + historyTime
    var historyDate: String? = null
    var historyTime: String? = null

    var startPlaceX: Double? = null
    var startPlaceY: Double? = null
    var destinationX: Double? = null
    var destinationY: Double? = null

    // Date, Time Picker 기본 값으로 사용될 현재 날짜 및 시각 가져옴
    private val calendarInstance = Calendar.getInstance()
    private val year = calendarInstance.get(Calendar.YEAR)
    private val month = calendarInstance.get(Calendar.MONTH)
    private val day = calendarInstance.get(Calendar.DAY_OF_MONTH)
    private val hour = calendarInstance.get(Calendar.HOUR_OF_DAY)
    private val minute = calendarInstance.get(Calendar.MINUTE)


    companion object {
        const val BASE_URL = "https://dapi.kakao.com/"
        const val API_KEY = BuildConfig.KAKAO_MAP_REST_API_KEY  // REST API 키

    }

    private val listItems = arrayListOf<ModelKakaoLocal>()   // 리사이클러 뷰 아이템
    private val listAdapter = KakaoLocalAdapter(listItems)    // 리사이클러 뷰 어댑터
    private var pageNumber = 1      // 검색 페이지 번호
    private var keyword = ""        // 검색 키워드
    private var flag = false

    private var startPlaceCoroutineJob: Job = Job()
    private val startPlaceCoroutineContext: CoroutineContext
        get() = Dispatchers.IO + startPlaceCoroutineJob

    private var destinationCoroutineJob: Job = Job()
    private val destinationCoroutineContext: CoroutineContext
        get() = Dispatchers.IO + destinationCoroutineJob

    // 서치뷰 에딧 텍스트
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

        // 키보드 설정
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        // 리사이클러 뷰
        binding.rvStartPlaceList.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.rvStartPlaceList.adapter = listAdapter

        binding.rvDestinationList.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.rvDestinationList.adapter = listAdapter

        // 리스트 아이템 클릭 시 해당 위치로 이동
        listAdapter.setItemClickListener(object : KakaoLocalAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int, itemList: ArrayList<ModelKakaoLocal>) {
//                val mapPoint = MapPoint.mapPointWithGeoCoord(listItems[position].y, listItems[position].x)
//                binding.mapView.setMapCenterPointAndZoomLevel(mapPoint, 1, true)




                if (binding.editStartPlace.isFocused) {
                    binding.rvStartPlaceList.visibility = View.GONE
                    binding.editStartPlace.clearFocus()
                    binding.editStartPlace.setText(itemList[position].name)
                    Log.d("ttt22", binding.editStartPlace.isFocused.toString())
                    startPlaceX = itemList[position].x
                    startPlaceY = itemList[position].y

                    Log.d("ttt", itemList[position].x.toString())
                    Log.d("ttt", itemList[position].y.toString())


                }

                if (binding.editDestination.isFocused) {
                    binding.rvDestinationList.visibility = View.GONE
                    binding.editDestination.clearFocus()
                    binding.editDestination.setText(itemList[position].name)
                    destinationX = itemList[position].x
                    destinationY = itemList[position].y
                    Log.d("ttt", itemList[position].x.toString())
                    Log.d("ttt", itemList[position].y.toString())
                }
                imm.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
                listItems.clear()


            }
        })

        binding.editStartPlace.apply {
            this.hint = "어디서 탈래요?"

            Log.d("ttt", "호잇?")
            // EditText 에 포커스가 갔을 때 ClearButton 활성화
            this.setOnFocusChangeListener { v, hasFocus ->
                flag = !hasFocus
            }

            // 서치뷰에서 에딧텍스트를 가져온다.
            startPlaceSearchViewEditText = binding.editStartPlace

//            binding.rvList.visibility = View.VISIBLE
            GlobalScope.launch(context = startPlaceCoroutineContext) {

                // editText 가 변경되었을때
                val editTextFlow = startPlaceSearchViewEditText.textChangesToFlow()

                editTextFlow
                    // 연산자들
                    // 입려되고 나서 0.2초 뒤에 받는다
                    .debounce(200)
                    .filter {
                        it?.length!! >= 0 && !flag
                    }
                    .onEach {
                        Log.d(ContentValues.TAG, "flow로 받는다 $it")

                        // 해당 검색어로 api 호출
                        keyword = it.toString()
                        pageNumber = 1
                        searchKeyword(keyword, pageNumber, 0)

//                        searchPhotoApiCall(it.toString())
                    }
                    .launchIn(this)
            }

        }


        binding.editDestination.apply {
            this.hint = "어디로 갈까요?"

            Log.d("ttt", "호잇?")
            // EditText 에 포커스가 갔을 때 ClearButton 활성화
            this.setOnFocusChangeListener { v, hasFocus ->
                flag = !hasFocus
            }

            // 서치뷰에서 에딧텍스트를 가져온다.
            destinationSearchViewEditText = binding.editDestination

//            binding.rvList.visibility = View.VISIBLE
            GlobalScope.launch(context = destinationCoroutineContext) {

                // editText 가 변경되었을때
                val editTextFlow = destinationSearchViewEditText.textChangesToFlow()

                editTextFlow
                    // 연산자들
                    // 입려되고 나서 0.2초 뒤에 받는다
                    .debounce(200)
                    .filter {
                        it?.length!! >= 0 && !flag

                    }
                    .onEach {
                        Log.d(ContentValues.TAG, "flow로 받는다 $it")

                        // 해당 검색어로 api 호출
                        keyword = it.toString()
                        pageNumber = 1
                        searchKeyword(keyword, pageNumber, 1)

//                        searchPhotoApiCall(it.toString())
                    }
                    .launchIn(this)
            }

        }


        val bottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet_seat_layout, null)
        bottomSheetDialog = BottomSheetDialog(requireActivity())
        bottomSheetDialog!!.setContentView(bottomSheetView)

        binding.seatSelectBtn.setOnClickListener {


            for (i in 0..3) {
                if (i == seatAnswer){
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
                    binding.seatText.text = "${i + 1}번  좌석"
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
            CreateGenderActivity.createIntent(requireActivity(), "A Child Callback OK!!").also {
                genderForResult.launch(it)
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

    private val seatForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                RESULT_OK -> {
                    binding.seatText.text = result.data!!.getStringExtra("seatType")
                }
            }
        }

    // 키워드 검색 함수
    private fun searchKeyword(keyword: String, page: Int, type: Int) {
        val retrofit = Retrofit.Builder()          // Retrofit 구성
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(KakaoLocalInterface::class.java)            // 통신 인터페이스를 객체로 생성
        val call = api.getSearchKeyword(API_KEY, keyword, page)    // 검색 조건 입력

        // API 서버에 요청
        call.enqueue(object : Callback<ResultSearchKeyword> {
            override fun onResponse(
                call: Call<ResultSearchKeyword>,
                response: Response<ResultSearchKeyword>
            ) {

                // 통신 성공
                addItemsAndMarkers(response.body(), type)
                Log.d("Ttt", "성공")
            }

            override fun onFailure(call: Call<ResultSearchKeyword>, t: Throwable) {
                // 통신 실패
                Log.w("LocalSearch", "통신 실패: ${t.message}")
            }
        })
    }

    // 검색 결과 처리 함수
    @SuppressLint("NotifyDataSetChanged")
    fun addItemsAndMarkers(searchResult: ResultSearchKeyword?, type: Int) {
        if (!searchResult?.documents.isNullOrEmpty()) {

            if (type == 0) {
                binding.rvStartPlaceList.visibility = View.VISIBLE

            } else {
                binding.rvDestinationList.visibility = View.VISIBLE
            }
//            binding.rvStartPlaceList.visibility = View.VISIBLE
//            binding.noResultCard.visibility = View.GONE

            // 검색 결과 있음
            listItems.clear()                   // 리스트 초기화
//            binding.mapView.removeAllPOIItems() // 지도의 마커 모두 제거
//            addMyPmdMarker() // My PMD 마커 추가

            for (document in searchResult!!.documents) {

                // 결과를 리사이클러 뷰에 추가
                val item = ModelKakaoLocal(
                    document.place_name,
                    document.road_address_name,
                    document.address_name,
                    document.x.toDouble(),
                    document.y.toDouble()
                )
                listItems.add(item)

                // 지도에 마커 추가
//                val point = MapPOIItem()
//                point.apply {
//                    itemName = document.place_name // 마커 이름
//                    mapPoint = MapPoint.mapPointWithGeoCoord( // 좌표
//                        document.y.toDouble(),
//                        document.x.toDouble()
//                    )
//                    markerType = MapPOIItem.MarkerType.BluePin // 마커 모양
//                    selectedMarkerType = MapPOIItem.MarkerType.RedPin // 클릭 시 마커 모양
//                }
//                binding.mapView.addPOIItem(point)
            }
            listAdapter.notifyDataSetChanged()


        } else {
            // 검색 결과 없음
            binding.rvStartPlaceList.visibility = View.GONE
            binding.rvDestinationList.visibility = View.GONE

//            binding.noResultCard.visibility = View.VISIBLE

        }
    }


    override fun onDestroy() {
        listItems.clear()                   // 리스트 초기화
        startPlaceCoroutineContext.cancel()  // MemoryLeak 방지를 위해 myCoroutineContext 해제
        destinationCoroutineContext.cancel()  // MemoryLeak 방지를 위해 myCoroutineContext 해제

        super.onDestroy()
    }

}