package com.example.yogiflow.viewmodels

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.*
import com.example.yogiflow.data.Repository
import com.example.yogiflow.data.database.entities.FavoritesEntity
import com.example.yogiflow.data.database.entities.PosesEntity
import com.example.yogiflow.models.AuthToken
import com.example.yogiflow.models.Poses
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

    val readPoses: LiveData<List<PosesEntity>> = repository.local.readPoses().asLiveData()
    val readFavoritePoses: LiveData<List<FavoritesEntity>> =
        repository.local.readFavoritePoses().asLiveData()

    private fun insertPoses(posesEntity: PosesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertPoses(posesEntity)
        }

    fun insertFavoritePose(favoritesEntity: FavoritesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertFavoritePoses(favoritesEntity)
        }

    fun deleteFavoritePose(favoritesEntity: FavoritesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteFavoritePose(favoritesEntity)
        }

    fun deleteAllFavoritePoses() =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteAllFavoritePoses()
        }

    /** RETROFIT */
    var posesResponse: MutableLiveData<NetworkResult<Poses>> = MutableLiveData()
    var loginResponse: MutableLiveData<NetworkResult<AuthToken>> = MutableLiveData()
    var registerResponse: MutableLiveData<NetworkResult<Boolean>> = MutableLiveData()

    fun getPoses() = viewModelScope.launch {
        getPosesSafeCall()
    }

    private suspend fun getPosesSafeCall() {
        posesResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val authToken = prefs.getString("token", "NO TOKEN")
                val response = repository.remote.getPoses(authToken!!)
                posesResponse.value = handleFoodPosesResponse(response)

                val foodPose = posesResponse.value!!.data
                if (foodPose != null) {
                    offlineCachePoses(foodPose)
                }
            } catch (e: Exception) {
                posesResponse.value = NetworkResult.Error("Poses not found.")
            }
        } else {
            posesResponse.value = NetworkResult.Error("No Internet Connection.")
        }
    }

    private fun offlineCachePoses(poses: Poses) {
        val posesEntity = PosesEntity(poses)
        insertPoses(posesEntity)
    }

    private fun handleFoodPosesResponse(response: Response<List<Result>>): NetworkResult<Poses> {

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
                val foodPoses = response.body()
                val foodPosesModel = Poses(foodPoses!!);
                return NetworkResult.Success(foodPosesModel)
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }

    private fun handleLoginResponse(response: Response<AuthToken>): NetworkResult<AuthToken> {
        if (response.isSuccessful) {
            val foodPoses = response.body()
            return NetworkResult.Success(foodPoses!!)
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