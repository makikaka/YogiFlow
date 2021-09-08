package com.example.yogiflow.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AuthToken(
    @SerializedName("authToken")
    val authToken: String,
): Parcelable