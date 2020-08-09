package com.velectico.rbm.menuitems.viewmodel

import com.google.gson.annotations.SerializedName
import com.velectico.rbm.base.model.BaseModel
import java.io.Serializable

data class  AttendanceRequestParams(
    @SerializedName("userId") var userId: String
): BaseModel()

data class AttendancResponse(
    val respMessage:String,
    val AM_ID:String,
    val status: Int? = null

)