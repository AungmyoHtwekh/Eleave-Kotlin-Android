package com.yoma.yomahackathon.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PendingLeaveRequest(
    @Expose
    @SerializedName("managerEmployeeId")
    val managerid: String,
    @Expose
    @SerializedName("leaveStatus")
    val status: String,
    @Expose
    @SerializedName("startDate")
    val startDate: String,
    @Expose
    @SerializedName("endDate")
    val endDate: String,
    @Expose
    @SerializedName("employeeId")
    val emplyeddId: String
)