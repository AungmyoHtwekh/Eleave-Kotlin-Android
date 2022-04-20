package com.yoma.yomahackathon.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*
import kotlin.collections.ArrayList

data class PendingLeave(
    @Expose
    @SerializedName("status")
    val status: Status,
    @Expose
    @SerializedName("leave_associate_list")
    val leaveAssociateList: List<LeaveAssociate>
)