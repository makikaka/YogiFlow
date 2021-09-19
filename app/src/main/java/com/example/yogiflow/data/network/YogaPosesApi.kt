package com.example.yogiflow.data.network

import com.example.yogiflow.models.*
import retrofit2.Response
import retrofit2.http.*

interface YogaPosesApi {

    @GET("/api/poses")
    suspend fun getRecipes(@Header("Authorization") authToken: AuthToken): Response<List<Result>>

    @POST("/api/login")
    suspend fun login(@Body login: Login): Response<AuthToken>

    @POST("/api/register")
    suspend fun register(@Body register: Map<String, Any>): Response<AuthToken>

//    @GET("/recipes/complexSearch")
//    suspend fun searchRecipes(
//        @QueryMap searchQuery: Map<String, String>
//    ): Response<FoodRecipe>
//
//    @GET("food/jokes/random")
//    suspend fun getFoodJoke(
//        @Query("apiKey") apiKey: String
//    ): Response<FoodJoke>

}