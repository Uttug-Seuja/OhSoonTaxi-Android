package com.example.myapplication.data

import com.google.gson.annotations.SerializedName

data class Hashtag(@SerializedName("data") val hashtagItem: HashtagItem)

data class HashtagItem(
    @SerializedName("name") val name: String,
    @SerializedName("id") val id: Int,
    @SerializedName("polls") val polls: ArrayList<PollsComponent>,


    )

data class PollsComponent(
    @SerializedName("id") val id: Int,
    @SerializedName("contents") val contents: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("endTime") val endTime: String,
    @SerializedName("presentImagePath") val presentImagePath: String?,

    )