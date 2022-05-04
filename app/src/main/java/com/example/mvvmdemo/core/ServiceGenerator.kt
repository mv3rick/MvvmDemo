package com.example.mvvmdemo.core

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ServiceGenerator {

    companion object{

        private val sLogger =
            HttpLoggingInterceptor.Logger { message -> Log.d("Retrofit", message) }

        fun <S> createService(serviceClass: Class<S>, baseUrl: String?): S {
            val logging = HttpLoggingInterceptor(sLogger)
            logging.level = HttpLoggingInterceptor.Level.BODY

            val httpClient = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .callbackExecutor(BusinessThreadExecutor.mBusinessPoolExecutor)
                .client(httpClient)
                .build()
            return retrofit.create(serviceClass)
        }
    }
}