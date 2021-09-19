package com.example.yogiflow.viewmodels

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast
import androidx.lifecycle.*
import com.example.yogiflow.data.Repository
import com.example.yogiflow.data.database.entities.FavoritesEntity
import com.example.yogiflow.data.database.entities.RecipesEntity
import com.example.yogiflow.models.AuthToken
import com.example.yogiflow.models.FoodRecipe
import com.example.yogiflow.models.Register
import com.example.yogiflow.models.Result
import com.example.yogiflow.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    val prefs: SharedPreferences = application.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)

    /** ROOM DATABASE */

    val readRecipes: LiveData<List<RecipesEntity>> = repository.local.readRecipes().asLiveData()
    val readFavoriteRecipes: LiveData<List<FavoritesEntity>> =
        repository.local.readFavoriteRecipes().asLiveData()

    private fun insertRecipes(recipesEntity: RecipesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertRecipes(recipesEntity)
        }

    fun insertFavoriteRecipe(favoritesEntity: FavoritesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertFavoriteRecipes(favoritesEntity)
        }

    fun deleteFavoriteRecipe(favoritesEntity: FavoritesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteFavoriteRecipe(favoritesEntity)
        }

    fun deleteAllFavoriteRecipes() =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteAllFavoriteRecipes()
        }

    /** RETROFIT */
    var recipesResponse: MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()
    var loginResponse: MutableLiveData<NetworkResult<AuthToken>> = MutableLiveData()
    var registerResponse: MutableLiveData<NetworkResult<Boolean>> = MutableLiveData()
    var searchedRecipesResponse: MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()

    fun getRecipes() = viewModelScope.launch {
        getRecipesSafeCall()
    }

    fun searchRecipes(searchQuery: Map<String, String>) = viewModelScope.launch {
        searchRecipesSafeCall(searchQuery)
    }

    private suspend fun getRecipesSafeCall() {
        recipesResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val authToken = prefs.getString("token", "NO TOKEN")
                val response = repository.remote.getRecipes(authToken!!)
                recipesResponse.value = handleFoodRecipesResponse(response)

                val foodRecipe = recipesResponse.value!!.data
                if (foodRecipe != null) {
                    offlineCacheRecipes(foodRecipe)
                }
            } catch (e: Exception) {
                recipesResponse.value = NetworkResult.Error("Poses not found.")
            }
        } else {
            recipesResponse.value = NetworkResult.Error("No Internet Connection.")
        }
    }

    private suspend fun searchRecipesSafeCall(searchQuery: Map<String, String>) {
        searchedRecipesResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
//                val response = repository.remote.searchRecipes(searchQuery)
//                searchedRecipesResponse.value = handleFoodRecipesResponse(response)
            } catch (e: Exception) {
                searchedRecipesResponse.value = NetworkResult.Error("Poses not found.")
            }
        } else {
            searchedRecipesResponse.value = NetworkResult.Error("No Internet Connection.")
        }
    }

    private fun offlineCacheRecipes(foodRecipe: FoodRecipe) {
        val recipesEntity = RecipesEntity(foodRecipe)
        insertRecipes(recipesEntity)
    }

    private fun handleFoodRecipesResponse(response: Response<List<Result>>): NetworkResult<FoodRecipe> {

        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Timeout")
            }
            response.code() == 402 -> {
                return NetworkResult.Error("API Key Limited.")
            }
            response.body()!!.isNullOrEmpty() -> {
                return NetworkResult.Error("Poses not found.")
            }
            response.isSuccessful -> {
                val foodRecipes = response.body()
                val foodRecipesModel = FoodRecipe(foodRecipes!!);
                return NetworkResult.Success(foodRecipesModel)
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }

    private fun handleLoginResponse(response: Response<AuthToken>): NetworkResult<AuthToken> {
        if (response.isSuccessful) {
            val foodRecipes = response.body()
            return NetworkResult.Success(foodRecipes!!)
        }
        val jObjError = JSONObject(response.errorBody()!!.string())
        return NetworkResult.Error(jObjError.getJSONObject("error").getString("message"),)
    }

    private fun handleRegisterResponse(response: Response<Register>): NetworkResult<Boolean>? {
        if (response.isSuccessful) {
            return NetworkResult.Success(true)
        }
        val jObjError = JSONObject(response.errorBody()!!.string())
        return NetworkResult.Error(jObjError.getJSONObject("error").getString("message"),)
    }

    fun makeLoginRequest(username: String, password: String) {
        loginResponse.value = NetworkResult.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            if (hasInternetConnection()) {
                try {

                    val response = repository.remote.login(username, password)
                    loginResponse.postValue(handleLoginResponse(response))


                } catch (e: Exception) {
                    loginResponse.postValue(NetworkResult.Error("Token not found"))
                }
            } else {
                loginResponse.postValue(NetworkResult.Error("No Internet Connection."))
            }
        }
    }

    fun makeRegisterRequest(username: String, password: String) {
        registerResponse.value = NetworkResult.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            if (hasInternetConnection()) {
                try {
                    val response = repository.remote.register(username, password)
                    registerResponse.postValue(handleRegisterResponse(response))
                } catch (e: Exception) {
                    registerResponse.postValue(NetworkResult.Error(e.message))
                }
            } else {
                registerResponse.postValue(NetworkResult.Error("No Internet Connection."))
            }
        }
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

}