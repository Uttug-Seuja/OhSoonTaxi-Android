package com.example.myapplication.data

import com.google.gson.annotations.SerializedName

data class ReservesSportDate(@SerializedName("data") val reservesSportDateData: ArrayList<ReservesSportDateData>)

data class ReservesSportDateData(

    @SerializedName("sport") val sport: String,
    @SerializedName("reserveDate") val reserveDate: String,
    @SerializedName("startT") val startT: String,
    @SerializedName("endT") val endT: String,
    @SerializedName("title") val title: String,
    @SerializedName("explanation") val explanation: String,
    @SerializedName("currentNum") val currentNum: Int,
    @SerializedName("recruitmentNum") val recruitmentNum: Int,
    @SerializedName("gender") val gender: String,
    @SerializedName("reserveId") val reserveId: Int,
    @SerializedName("reserveStatus") val reserveStatus: String,
    @SerializedName("userId") val userId: Int,





    )


