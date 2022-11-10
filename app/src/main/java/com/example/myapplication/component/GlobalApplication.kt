package com.example.myapplication.component

import android.app.Application
import com.example.myapplication.BuildConfig

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // 다른 초기화 코드들
//        KakaoSdk.init(this, BuildConfig.KAKAO_MAP_NATIVE_KEY)

    }
}