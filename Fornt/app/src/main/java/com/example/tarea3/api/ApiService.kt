package com.example.tarea3.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

data class User(val username: String, val password: String)
data class ApiResponse(val message: String)

interface ApiService {
    @GET("/")
    suspend fun checkStatus(): ApiResponse

    @POST("/register")
    suspend fun register(@Body user: User): ApiResponse

    @POST("/login")
    suspend fun login(@Body user: User): ApiResponse
}
