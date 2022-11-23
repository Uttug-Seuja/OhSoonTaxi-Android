package com.example.myapplication.common.base

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.myapplication.network.RetrofitObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class DataStoreModule(private val context: Context) {

    private val Context.dataStore by preferencesDataStore(name = "dataStore")


    companion object {
        val USER_UID = stringPreferencesKey("userUid")
        val AUTHORIZATION = stringPreferencesKey("authorization")
        val REFRESHTOKEN = stringPreferencesKey("refreshToken")

    }

    suspend fun setUserUid(userUid: String) {
        context.dataStore.edit {
            it[USER_UID] = userUid
        }
    }

    suspend fun setAuthorization(authorization: String) {
        context.dataStore.edit {
            it[AUTHORIZATION] = authorization

        }
    }

    suspend fun setRefreshToken(refreshToken: String) {
        context.dataStore.edit {
            it[REFRESHTOKEN] = refreshToken

        }
    }


    val userUid: Flow<String> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map {
            it[USER_UID] ?: ""
        }

    val authorization: Flow<String> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map {
            it[AUTHORIZATION] ?: ""
        }
    val refreshToken: Flow<String> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map {
            it[REFRESHTOKEN] ?: ""
        }
}