package com.example.yogiflow.data

import com.example.yogiflow.data.network.YogaPosesApi
import com.example.yogiflow.models.AuthToken
import com.example.yogiflow.models.FoodRecipe
import com.example.yogiflow.models.Result
import retrofit2.Response
import retrofit2.http.Body
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val yogaPosesApi: YogaPosesApi
) {

    suspend fun getRecipes(): Response<List<Result>> {
        val token = AuthToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoyLCJpYXQiOjE2MzEwOTcwMjksInN1YiI6Im1ha2kifQ.x-DZliG_qhcKD8TMn5PfSOyNVUXOLnS3m6AgzetJkEI")
        return yogaPosesApi.getRecipes(token)
    }

//    suspend fun searchRecipes(searchQuery: Map<String, String>): Response<FoodRecipe> {
//        return yogaPosesApi.searchRecipes(searchQuery)
//    }
//
//    suspend fun getFoodJoke(apiKey: String): Response<FoodJoke> {
//        return yogaPosesApi.getFoodJoke(apiKey)
//    }

}