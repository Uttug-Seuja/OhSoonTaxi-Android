package com.example.myapplication.ui.home

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.example.myapplication.R
import com.example.myapplication.adapter.CalendarAdapter
import com.example.myapplication.data.CalendarDateModel
import com.example.myapplication.databinding.FragmentHomeBinding
import com.example.myapplication.ui.search.SearchActivity
import com.junjange.soondong.utils.HorizontalItemDecoration
import java.text.SimpleDateFormat
import java.util.*


class HomeFragment : Fragment(),  CalendarAdapter.ItemClickListener{

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModel: HomeViewModel
    private val sdf = SimpleDateFormat("yyyy년 MMMM", Locale.KOREA)
    private val cal = Calendar.getInstance(Locale.KOREA)
    private val sdfRv = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
    private val currentDate = Calendar.getInstance(Locale.KOREA)
    private val dates = ArrayList<Date>()
    private lateinit var calendarAdapter: CalendarAdapter
    private val calendarDateList = ArrayList<CalendarDateModel>()

    fun newInstance(): HomeFragment {
        return HomeFragment()
    }

    override fun onGetLayoutInflater(savedInstanceState: Bundle?): LayoutInflater {
        val inflater = super.onGetLayoutInflater(savedInstanceState)
        val contextThemeWrapper: Context = ContextThemeWrapper(requireContext(), R.style.Theme_home_OhSoonTaxiAndroid)
        return inflater.cloneInContext(contextThemeWrapper)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("ttt", "onCreate 실행")

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        val themedInflater =
            inflater.cloneInContext(ContextThemeWrapper(requireActivity(), R.style.Theme_home_OhSoonTaxiAndroid))


        _binding = FragmentHomeBinding.inflate(themedInflater, container, false)

//        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        setUpDateAdapter()
        setUpDateClickListener()
        setUpCalendar(currentDate.get(Calendar.DATE) - 1)
        binding.recyclerView.scrollToPosition(currentDate.get(Calendar.DATE) - 1)

        binding.searchViewLayout.setOnClickListener {
            val intent = Intent(activity, SearchActivity::class.java)
            startActivity(intent)
        }

        return binding.root


    }

//    private fun setMatchView(){
//        matchAdapter =  MatchAdapter(this,this).apply {
//            setHasStableIds(true) // 리사이클러 뷰 업데이트 시 깜빡임 방지
//        }
//        binding.rvMatch.adapter = matchAdapter
//    }

//    private fun setObserver() {
//        viewModel.retrofitReservesInfoRetrofit(sportType, sdfRv.format(cal.time).toString())
//        viewModel.reservesSportDateText.observe(this){
//            viewModel.reservesSportDateText.value.let {
//                matchAdapter.setData(it!!.reservesSportDateData)
//            }
//        }
//    }

    /**
     * 클릭 리스너 설정
     */
    private fun setUpDateClickListener() {
        binding.ivCalendarNext.setOnClickListener {
            cal.add(Calendar.MONTH, 1)
            if (cal == currentDate) {
                setUpCalendar(currentDate.get(Calendar.DATE) -1)
                binding.recyclerView.scrollToPosition(currentDate.get(Calendar.DATE) - 1 + 4)
                Log.d("ttt", currentDate.get(Calendar.DATE).toString())

            }else{
                setUpCalendar(0)
                binding.recyclerView.scrollToPosition(0)

            }
        }
        binding.ivCalendarPrevious.setOnClickListener {
            cal.add(Calendar.MONTH, -1)

            if (cal == currentDate) {
                setUpCalendar(currentDate.get(Calendar.DATE) -1 )
                binding.recyclerView.scrollToPosition(currentDate.get(Calendar.DATE) - 1 + 4)

            }else{
                setUpCalendar(0)
                binding.recyclerView.scrollToPosition(0)

            }
        }
    }

    /**
     * 리사이클러뷰용 어댑터 설정
     */
    private fun setUpDateAdapter() {
        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.single_calendar_margin)
        binding.recyclerView.addItemDecoration(HorizontalItemDecoration(spacingInPixels))
        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.recyclerView)

        calendarAdapter =  CalendarAdapter(this).apply {
            setHasStableIds(true) // 리사이클러 뷰 업데이트 시 깜빡임 방지
        }

        binding.recyclerView.adapter = calendarAdapter
    }

    /**
     * 매월 달력을 설정하는 기능
     */
    private fun setUpCalendar(toDay : Int) {
        val calendarList = ArrayList<CalendarDateModel>()
        binding.tvDateMonth.text = sdf.format(cal.time)

        val monthCalendar = cal.clone() as Calendar
        val maxDaysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        dates.clear()

        monthCalendar.set(Calendar.DAY_OF_MONTH, 1)
        while (dates.size < maxDaysInMonth) {
            dates.add(monthCalendar.time)
            calendarList.add(CalendarDateModel(monthCalendar.time))
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        calendarDateList.clear()
        calendarDateList.addAll(calendarList)
        calendarDateList[toDay].isSelected = true

        calendarAdapter.setData(calendarList)
    }

    // 클릭 리스너
//    override fun onItemClickListener(item: CalendarDateModel, position: Int) {
//        calendarDateList.forEachIndexed { index, calendarModel ->
//            if(index == position){
//                calendarModel.isSelected = true
//                viewModel.retrofitReservesInfoRetrofit(sportType, sdfRv.format(calendarModel.data).toString())
//
//            }else{
//                calendarModel.isSelected = false
//            }
//        }
//        calendarAdapter.setData(calendarDateList)
//
//    }
//
//    override fun onItemClickListener(item: Match, position: Int) {
//        TODO("Not yet implemented")
//    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home->{ // 메뉴 버튼
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onItemClickListener(item: CalendarDateModel, position: Int) {
        TODO("Not yet implemented")
    }

}