package com.example.yogiflow.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.yogiflow.models.FoodRecipe
import com.example.yogiflow.util.Constants.Companion.RECIPES_TABLE

@Entity(tableName = RECIPES_TABLE)
class RecipesEntity(
    var foodRecipe: FoodRecipe
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}