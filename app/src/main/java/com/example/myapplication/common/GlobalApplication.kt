package com.example.myapplication.common

import android.app.Application
import org.conscrypt.Conscrypt
import java.security.Security

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Security.insertProviderAt(Conscrypt.newProvider(), 1);

        // 다른 초기화 코드들
//        KakaoSdk.init(this, BuildConfig.KAKAO_MAP_NATIVE_KEY)

    }
}