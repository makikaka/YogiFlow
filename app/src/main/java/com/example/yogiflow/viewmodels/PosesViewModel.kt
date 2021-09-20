package com.example.yogiflow.viewmodels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.yogiflow.data.DataStoreRepository
import com.example.yogiflow.data.MealAndDietType
import com.example.yogiflow.util.Constants.Companion.API_KEY
import com.example.yogiflow.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.example.yogiflow.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.example.yogiflow.util.Constants.Companion.DEFAULT_RECIPES_NUMBER
import com.example.yogiflow.util.Constants.Companion.QUERY_ADD_RECIPE_INFORMATION
import com.example.yogiflow.util.Constants.Companion.QUERY_API_KEY
import com.example.yogiflow.util.Constants.Companion.QUERY_DIET
import com.example.yogiflow.util.Constants.Companion.QUERY_FILL_INGREDIENTS
import com.example.yogiflow.util.Constants.Companion.QUERY_NUMBER
import com.example.yogiflow.util.Constants.Companion.QUERY_SEARCH
import com.example.yogiflow.util.Constants.Companion.QUERY_TYPE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PosesViewModel @Inject constructor(
    application: Application,
    private val dataStoreRepository: DataStoreRepository
) : AndroidViewModel(application) {

    private lateinit var mealAndDiet: MealAndDietType

    var networkStatus = false
    var backOnline = false

    val readBackOnline = dataStoreRepository.readBackOnline.asLiveData()

    fun saveMealAndDietType() =
        viewModelScope.launch(Dispatchers.IO) {
            if (this@PosesViewModel::mealAndDiet.isInitialized) {
                dataStoreRepository.saveMealAndDietType(
                    mealAndDiet.selectedMealType,
                    mealAndDiet.selectedMealTypeId,
                    mealAndDiet.selectedDietType,
                    mealAndDiet.selectedDietTypeId
                )
            }
        }

    private fun saveBackOnline(backOnline: Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveBackOnline(backOnline)
        }


    fun showNetworkStatus() {
        if (!networkStatus) {
            Toast.makeText(getApplication(), "No Internet Connection.", Toast.LENGTH_SHORT).show()
            saveBackOnline(true)
        } else if (networkStatus) {
            if (backOnline) {
                Toast.makeText(getApplication(), "We're back online.", Toast.LENGTH_SHORT).show()
                saveBackOnline(false)
            }
        }
    }

}