package com.example.myapplication.ui.detail

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.NavHostActivity
import com.example.myapplication.R
import com.example.myapplication.adapter.CustomBalloonAdapter
import com.example.myapplication.adapter.MarkerEventListener
import com.example.myapplication.data.Participations
import com.example.myapplication.databinding.ActivityDetailBinding
import com.example.myapplication.ui.update.UpdateActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.roundToLong
import kotlin.math.sin


class DetailActivity : AppCompatActivity() {

    private val binding by lazy { ActivityDetailBinding.inflate(layoutInflater) }
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            DetailViewModel.Factory(application)
        )[DetailViewModel::class.java]
    }
    private val eventListener = MarkerEventListener(this)   // 마커 클릭 이벤트 리스너
    private var bottomSheetDialog: BottomSheetDialog? = null
    private val seatAnswerList = mutableListOf<Boolean>(false, false, false, false)
    private var seatAnswer = -1
    private var reserveDate: String? = null
    private val gender =
        hashMapOf<String, String>("ALL" to "남녀모두", "MAN" to "남자만", "WOMAN" to "여자만")
    private val reserveStatus =
        hashMapOf<String, String>("POSSIBLE" to "좌석선택", "IMMINENT" to "좌석선택", "DEADLINE" to "신청 마감")
    private val stateTextColor = hashMapOf<String, String>(
        "POSSIBLE" to "#FFFFFF",
        "IMMINENT" to "#FFFFFF",
        "DEADLINE" to "#cccccc"
    )
    private val stateBtnColor = hashMapOf<String, String>(
        "POSSIBLE" to "#1570ff",
        "IMMINENT" to "#1570ff",
        "DEADLINE" to "#EEEEEE"
    )
    private var startingPlace: String? = null
    private var destination: String? = null
    private var startLatitude: Double? = null
    private var startLongitude: Double? = null
    private var finishLatitude: Double? = null
    private var finishLongitude: Double? = null
    private var currentNum: Int? = null
    private var challengeWord: String? = null
    private var countersignWord: String? = null
    private var reserveId: String? = null


    private val seatSelectButton = mutableListOf(
        R.id.seat1_select_btn,
        R.id.seat2_select_btn,
        R.id.seat3_select_btn,
        R.id.seat4_select_btn
    )

    private var selectSeatPosition: MutableList<Participations>? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.viewmodel = viewModel

        setSupportActionBar(binding.mainToolbar) // 툴바를 액티비티의 앱바로 지정
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // 드로어를 꺼낼 홈 버튼 활성화
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_previous) // 홈버튼 이미지 변경
        supportActionBar?.setDisplayShowTitleEnabled(false) // 툴바에 타이틀 안보이게
        reserveId = intent.getIntExtra("reserveId", 0).toString()

        viewModel.reservesRetrofit(reserveId!!.toInt())

        lifecycleScope.launchWhenStarted {
            viewModel.retrofitReservesEvent.collect {

                val createdAtSplit = it.createdAt.split("T")
                val createdAtSplitDate = createdAtSplit[0]
                val createdAtSplitTime = createdAtSplit[1].substring(0, 5)


                binding.mainToolbarText.text = "${it.startingPlace} -> ${it.destination}"
                binding.nameText.text = "${it.name}(${gender[it.sex]})"
                binding.schoolNumText.text = "${it.schoolNum.substring(2, 4)}학번"
                binding.titleText.text = it.title
                binding.createdAtText.text = "$createdAtSplitDate $createdAtSplitTime"
                binding.startingPlaceText.text = it.startingPlace
                binding.destinationText.text = it.destination
                binding.reserveTimeText.text = it.reserveTime.substring(0, 5)
                binding.reserveDateText.text = it.reserveDate
                binding.sexText.text = gender[it.sex]
                binding.passengerNumText.text = it.passengerNum.toString()
                binding.restText.text = (it.passengerNum - it.currentNum).toString()
                startLatitude = it.startLatitude
                startLongitude = it.startLongitude
                finishLatitude = it.finishLatitude
                finishLongitude = it.finishLongitude
                startingPlace = it.startingPlace
                destination = it.destination
                currentNum = it.currentNum
                reserveDate = it.reserveDate
                selectSeatPosition = it.participations
                makeMap()
                makeBottomSheetView()


                binding.applyBtn.setCardBackgroundColor(Color.parseColor(stateBtnColor[it.reservationStatus]))
                binding.applyText.text = reserveStatus[it.reservationStatus]
                binding.applyText.setTextColor(Color.parseColor(stateTextColor[it.reservationStatus]))

                challengeWord = it.challengeWord
                countersignWord = it.countersignWord


            }
        }


        lifecycleScope.launchWhenStarted {
            viewModel.toastMessage.collect { message ->
                Toast.makeText(this@DetailActivity, message, Toast.LENGTH_SHORT).show()
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.navigationEvent.collect {
                when (it) {
                    is DetailNavigationAction.NavigateToBackNav -> {
                        val intent = Intent(this@DetailActivity, NavHostActivity::class.java)
                        intent.putExtra("id", reserveDate)
                        intent.putExtra("password", "이건희")
                        setResult(RESULT_OK, intent)
                        finish()
                    }
                }
            }
        }


    }

    private fun makeMap() {
        val customBalloonAdapter = CustomBalloonAdapter(layoutInflater)

        binding.mapView.setPOIItemEventListener(eventListener)  // 마커 클릭 이벤트 리스너 등록
        binding.mapView.setCalloutBalloonAdapter(customBalloonAdapter)  // 커스텀 말풍선 등록


        binding.mapView.currentLocationTrackingMode =
            MapView.CurrentLocationTrackingMode.TrackingModeOff
        val mapPoint = MapPoint.mapPointWithGeoCoord(
            (startLatitude!! + finishLatitude!!) / 2,
            (startLongitude!! + finishLatitude!!) / 2
        )
        binding.mapView.setMapCenterPointAndZoomLevel(mapPoint, 6, true)

        addItemsAndMarkers()

        binding.mapView.setOnTouchListener(OnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    binding.scrollView.requestDisallowInterceptTouchEvent(true)
                    binding.scrollView.requestDisallowInterceptTouchEvent(true)

                }
                MotionEvent.ACTION_UP -> binding.scrollView.requestDisallowInterceptTouchEvent(true)
                MotionEvent.ACTION_MOVE -> binding.scrollView.requestDisallowInterceptTouchEvent(
                    true
                )
            }
            false
        })


    }


    private fun makeBottomSheetView() {
        val bottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet_seat_layout, null)
        bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog!!.setContentView(bottomSheetView)

        selectSeatPosition!!.forEach {
            seatAnswerList[it.seatPosition] = true
            bottomSheetView.findViewById<TextView>(seatSelectButton[it.seatPosition]).text =
                "${it.schoolNum.substring(2, 4)}학번\n${it.name}"

        }


        binding.applyBtn.setOnClickListener {

            for (i in 0..3) {
                if (seatAnswerList[i]) {
                    bottomSheetView.findViewById<View>(seatSelectButton[i])
                        .setBackgroundResource(R.drawable.seat_selected)
                    bottomSheetView.findViewById<View>(seatSelectButton[i]).isClickable = false
                    continue

                }
                bottomSheetView.findViewById<View>(seatSelectButton[i])
                    .setBackgroundResource(R.drawable.seat_un_selected)
            }
            bottomSheetDialog!!.show()

        }


        bottomSheetView.findViewById<View>(R.id.seat1_select_btn).setOnClickListener {

            if (seatAnswer == 0) {
                bottomSheetView.findViewById<View>(R.id.seat1_select_btn)
                    .setBackgroundResource(R.drawable.seat_un_selected)
                seatAnswer = -1

            } else {
                seatAnswer = 0

                for (i in 0..3) {
                    if (i == seatAnswer) {
                        bottomSheetView.findViewById<View>(seatSelectButton[i])
                            .setBackgroundResource(R.drawable.seat_selected)
                        continue
                    } else if (seatAnswerList[i]) continue
                    else bottomSheetView.findViewById<View>(seatSelectButton[i])
                        .setBackgroundResource(R.drawable.seat_un_selected)
                }
            }

        }
        bottomSheetView.findViewById<View>(R.id.seat2_select_btn).setOnClickListener {
            if (seatAnswer == 1) {
                bottomSheetView.findViewById<View>(R.id.seat2_select_btn)
                    .setBackgroundResource(R.drawable.seat_un_selected)
                seatAnswer = -1

            } else {
                seatAnswer = 1
                for (i in 0..3) {

                    if (i == seatAnswer) {
                        bottomSheetView.findViewById<View>(seatSelectButton[i])
                            .setBackgroundResource(R.drawable.seat_selected)
                        continue
                    } else if (seatAnswerList[i]) continue
                    else bottomSheetView.findViewById<View>(seatSelectButton[i])
                        .setBackgroundResource(R.drawable.seat_un_selected)
                }

            }
        }
        bottomSheetView.findViewById<View>(R.id.seat3_select_btn).setOnClickListener {
            if (seatAnswer == 2) {
                bottomSheetView.findViewById<View>(R.id.seat3_select_btn)
                    .setBackgroundResource(R.drawable.seat_un_selected)
                seatAnswer = -1


            } else {
                seatAnswer = 2

                for (i in 0..3) {

                    if (i == seatAnswer) {
                        bottomSheetView.findViewById<View>(seatSelectButton[i])
                            .setBackgroundResource(R.drawable.seat_selected)
                        continue
                    } else if (seatAnswerList[i]) continue
                    else bottomSheetView.findViewById<View>(seatSelectButton[i])
                        .setBackgroundResource(R.drawable.seat_un_selected)


                }
            }
        }
        bottomSheetView.findViewById<View>(R.id.seat4_select_btn).setOnClickListener {
            if (seatAnswer == 3) {
                bottomSheetView.findViewById<View>(R.id.seat4_select_btn)
                    .setBackgroundResource(R.drawable.seat_un_selected)
                seatAnswer = -1

            } else {
                seatAnswer = 3

                for (i in 0..3) {

                    if (i == seatAnswer) {
                        bottomSheetView.findViewById<View>(seatSelectButton[i])
                            .setBackgroundResource(R.drawable.seat_selected)
                        continue
                    } else if (seatAnswerList[i]) continue
                    else bottomSheetView.findViewById<View>(seatSelectButton[i])
                        .setBackgroundResource(R.drawable.seat_un_selected)


                }
            }
        }


        // 좌석 선택 및 암구호 확인 버튼
        bottomSheetView.findViewById<View>(R.id.seat_answer_btn).setOnClickListener {
            seatAnswer // 사용자가 선택한 좌석
            bottomSheetDialog!!.dismiss()

            viewModel.getParticipationCheckRetrofit(reserveId!!.toInt(), "jo")

            lifecycleScope.launchWhenStarted {
                viewModel.navigationEvent.collect {
                    when (it) {
                        is DetailNavigationAction.NavigateToBackNav -> {
                            val intent = Intent(this@DetailActivity, NavHostActivity::class.java)
                            intent.putExtra("id", reserveDate)
                            intent.putExtra("password", "이건희")
                            setResult(RESULT_OK, intent)
                            finish()
                        }
                    }
                }
            }

            val intent = Intent(this@DetailActivity, PassphraseDialog::class.java)
            intent.putExtra("challengeWord", challengeWord)
            intent.putExtra("countersignWord", countersignWord)
            startActivity(intent)

        }
    }

    // 검색 결과 처리 함수
    @SuppressLint("NotifyDataSetChanged")
    fun addItemsAndMarkers() {

        // 검색 결과 있음
        binding.mapView.removeAllPOIItems() // 지도의 마커 모두 제거

        // 지도에 마커 추가
        var point = MapPOIItem()
        point.apply {
            itemName = startingPlace// 마커 이름
            mapPoint = MapPoint.mapPointWithGeoCoord( // 좌표
                startLatitude!!,
                startLongitude!!

            )
            markerType = MapPOIItem.MarkerType.BluePin // 마커 모양
            selectedMarkerType = MapPOIItem.MarkerType.RedPin // 클릭 시 마커 모양
        }

        binding.mapView.addPOIItem(point)

        point = MapPOIItem()
        point.apply {
            itemName = destination// 마커 이름
            mapPoint = MapPoint.mapPointWithGeoCoord( // 좌표
                finishLatitude!!,
                finishLongitude!!

            )
            markerType = MapPOIItem.MarkerType.BluePin // 마커 모양
            selectedMarkerType = MapPOIItem.MarkerType.RedPin // 클릭 시 마커 모양
        }

        var distance =
            calDist(startLatitude!!, startLongitude!!, finishLatitude!!, finishLongitude!!)


        Log.d("ttt distance", distance.toString())


        // http://www.taxi.or.kr/02/01.php <= 기역별 택시 요금안내
        var fee = 0
        if (distance - 2000 <= 0) {
            binding.feeText.text = "약 3300원"
            fee = 3300
        } else {
            distance -= 2000

            val drivingFee = distance / 131
            fee = (3300 + drivingFee * 100).toInt()
            binding.feeText.text = "약 ${3300 + drivingFee * 100}원"

        }

        if (currentNum == 0) currentNum = 1

        binding.paymentFeeText.text = "약 ${fee / currentNum!!}원"
        binding.mapView.addPOIItem(point)


    }

    //액션버튼 메뉴 액션바에 집어 넣기
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu!!)
        menu.findItem(R.id.edit_btn).isVisible = true
        menu.findItem(R.id.delete_btn).isVisible = true


        return true
    }

    private val registerForActivityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val id = result.data?.getStringExtra("id") ?: ""
                val password = result.data?.getStringExtra("password") ?: ""
//                viewModel.reservesSportDateRetrofit("SOCCER", id)

                Log.d("ttt Detail", id)
                Log.d("ttt", password)

            }
        }

    //액션버튼 클릭 했을 때
    override fun onOptionsItemSelected(item: MenuItem): Boolean {


        when (item.itemId) {
            android.R.id.home -> { // 메뉴 버튼
                finish()
            }

            R.id.report_btn -> {
                Toast.makeText(this, "신고가 접수되었습니다.", Toast.LENGTH_SHORT).show()

            }

            R.id.edit_btn -> {
                val intent = Intent(this@DetailActivity, UpdateActivity::class.java)
                registerForActivityResult.launch(intent)

            }
            R.id.delete_btn -> {
                Toast.makeText(this, "삭제 완료", Toast.LENGTH_SHORT).show()

            }

        }
        return super.onOptionsItemSelected(item)
    }

    // 좌표로 거리구하기
    private fun calDist(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Long {
        val EARTH_R = 6371000.0
        val rad = Math.PI / 180
        val radLat1 = rad * lat1
        val radLat2 = rad * lat2
        val radDist = rad * (lon1 - lon2)

        var distance = sin(radLat1) * sin(radLat2)
        distance += cos(radLat1) * cos(radLat2) * cos(radDist)
        val ret = EARTH_R * acos(distance)

        return ret.roundToLong() // 미터 단위
    }

    override fun onBackPressed() {
        val intent = Intent(this@DetailActivity, NavHostActivity::class.java)
        intent.putExtra("id", reserveDate)
        intent.putExtra("password", "이건희")
        setResult(RESULT_OK, intent)
        finish()
        super.onBackPressed()

    }
}