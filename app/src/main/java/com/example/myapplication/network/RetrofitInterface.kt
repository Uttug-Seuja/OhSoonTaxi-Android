package com.example.myapplication.network

import com.example.myapplication.ui.common.utils.API
import com.example.myapplication.data.*
import com.example.myapplication.data.ReservesCreation
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.*
import retrofit2.http.Body
import retrofit2.http.POST


interface RetrofitInterface {

    /**User*/
    // 회원가입
    @Headers("Content-Type: application/json")
    @POST(API.POST_USERS_SIGNUP)
    suspend fun postUsersCreation(
        @Body user: User,
    )

    // 로그인
    @Headers("Content-Type: application/json")
    @POST(API.POST_USERS_SIGNIN)
    suspend fun postUsersLogin(
        @Body login: Login,
    )

    // 아이디 중복확인
    @Headers("Content-Type: application/json")
    @GET(API.POST_USERS_CHECK_UNIQUE)
    suspend fun postUsersCheckUnique(
        @Query("uid") uid: String,
    )

    // 로그아읏
    @Headers("Content-Type: application/json")
    @POST(API.POST_USERS_SIGN_OUT)
    suspend fun postUserSignOut(
        @Query("userUid") userUid: String,
    )

    // 내 정보 보기
    @Headers("Content-Type: application/json")
    @GET(API.GET_USERS_MY_INFO)
    suspend fun getUserMyInfo(
        @Query("userUid") userUid: String,
    ): MyInfoResponse

    /**Reservation*/
    // 약속 생성
    @Headers("Content-Type: application/json")
    @POST(API.POST_RESERVES_ADD)
    suspend fun postReservesAdd(
        @Query("userUid") userUid: String,
        @Body reservesCreation: ReservesCreation,
    ) : ReserveIdResponse

    // 경기 삭제
    @Headers("Content-Type: application/json")
    @HTTP(method = "DELETE", path = API.DELETE_RESERVES, hasBody = true)
    suspend fun deleteReserves(
        @Path("reservationId") reservationId: Int,
        @Query("userUid") userUid: String,
    )

    // 약속 세부 정보 제공
    @Headers("Content-Type: application/json")
    @GET(API.GET_RESERVES)
    suspend fun getReserves(
        @Path("reservationId") reservationId: Int,
    ): ReservesResponse

    // 날짜 별로 약속 조회
    @Headers("Content-Type: application/json")
    @GET(API.GET_RESERVES_LIST)
    suspend fun getReservesList(
        @Query("reservationDate") reservationDate: String,
    ): ReservesListResponse

    // 해당 약속 암구호 조회
    @Headers("Content-Type: application/json")
    @GET(API.GET_RESERVES_PASSPHRASE)
    suspend fun getReservesPassphrase(
        @Query("userUid") userUid: String,
        @Path("reservationId") reservationId: Int,

        ): PassphraseResponse

    // 내가 만든 게시글 조회
    @Headers("Content-Type: application/json")
    @GET(API.GET_RESERVES_LIST_RESERVATIONS)
    suspend fun getReservesListReservations(
        @Query("userUid") userUid: String,
    ): ReservesListResponse

    // 내가 참여한 게시글 조회
    @Headers("Content-Type: application/json")
    @GET(API.GET_RESERVES_LIST_PARTICIPATIONS)
    suspend fun getReservesListParticipations(
        @Query("userUid") userUid: String,
    ): ReservesListResponse

    // 경기 수정
    @Headers("Content-Type: application/json")
    @POST(API.POST_RESERVES_EDIT)
    suspend fun postReservesEdit(
        @Query("userUid") userUid: String,
        @Body reservesEdit: ReservesEdit,
    )

    // 게시글 검색
    @GET(API.GET_RESERVES_SEARCH_LIST)
    suspend fun getReservesSearchList(
        @Query("keyword") keyword: String
    ): ReservesListResponse

    /**Participation*/
    // 참가 신청
    @Headers("Content-Type: application/json")
    @POST(API.POST_PARTICIPATION_ADD)
    suspend fun postParticipationAdd(
        @Query("userUid") userUid: String,
        @Body participation: Participation,
    )

    // 참가 취소
    @Headers("Content-Type: application/json")
    @HTTP(method = "DELETE", path = API.DELETE_PARTICIPATION, hasBody = true)
    suspend fun deleteParticipation(
        @Path("reservationId") reservationId: Int,
        @Query("userUid") userUid: String,

        )

    // 참여 확인 조회
    @Headers("Content-Type: application/json")
    @GET(API.GET_PARTICIPATION_CHECK)
    suspend fun getParticipationCheck(
        @Path("reservationId") reservationId: Int,
        @Query("userUid") userUid: String,
    ): ParticipationCheckResponse
}