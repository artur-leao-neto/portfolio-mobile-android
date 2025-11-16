package com.example.cartaovisitaartur.data.remote

import com.squareup.moshi.Json

data class GithubRepo(
    val id: Int,
    val name: String,
    val description: String?
)

