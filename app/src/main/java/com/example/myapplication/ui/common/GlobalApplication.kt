package com.example.myapplication.ui.common

import android.app.Application
import com.example.myapplication.ui.common.base.DataStoreModule
import org.conscrypt.Conscrypt
import java.security.Security

class GlobalApplication : Application() {

    private lateinit var dataStore : DataStoreModule

    companion object {
        private lateinit var globalApplication: GlobalApplication
        fun getInstance() : GlobalApplication = globalApplication
    }

    override fun onCreate() {
        super.onCreate()
        globalApplication = this
        dataStore = DataStoreModule(this)
        Security.insertProviderAt(Conscrypt.newProvider(), 1);

        // 다른 초기화 코드들
//        KakaoSdk.init(this, BuildConfig.KAKAO_MAP_NATIVE_KEY)

    }



    fun getDataStore() : DataStoreModule = dataStore

}