package com.example.myapplication.ui.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R
import com.example.myapplication.adapter.CustomBalloonAdapter
import com.example.myapplication.adapter.MarkerEventListener
import com.example.myapplication.databinding.ActivityDetailBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView


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

    private val seatSelectButton = mutableListOf(
        R.id.seat1_select_btn,
        R.id.seat2_select_btn,
        R.id.seat3_select_btn,
        R.id.seat4_select_btn
    )

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.viewmodel = viewModel
        val customBalloonAdapter = CustomBalloonAdapter(layoutInflater)

        binding.mapView.setPOIItemEventListener(eventListener)  // 마커 클릭 이벤트 리스너 등록
        binding.mapView.setCalloutBalloonAdapter(customBalloonAdapter)  // 커스텀 말풍선 등록

        binding.mapView.currentLocationTrackingMode =
            MapView.CurrentLocationTrackingMode.TrackingModeOff
        val mapPoint = MapPoint.mapPointWithGeoCoord((36.77319581029296 + 36.77319581029296) / 2 , (126.93359085592283 + 126.951393082675) / 2)
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



        val bottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet_seat_layout, null)
        bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog!!.setContentView(bottomSheetView)

        bottomSheetView.findViewById<TextView>(R.id.seat1_select_btn).text = "김찬우\n18학번"


        binding.applyBtn.setOnClickListener {


            for (i in 0..3) {
                if (i == seatAnswer){
                    bottomSheetView.findViewById<View>(seatSelectButton[seatAnswer])
                        .setBackgroundResource(R.drawable.seat_selected)
                    continue

                }
                bottomSheetView.findViewById<View>(seatSelectButton[i])
                    .setBackgroundResource(R.drawable.seat_un_selected)
            }
            bottomSheetView.findViewById<View>(R.id.seat1_select_btn)
                .setBackgroundResource(R.drawable.seat_selected)

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

    }

    // 검색 결과 처리 함수
    @SuppressLint("NotifyDataSetChanged")
    fun addItemsAndMarkers() {


        // 검색 결과 있음
        binding.mapView.removeAllPOIItems() // 지도의 마커 모두 제거

        // 지도에 마커 추가
        val point = MapPOIItem()
        point.apply {
            itemName = "순천향대학교 후문"// 마커 이름
            mapPoint = MapPoint.mapPointWithGeoCoord( // 좌표
                36.77319581029296,

                126.93359085592283

            )
            markerType = MapPOIItem.MarkerType.BluePin // 마커 모양
            selectedMarkerType = MapPOIItem.MarkerType.RedPin // 클릭 시 마커 모양
        }

        binding.mapView.addPOIItem(point)

        point.apply {
            itemName = "신창역"// 마커 이름
            mapPoint = MapPoint.mapPointWithGeoCoord( // 좌표
                36.7696422998843,
                126.951393082675

            )
            markerType = MapPOIItem.MarkerType.BluePin // 마커 모양
            selectedMarkerType = MapPOIItem.MarkerType.RedPin // 클릭 시 마커 모양
        }

        binding.mapView.addPOIItem(point)


    }
}