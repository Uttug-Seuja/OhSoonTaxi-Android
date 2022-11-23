package com.example.myapplication.data

import com.google.gson.annotations.SerializedName



data class MyInfoResponse(@SerializedName("data") val myInfoResponseData: MyInfoResponseData)

data class MyInfoResponseData(
    @SerializedName("name") val name: String,
    @SerializedName("sex") val sex: String,
    @SerializedName("schoolNum") val schoolNum: String,

    )
