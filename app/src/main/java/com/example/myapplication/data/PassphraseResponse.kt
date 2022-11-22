package com.example.myapplication.data

import com.google.gson.annotations.SerializedName

data class PassphraseResponse(@SerializedName("data") val passphraseResponseData: PassphraseResponseData)

data class PassphraseResponseData(
    @SerializedName("challengeWord") val challengeWord: String,
    @SerializedName("countersignWord") val countersignWord: String
    )