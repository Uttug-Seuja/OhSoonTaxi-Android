package com.example.myapplication.utils

import com.example.myapplication.BuildConfig

object API {
    const val BASE_URL : String = BuildConfig.BASE_URL // 서버 주소

    // participants
    const val POST_PARTICIPATION : String = "/participants/participation"
    const val DELETE_PARTICIPATION : String = "/participants/delete"
    const val GET_PARTICIPANT_USER_INFO : String = "/participants/{reserveId}/users-info"

    // users
    const val POST_USERS_CREATION : String = "/users/creation"
    const val POST_USERS_LOGIN: String = "/users/login"


    // reserves
    const val POST_RESERVES_CREATION : String = "/reserves/creation"
    const val POST_RESERVES_EDIT : String = "/reserves/edit"
    const val DELETE_RESERVES : String = "/reserves/{reserveId}/delete"
    const val GET_RESERVES_SPORT_DATE : String = "/reserves/get/sports-date"
    const val GET_RESERVES_SPORT_TODAY : String = "/reserves/get/today-sport"
    const val GET_RESERVES_Info : String = "/reserves/{reserveId}/reserve-info"

}