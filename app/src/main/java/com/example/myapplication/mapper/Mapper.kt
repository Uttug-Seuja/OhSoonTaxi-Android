package com.example.myapplication.mapper

import com.example.myapplication.data.Login
import com.example.myapplication.data.Participations
import com.example.myapplication.data.ReservesResponse
import com.example.myapplication.data.ReservesResponseData
import com.google.gson.annotations.SerializedName

fun Login.toDomain(): Login {

    return Login(
        uid = this.uid,
        password = this.password
    )

}


fun ReservesResponseData.toDomain(): ReservesResponseData {

    return ReservesResponseData(
        id = this.id,
        title= this.title,
        reserveDate = this. reserveDate,
        reserveTime= this.reserveTime,
        startingPlace = this.startingPlace,
        destination = this.destination,
        sex = this.sex,
        createdAt = this.createdAt,
        currentNum = this.currentNum,
        passengerNum = this.passengerNum,
        challengeWord = this.challengeWord,
        countersignWord = this.countersignWord,
        startLatitude = this.startLatitude,
        startLongitude = this.startLongitude,
        finishLatitude = this.finishLatitude,
        finishLongitude = this.finishLongitude,
        reservationStatus = this.reservationStatus,
        userUid = this.userUid,
        name = this.name,
        schoolNum = this.schoolNum,
        participations = this.participations,
    )

}