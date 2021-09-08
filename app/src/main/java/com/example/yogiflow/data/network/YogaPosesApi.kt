package com.example.yogiflow.data.network

import com.example.yogiflow.models.AuthToken
import com.example.yogiflow.models.FoodRecipe
import com.example.yogiflow.models.Result
import retrofit2.Response
import retrofit2.http.*

interface YogaPosesApi {

    @GET("/api/poses")
    suspend fun getRecipes(@Header("Authorization") authToken: AuthToken): Response<List<Result>>

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