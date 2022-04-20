package com.yoma.yomahackathon.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Status(
    @Expose
    @SerializedName("message")
    val message: String,
    @Expose
    @SerializedName("code")
    val code: Int
) {
}