package com.example.mvvmdemo.core

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

object BusinessThreadExecutor {

    val mBusinessPoolExecutor : ExecutorService = Executors.newSingleThreadExecutor()
}