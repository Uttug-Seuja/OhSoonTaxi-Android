package com.example.myapplication.network

import com.example.myapplication.common.utils.API
import com.example.myapplication.data.Login
import com.example.myapplication.data.ReservesEdit
import com.example.myapplication.data.ReservesSportDate
import com.example.myapplication.data.User
import com.example.myapplication.repository.ReservesCreation
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.*
import retrofit2.http.Body
import retrofit2.http.POST




interface RetrofitInterface {
    // 스포츠와 날짜 별로 경기 조회
    @Headers("Content-Type: application/json")
    @GET(API.GET_RESERVES_SPORT_DATE)
    suspend fun getReservesSportDate(
        @Query("sport") sport : String,
        @Query("day") day : String,


        ): ReservesSportDate

    // 회원가입
    @Headers("Content-Type: application/json")
    @POST(API.POST_USERS_CREATION)
    suspend fun postUsersCreation(
        @Body user: User,
    ): Unit


    // 로그인
    @Headers("Content-Type: application/json")
    @POST(API.POST_USERS_LOGIN)
    suspend fun postUsersLogin(
        @Body login: Login,
    ): Unit


    // 경기 생성
    @Headers("Content-Type: application/json")
    @POST(API.POST_RESERVES_CREATION)
    suspend fun postReservesCreation(
        @Body reservesCreation: ReservesCreation,
    ): Unit

    // 경기 수정
    @Headers("Content-Type: application/json")
    @POST(API.POST_RESERVES_EDIT)
    suspend fun postReservesEdit(
        @Body reservesEdit: ReservesEdit,
    ): Unit


}