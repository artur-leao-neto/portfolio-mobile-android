package com.example.cartaovisitaartur.data.remote

import retrofit2.http.GET
import retrofit2.http.Path

interface GithubApiService {
    @GET("users/{user}/repos")
    suspend fun listRepos(@Path("user") user: String): List<GithubRepo>
}
