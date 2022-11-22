package com.example.myapplication.data

import com.google.gson.annotations.SerializedName

data class ReservesResponse(@SerializedName("data") val reservesResponseData: ReservesResponseData)

data class ReservesResponseData(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("reserveDate") val reserveDate: String,
    @SerializedName("reserveTime") val reserveTime: String,
    @SerializedName("startingPlace") val startingPlace: String,
    @SerializedName("destination") val destination: String,
    @SerializedName("sex") val sex: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("currentNum") val currentNum: Int,
    @SerializedName("passengerNum") val passengerNum: Int,
    @SerializedName("challengeWord") val challengeWord: String,
    @SerializedName("countersignWord") val countersignWord: String,
    @SerializedName("startLatitude") val startLatitude: Double,
    @SerializedName("startLongitude") val startLongitude: Double,
    @SerializedName("finishLatitude") val finishLatitude: Double,
    @SerializedName("finishLongitude") val finishLongitude: Double,
    @SerializedName("reservationStatus") val reservationStatus: String,
    @SerializedName("userUid") val userUid: String,
    @SerializedName("name") val name: String,
    @SerializedName("schoolNum") val schoolNum: String,
    @SerializedName("participations") val participations: MutableList<Participations>,

    )

data class Participations(

    @SerializedName("schoolNum") val schoolNum: String,
    @SerializedName("name") val name: String,
    @SerializedName("seatPosition") val seatPosition: Int,

    )
