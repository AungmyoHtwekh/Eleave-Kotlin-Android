package com.yoma.yomahackathon.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LeaveAssociate(
    @Expose
    @SerializedName("employee_id")
    val employeeId: String,
    @Expose
    @SerializedName("employee_name")
    val employeeName: String,
    @Expose
    @SerializedName("designation")
    val designation: String,
    @Expose
    @SerializedName("leave_type_code")
    val tyeCode: String,
    @Expose
    @SerializedName("leave_start_date")
    val startDate: String,
    @Expose
    @SerializedName("leave_end_date")
    val endDate: String,
    @Expose
    @SerializedName("half_type")
    val halfType: String,
    @Expose
    @SerializedName("duration")
    val duration: String,
    @Expose
    @SerializedName("remark")
    val remark: String,
    @Expose
    @SerializedName("status")
    val status: String,
    @Expose
    @SerializedName("status_description")
    val description: String
)