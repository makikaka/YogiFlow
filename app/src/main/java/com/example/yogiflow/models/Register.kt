package com.example.yogiflow.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize;

@Parcelize
data class Register(
    @SerializedName("username")
    val username: String,
    @SerializedName("fullname")
    val fullname: String,
    @SerializedName("password")
    val password: String
): Parcelable
