package com.velectico.rbm.leave.model

data class ApplyLeaveResponse(
    val leaveReqId: Int,
    val respMessage: String,
    val status: Int
)