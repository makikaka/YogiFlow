package com.example.yogiflow.data

import com.example.yogiflow.data.network.YogaPosesApi
import com.example.yogiflow.models.FoodJoke
import com.example.yogiflow.models.FoodRecipe
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val yogaPosesApi: YogaPosesApi
) {

    suspend fun getRecipes(queries: Map<String, String>): Response<FoodRecipe> {
        return yogaPosesApi.getRecipes(queries)
    }

    suspend fun searchRecipes(searchQuery: Map<String, String>): Response<FoodRecipe> {
        return yogaPosesApi.searchRecipes(searchQuery)
    }

    suspend fun getFoodJoke(apiKey: String): Response<FoodJoke> {
        return yogaPosesApi.getFoodJoke(apiKey)
    }

}