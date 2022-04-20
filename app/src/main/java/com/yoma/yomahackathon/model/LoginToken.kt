package com.yoma.yomahackathon.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginToken(

    @Expose
    @SerializedName("access_token")
    val accessToken: String,
    @Expose
    @SerializedName("token_type")
    val tokenType: String,
    @Expose
    @SerializedName("expires_in")
    val expiresIn: Int
){
    constructor() : this(accessToken = "", tokenType = "", expiresIn = 1)
}
