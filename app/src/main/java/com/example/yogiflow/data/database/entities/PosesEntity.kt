package com.example.yogiflow.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.yogiflow.models.Poses
import com.example.yogiflow.util.Constants.Companion.RECIPES_TABLE

@Entity(tableName = RECIPES_TABLE)
class PosesEntity(
    var poses: Poses
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}