package com.example.myapplication.network

import com.example.myapplication.common.utils.API
import com.example.myapplication.data.*
import com.example.myapplication.data.ReservesCreation
import retrofit2.Response
import retrofit2.http.*
import retrofit2.http.Body
import retrofit2.http.POST


interface RetrofitInterface {
    // 스포츠와 날짜 별로 경기 조회
    @Headers("Content-Type: application/json")
    @GET(API.GET_RESERVES_SPORT_DATE)
    suspend fun getReservesSportDate(
        @Query("sport") sport: String,
        @Query("day") day: String,


        ): ReservesSportDate

    // 회원가입
    @Headers("Content-Type: application/json")
    @POST(API.POST_USERS_CREATION)
    suspend fun postUsersCreation(
        @Body user: User,
    )


    // 로그인
    @Headers("Content-Type: application/json")
    @POST(API.POST_USERS_LOGIN)
    suspend fun postUsersLogin(
        @Body login: Login,
    )


    // 경기 생성
    @Headers("Content-Type: application/json")
    @POST(API.POST_RESERVES_CREATION)
    suspend fun postReservesCreation(
        @Body reservesCreation: ReservesCreation,
    )

    // 경기 수정
    @Headers("Content-Type: application/json")
    @POST(API.POST_RESERVES_EDIT)
    suspend fun postReservesEdit(
        @Body reservesEdit: ReservesEdit,
    )


    // 경기 삭제
    @Headers("Content-Type: application/json")
    @DELETE(API.DELETE_RESERVES)
    suspend fun deleteReserves(
        @Path("reserveId") reserveId: Int,

        )

    // 경기 세부 정보 제공
    @Headers("Content-Type: application/json")
    @GET(API.GET_RESERVES_Info)
    suspend fun getReservesInfo(
        @Path("reserveId") reserveId : Int,


        ): ReservesInfo

    // 참가 신청
    @Headers("Content-Type: application/json")
    @POST(API.POST_PARTICIPATION)
    suspend fun postParticipation(
        @Body participation: Participation,
    )

    // 참가 취소
    @Headers("Content-Type: application/json")
    @HTTP(method = "DELETE", path = API.DELETE_PARTICIPATION, hasBody = true)
    suspend fun deleteParticipation(
        @Body participation: Participation,
    )

    // 해시태그 이름 검색
    @GET("ASdasd")
    suspend fun getHashtagName(
        @Path("keyword") keyword : String
    ): Hashtag



}