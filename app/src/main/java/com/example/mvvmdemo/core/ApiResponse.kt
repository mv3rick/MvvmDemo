package com.example.mvvmdemo.core

import android.util.Log
import retrofit2.Response

abstract class ApiResponse {
    suspend fun <T> networkCall(pageNumber : Int?, apiCall: suspend () -> Response<T>): NetworkResult<T> {
        try {
            Log.d("ApiResponse", "making network call")
            val response = apiCall()
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    return NetworkResult.Success(body, pageNumber)
                }
            }
            return NetworkResult.Error("Error ${response.code()} ${response.message()}")
        } catch (e: Exception) {
            return NetworkResult.Error("Error ${e.message}")
        }
    }

}