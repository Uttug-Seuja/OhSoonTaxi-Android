package com.example.myapplication.data

data class Match(
    val startTime: String,
    val endTime: String,
    val place: String,
    val gender: Int, // 0 : 남녀 모두, 1 : 남자, 2: 여자
    val people: String,
    val level: String,
    val state : Int, // 0 : 신청 가능, 1 : 마감 임박, 2 : 마감

)
