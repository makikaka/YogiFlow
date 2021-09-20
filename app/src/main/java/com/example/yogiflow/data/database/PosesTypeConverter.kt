package com.example.yogiflow.data.database

import androidx.room.TypeConverter
import com.example.yogiflow.models.Poses
import com.example.yogiflow.models.Result
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PosesTypeConverter {

    var gson = Gson()

    @TypeConverter
    fun foodPoseToString(poses: Poses): String {
        return gson.toJson(poses)
    }

    @TypeConverter
    fun stringToFoodPose(data: String): Poses {
        val listType = object : TypeToken<Poses>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun resultToString(result: Result): String {
        return gson.toJson(result)
    }

    @TypeConverter
    fun stringToResult(data: String): Result {
        val listType = object : TypeToken<Result>() {}.type
        return gson.fromJson(data, listType)
    }

}