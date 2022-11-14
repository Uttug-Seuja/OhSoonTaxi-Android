package com.example.myapplication.common.utils

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.myapplication.data.Promise
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class DataStoreModule(private val context : Context) {

    private val Context.dataStore  by preferencesDataStore(name = "dataStore")


    companion object {
        val TITLE = stringPreferencesKey("TITLE")
        val START_PLACE = stringPreferencesKey("START_PLACE")
        val DESTINATION = stringPreferencesKey("DESTINATION")
        val RECRUIT = stringPreferencesKey("RECRUIT")
        val GENDER = stringPreferencesKey("GENDER")
        val DATE = stringPreferencesKey("DATE")
        val RESERVATION_TIME = stringPreferencesKey("RESERVATION_TIME")

    }

    suspend fun setTitle(title: String) {
        context.dataStore.edit {
            it[TITLE] = title
        }
    }
    suspend fun setStartPlace(startPlace: String) {
        context.dataStore.edit {
            it[START_PLACE] = startPlace

        }
    }
    suspend fun setDestination(destination: String) {
        context.dataStore.edit {
            it[DESTINATION] = destination

        }
    }

    suspend fun setRecruit(recruit: String) {
        context.dataStore.edit {
            it[RECRUIT] = recruit

        }
    }

    suspend fun setGender(gender: String) {
        context.dataStore.edit {
            it[GENDER] = gender

        }
    }

    suspend fun setDate(date: String) {
        context.dataStore.edit {
            it[DATE] = date

        }
    }

    suspend fun setReservationTime(reservationTime: String) {
        context.dataStore.edit {
            it[RESERVATION_TIME] = reservationTime

        }
    }



    val promise :  Flow<Promise> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map {

            Promise(
                it[TITLE] ?: "",
                it[START_PLACE] ?: "",
                it[DESTINATION] ?: "",
                it[RECRUIT] ?: "",
                it[GENDER] ?: "모집 성별",
                it[DATE] ?: "탑승 날짜",
                it[RESERVATION_TIME] ?: "탑승 시간",
            )
        }


}