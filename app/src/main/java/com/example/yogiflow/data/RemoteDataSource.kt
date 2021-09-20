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

    suspend fun getPoses(authToken: String): Response<List<Result>> {
        val token = AuthToken(authToken)
        return yogaPosesApi.getPoses(token)
    }

    suspend fun login(username: String, password: String): Response<AuthToken> {
        val loginRequest = Login(username, password)
        val loginRequestMap : Map<String, Any> = mapOf(
            "userName" to username,
            "password" to password
        )

        return yogaPosesApi.login(loginRequest)
    }

    suspend fun register(username: String, password: String): Response<Register> {
        // Don't use fullname just create random UID to ignore it
        val registerRequest = Register(username, UUID.randomUUID().toString(), password)
        return yogaPosesApi.register(registerRequest)
    }

//    suspend fun searchPoses(searchQuery: Map<String, String>): Response<FoodPose> {
//        return yogaPosesApi.searchPoses(searchQuery)
//    }
//
//    suspend fun getFoodJoke(apiKey: String): Response<FoodJoke> {
//        return yogaPosesApi.getFoodJoke(apiKey)
//    }

}