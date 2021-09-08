package com.example.yogiflow.data.network

import com.example.yogiflow.models.FoodRecipe
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface YogaPosesApi {

    @GET("/poses")
    suspend fun getRecipes(): Response<FoodRecipe>

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