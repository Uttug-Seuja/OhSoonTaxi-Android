package com.example.myapplication.ui.home

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.example.myapplication.R
import com.example.myapplication.adapter.CalendarAdapter
import com.example.myapplication.adapter.MatchAdapter
import com.example.myapplication.data.CalendarDateModel
import com.example.myapplication.data.ReservesListResponseData
import com.example.myapplication.databinding.FragmentHomeBinding
import com.example.myapplication.ui.detail.DetailActivity
import com.example.myapplication.ui.search.SearchActivity
import com.junjange.soondong.utils.HorizontalItemDecoration
import java.text.SimpleDateFormat
import java.util.*


class HomeFragment : Fragment(), CalendarAdapter.ItemClickListener, MatchAdapter.ItemClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            HomeViewModel.Factory(application = Application())
        )[HomeViewModel::class.java]
    }

    private val sdf = SimpleDateFormat("yyyy년 MMMM", Locale.KOREA)
    private val cal = Calendar.getInstance(Locale.KOREA)
    private val sdfRv = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
    private val currentDate = Calendar.getInstance(Locale.KOREA)
    private val dates = ArrayList<Date>()
    private lateinit var calendarAdapter: CalendarAdapter
    private lateinit var matchAdapter: MatchAdapter
    private val calendarDateList = ArrayList<CalendarDateModel>()


    fun newInstance(): HomeFragment {
        return HomeFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }


    override fun onGetLayoutInflater(savedInstanceState: Bundle?): LayoutInflater {
        val inflater = super.onGetLayoutInflater(savedInstanceState)
        val contextThemeWrapper: Context =
            ContextThemeWrapper(activity!!.applicationContext, R.style.Theme_home_OhSoonTaxiAndroid)
        return inflater.cloneInContext(contextThemeWrapper)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        val themedInflater =
            inflater.cloneInContext(
                ContextThemeWrapper(
                    activity!!.applicationContext,
                    R.style.Theme_home_OhSoonTaxiAndroid
                )
            )
//        inflater.context.setTheme(R.style.Theme_home_OhSoonTaxiAndroid)

//        binding = FragmentHomeBinding.inflate(themedInflater, container, false)
        _binding = FragmentHomeBinding.inflate(themedInflater, container, false)
        binding.viewmodel = viewModel

//        activity!!.window.statusBarColor = R.color.drawer_background
        binding.lifecycleOwner = viewLifecycleOwner

        setUpDateAdapter()
        setUpDateClickListener()
        setMatchView()
        setObserver()
        setUpCalendar(currentDate.get(Calendar.DATE) - 1)
        binding.recyclerView.scrollToPosition(currentDate.get(Calendar.DATE) - 1)

        lifecycleScope.launchWhenStarted {
            viewModel.toastMessage.collect { message ->
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.navigationEvent.collect {
                when (it) {
                    is HomeNavigationAction.NavigateToSearch -> {
                        val intent = Intent(activity, SearchActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
        }


        return binding.root

    }


    private fun setMatchView() {
        matchAdapter = MatchAdapter(this, requireActivity()).apply {
            setHasStableIds(true) // 리사이클러 뷰 업데이트 시 깜빡임 방지
        }
        binding.rvMatch.adapter = matchAdapter
    }

    private fun setObserver() {
        Log.d("ttt123", sdfRv.format(cal.time))

        viewModel.reservesDateRetrofit(sdfRv.format(cal.time).toString())
        Log.d("Ttt", "???")

        lifecycleScope.launchWhenStarted {
            viewModel.reservesSportDateEvent.collect {
                Log.d("Ttt", it.reservesListResponseData.toString())
                if (it.reservesListResponseData.isEmpty()) {
                    binding.rvMatch.visibility = View.GONE
                    binding.noResultCard.visibility = View.VISIBLE
                } else {
                    matchAdapter.setData(it.reservesListResponseData)
                    binding.rvMatch.visibility = View.VISIBLE
                    binding.noResultCard.visibility = View.GONE

                }

            }
        }
    }

    /**
     * 클릭 리스너 설정
     */
    private fun setUpDateClickListener() {
        binding.ivCalendarNext.setOnClickListener {
            cal.add(Calendar.MONTH, 1)
            if (cal == currentDate) {
                setUpCalendar(currentDate.get(Calendar.DATE) - 1)
                binding.recyclerView.scrollToPosition(currentDate.get(Calendar.DATE) - 1 + 4)
                Log.d("ttt", currentDate.get(Calendar.DATE).toString())

            } else {
                setUpCalendar(0)
                binding.recyclerView.scrollToPosition(0)

            }
        }
        binding.ivCalendarPrevious.setOnClickListener {
            cal.add(Calendar.MONTH, -1)

            if (cal == currentDate) {
                setUpCalendar(currentDate.get(Calendar.DATE) - 1)
                binding.recyclerView.scrollToPosition(currentDate.get(Calendar.DATE) - 1 + 4)

            } else {
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

        calendarAdapter = CalendarAdapter(this).apply {
            setHasStableIds(true) // 리사이클러 뷰 업데이트 시 깜빡임 방지
        }

        binding.recyclerView.adapter = calendarAdapter
    }

    /**
     * 매월 달력을 설정하는 기능
     */
    private fun setUpCalendar(toDay: Int) {
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


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> { // 메뉴 버튼
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onItemClickListener(item: CalendarDateModel, position: Int) {

        calendarDateList.forEachIndexed { index, calendarModel ->


            calendarModel.isSelected = index == position
            if (index == position) {
                calendarModel.isSelected = true
                viewModel.reservesDateRetrofit(
                    sdfRv.format(calendarModel.data).toString()
                )

            } else {
                calendarModel.isSelected = false
            }

        }
        calendarAdapter.setData(calendarDateList)

    }

    private val registerForActivityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val id = result.data?.getStringExtra("id") ?: ""
                val password = result.data?.getStringExtra("password") ?: ""
                viewModel.reservesDateRetrofit(id)

                Log.d("ttt", id)
                Log.d("ttt", password)

            }
        }

    override fun onItemClickListener(item: ReservesListResponseData, position: Int) {

        // 원하는 화면 연결
        val intent = Intent(requireActivity(), DetailActivity::class.java)
        intent.putExtra("reserveId", item.id)
        registerForActivityResult.launch(intent)


    }

}