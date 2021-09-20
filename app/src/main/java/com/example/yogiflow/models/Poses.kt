package com.example.yogiflow.models

import com.google.gson.annotations.SerializedName

data class Poses(
    @SerializedName("results")
    val results: List<Result>
)