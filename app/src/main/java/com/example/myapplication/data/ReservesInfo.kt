package com.example.myapplication.data

import com.google.gson.annotations.SerializedName

data class ReservesInfo(@SerializedName("data") val reservesInfoData: ReservesInfoData)

data class ReservesInfoData(
    @SerializedName("reserveId") val reserveId: Int,
    @SerializedName("sport") val sport: String,
    @SerializedName("reserveDate") val reserveDate: String,
    @SerializedName("startT") val startT: String,
    @SerializedName("endT") val endT: String,
    @SerializedName("title") val title: String,
    @SerializedName("explanation") val explanation: String,
    @SerializedName("currentNum") val currentNum: Int,
    @SerializedName("recruitmentNum") val recruitmentNum: Int,
    @SerializedName("place") val place: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("reserveStatus") val reserveStatus: String,
    @SerializedName("userId") val userId: Int,
    @SerializedName("schoolNum") val schoolNum: String,
    @SerializedName("name") val name: String,
    )
