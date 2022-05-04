package com.example.mvvmdemo.modules.gitpulls.repo.rest

import com.example.mvvmdemo.modules.gitpulls.entity.PullRequest
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubService {

    @Headers("Accept: application/vnd.github.v3.text+json")
    @GET("/repos/{owner}/{repo}/pulls")
    suspend fun fetchGitPullRequests(@Path("owner", encoded = true) owner: String, @Path("repo", encoded = true) repoName:String,
                                 @Query("state") state: String, @Query("sort") sortBy: String,
                                 @Query("direction") sortDirection: String, @Query("per_page") per_page: Int?,
                                 @Query("page") page: Int?): Response<List<PullRequest>>
}