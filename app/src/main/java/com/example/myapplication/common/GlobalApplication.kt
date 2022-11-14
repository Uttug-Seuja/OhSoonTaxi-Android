package com.example.myapplication.common

import android.app.Application

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // 다른 초기화 코드들
//        KakaoSdk.init(this, BuildConfig.KAKAO_MAP_NATIVE_KEY)

    }
}