package com.example.myapplication.common.utils

import com.example.myapplication.BuildConfig

object API {
    const val BASE_URL : String = BuildConfig.API_KEY // 서버 주소

    // participation
    const val POST_PARTICIPATION_ADD : String = "/participation/add"
    const val DELETE_PARTICIPATION : String = "/participation/delete/{reservationId}"
    const val GET_PARTICIPATION_CHECK : String = "/participation/check/{reservationId}"

    // users
    const val POST_USERS_SIGNUP : String = "/user/signUp"
    const val POST_USERS_SIGNIN: String = "/user/signIn"
    const val POST_USERS_CHECK_UNIQUE: String = "/user/checkUnique"
    const val POST_USERS_SIGN_OUT: String = "/user/signOut"

    // reserves
    const val POST_RESERVES_ADD : String = "/reservation/add"
    const val DELETE_RESERVES : String = "/reservation/delete/{reservationId}"
    const val GET_RESERVES : String = "/reservation/{reserveId}"
    const val GET_RESERVES_LIST : String = "/reservation/list"
    const val GET_RESERVES_PASSPHRASE : String = "/reservation/passphrase/{reservationId}"
    const val GET_RESERVES_LIST_RESERVATIONS : String = "/reservation/list/reservations"
    const val GET_RESERVES_LIST_PARTICIPATIONS : String = "/reservation/list/participations"
    const val POST_RESERVES_EDIT : String = "/reservation/edit"
    const val GET_RESERVES_SEARCH_LIST : String = "/reservation/search/list"




}