package com.velectico.rbm.leave.model

import com.google.gson.annotations.SerializedName
import com.velectico.rbm.base.model.BaseModel

data class LeaveListRequest(
    @SerializedName("userId")
    val userId: String
):BaseModel()