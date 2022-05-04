package com.example.mvvmdemo.modules.gitpulls.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvmdemo.core.BusinessThreadExecutor
import com.example.mvvmdemo.core.NetworkResult
import com.example.mvvmdemo.modules.gitpulls.entity.PullRequest
import com.example.mvvmdemo.modules.gitpulls.repo.GitHubRepo
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel() : ViewModel() {

    companion object {
        private const val TAG = "MainViewModel"
    }

    private val _response: MutableLiveData<NetworkResult<List<PullRequest>>> = MutableLiveData()
    val response: LiveData<NetworkResult<List<PullRequest>>> = _response

    fun fetchGitPullRequests(owner : String = "square", repo : String = "retrofit", state : String = "closed",
                             sort : String = "closed_at", direction : String = "desc",
                             per_page: Int = 30, pageNumber: Int) {
        viewModelScope.launch(BusinessThreadExecutor.mBusinessPoolExecutor.asCoroutineDispatcher()) {
            Log.d(TAG, "fetchGitPullRequests")
            val result = GitHubRepo.fetchGitPullRequests(owner, repo, state, sort, direction, per_page, pageNumber)
            _response.postValue(result)
        }
    }
}