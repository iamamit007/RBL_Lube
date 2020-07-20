package com.velectico.rbm.leave.model

import com.google.gson.annotations.SerializedName

data class LeaveListResponse(
    val count: Int,
    val leaveDetails: List<LeaveListModel>,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("status")
    val status: Int? = null
)