package com.example.myapplication.data


data class ReservesCreation(
    val title: String,
    val reserveDate: String,
    val reserveTime: String,
    val startingPlace: String,
    val destination: String,
    val sex: String,
    val passengerNum: Int,
    val challengeWord: String,
    val countersignWord: String,
    val startLatitude: Double,
    val startLongitude: Double,
    val finishLatitude: Double,
    val finishLongitude: Double
)