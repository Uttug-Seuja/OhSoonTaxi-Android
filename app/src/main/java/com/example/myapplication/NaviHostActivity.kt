package com.example.myapplication

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
//import android.support.v4.app.ActivityCompat
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.myapplication.databinding.ActivityNaviHostBinding
import com.example.myapplication.ui.create.CreateActivity
import com.example.myapplication.ui.home.HomeFragment
import com.example.myapplication.ui.my_info.MyInfoFragment
import com.example.myapplication.ui.notifications.NotificationsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class NaviHostActivity : AppCompatActivity() {

    private lateinit var homeFragment: HomeFragment
    private lateinit var notificationsFragment: NotificationsFragment
    private lateinit var myInfoFragment: MyInfoFragment


    private lateinit var binding: ActivityNaviHostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNaviHostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 바텀네비게이션 변수 실행
        binding.navView.setOnNavigationItemSelectedListener(onBottomNavItemSelectedListener)

        // 처음 뜨는 fragment 설정
        homeFragment = HomeFragment().newInstance()
        supportFragmentManager.beginTransaction().add(R.id.nav_host_fragment_activity_main, HomeFragment().newInstance()).commit()

        // Get permission
        val permissionList = arrayOf<String>(
            // 위치 권한
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        )

        // 권한 요청
        ActivityCompat.requestPermissions(this@NaviHostActivity, permissionList, 1)

    }

    // 바텀 네비게이션 아이템 클릭 리스터 설정
    private val onBottomNavItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener{

        when (it.itemId) {
            R.id.navigation_home -> {
                homeFragment = HomeFragment().newInstance()
                supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment_activity_main, homeFragment).commit()
                Log.d("ttt","홈")

            }
            R.id.navigation_create -> {
                val intent = Intent(this, CreateActivity::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener false
//                createFragment = CreateFragment().newInstance()
//                supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment_activity_main, createFragment).commit()
                Log.d("ttt","추가하기")


            }
            R.id.navigation_notifications-> {
                notificationsFragment = NotificationsFragment().newInstance()
                supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment_activity_main, notificationsFragment).commit()
                Log.d("ttt","알림 목록")

            }
            R.id.navigation_my_info -> {
                myInfoFragment = MyInfoFragment().newInstance()
                supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment_activity_main, myInfoFragment).commit()
                Log.d("ttt","내 정보")

            }

        }
        true
    }

    // 뒤로가기 2번 눌러야 종료
    private val FINISH_INTERVAL_TIME: Long = 2500
    private var backPressedTime: Long = 0
    private var toast: Toast? = null
    override fun onBackPressed() {
        val tempTime = System.currentTimeMillis()
        val intervalTime = tempTime - backPressedTime


        // 뒤로 가기 할 경우 홈 화면으로 이동
        if (intervalTime in 0..FINISH_INTERVAL_TIME) {
            super.onBackPressed()
            // 뒤로가기 토스트 종료
            toast!!.cancel()
            finish()
        } else {
            backPressedTime = tempTime
            toast =
                Toast.makeText(applicationContext, "'뒤로'버튼 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT)
            toast!!.show()
        }
    }



}