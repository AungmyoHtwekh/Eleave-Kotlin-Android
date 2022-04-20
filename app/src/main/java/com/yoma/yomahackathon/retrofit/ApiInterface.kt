package com.yoma.yomahackathon.retrofit

import com.yoma.yomahackathon.model.LoginData
import com.yoma.yomahackathon.model.LoginToken
import com.yoma.yomahackathon.model.PendingLeave
import com.yoma.yomahackathon.model.PendingLeaveRequest
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    @POST("auth/login")
    fun authenticate(@Body login: LoginData): Call<LoginToken>

    @POST("eleave-presentation-service/v1/eleave/associateLeave")
    fun getPendingLeaveList(@Body pendingLeave: PendingLeaveRequest,@Header("Authorization") auth: String): Call<PendingLeave>

}