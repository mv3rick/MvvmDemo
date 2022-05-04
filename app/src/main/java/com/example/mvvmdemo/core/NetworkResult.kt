package com.example.mvvmdemo.core

sealed class NetworkResult<T>(
    val data: T? = null,
    val message: String? = null,
    val paginationIndex : Int? =0
){
    class Success<T>(data: T, pageIndex: Int?) : NetworkResult<T>(data, paginationIndex = pageIndex)

    class Error<T>(message: String, data: T? = null) : NetworkResult<T>(data, message)

    class Loading<T> : NetworkResult<T>()

    override fun toString(): String {
        return "NetworkResult(message=$message, paginationIndex=$paginationIndex)"
    }


}
