package com.yoma.yomahackathon.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginData(
    @Expose
    @SerializedName("username")
    var username: String,
    @Expose
    @SerializedName("password")
    var password: String
)