package com.example.yogiflow.models;

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize;

@Parcelize
data class Login(
    @SerializedName("userName")
    val userName: String,
    @SerializedName("password")
    val password: String
): Parcelable
