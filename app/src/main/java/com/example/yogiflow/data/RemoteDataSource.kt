package com.example.yogiflow.data

import com.example.yogiflow.data.network.YogaPosesApi
import com.example.yogiflow.models.*
import retrofit2.Response
import retrofit2.http.Body
import java.util.*
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val yogaPosesApi: YogaPosesApi
) {

    suspend fun getRecipes(): Response<List<Result>> {
        val token = AuthToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoyLCJpYXQiOjE2MzEwOTcwMjksInN1YiI6Im1ha2kifQ.x-DZliG_qhcKD8TMn5PfSOyNVUXOLnS3m6AgzetJkEI")
        return yogaPosesApi.getRecipes(token)
    }

    suspend fun login(username: String, password: String): Response<AuthToken> {
        val loginRequest = Login(username, password)
        val loginRequestMap : Map<String, Any> = mapOf(
            "userName" to username,
            "password" to password
        )

        return yogaPosesApi.login(loginRequest)
    }

    suspend fun register(username: String, password: String): Response<AuthToken> {
        val loginRequestMap : Map<String, Any> = mapOf(
            "userName" to username,
            "password" to password,
            "fullname" to UUID.randomUUID().toString()
        )
        return yogaPosesApi.register(loginRequestMap)
    }

//    suspend fun searchRecipes(searchQuery: Map<String, String>): Response<FoodRecipe> {
//        return yogaPosesApi.searchRecipes(searchQuery)
//    }
//
//    suspend fun getFoodJoke(apiKey: String): Response<FoodJoke> {
//        return yogaPosesApi.getFoodJoke(apiKey)
//    }

}