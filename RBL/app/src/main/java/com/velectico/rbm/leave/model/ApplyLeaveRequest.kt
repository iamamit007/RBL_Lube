package com.velectico.rbm.leave.model

data class ApplyLeaveRequest(
    val leaveReasonId: Int,
    val userId: String,
    val leaveFromDate: String,
    val leaveToDate: String,
    val leaveReasonOther: String,
    val leaveId: String
)