package com.example.yogiflow.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Result(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name_eng")
    val name_eng: String,
    @SerializedName("alias")
    val alias: String,
    @SerializedName("name_san")
    val name_san: String,
    @SerializedName("benefits")
    val benefits: String,
    @SerializedName("pose_level")
    val pose_level: String,
    @SerializedName("pose_type")
    val pose_type: String,
    @SerializedName("img")
    val img: String
): Parcelable