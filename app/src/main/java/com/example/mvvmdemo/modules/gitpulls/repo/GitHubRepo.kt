package com.example.mvvmdemo.modules.gitpulls.repo

import android.util.Log
import com.example.mvvmdemo.core.ApiResponse
import com.example.mvvmdemo.core.AppConfiguration
import com.example.mvvmdemo.core.NetworkResult
import com.example.mvvmdemo.core.ServiceGenerator
import com.example.mvvmdemo.modules.gitpulls.entity.PullRequest
import com.example.mvvmdemo.modules.gitpulls.repo.rest.GitHubService

object GitHubRepo : ApiResponse() {
    private const val TAG = "GitHubRepo"

    suspend fun fetchGitPullRequests(
        owner: String,
        repo: String,
        state: String,
        sort: String,
        direction: String,
        perPage: Int,
        pageNumber: Int
    ): NetworkResult<List<PullRequest>> {
        Log.d(TAG, "fetchGitPullRequests")
        val gitHubService = ServiceGenerator.createService(GitHubService::class.java, AppConfiguration.BASE_URL)
        return networkCall (pageNumber) {
            gitHubService.fetchGitPullRequests(
                owner,
                repo,
                state,
                sort,
                direction,
                perPage,
                pageNumber
            )
        }
    }

}