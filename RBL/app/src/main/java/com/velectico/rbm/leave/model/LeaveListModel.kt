package com.velectico.rbm.leave.model

import com.google.gson.annotations.SerializedName
import com.velectico.rbm.base.model.BaseModel

/**
 * Created by mymacbookpro on 2020-05-09
 * TODO: Add a class header comment!
 */
data class LeaveListModel(
    val leaveID: String,
    val leaveReasonId: String,
    @SerializedName("leaveReasonName")
    val leaveName: String? = "",
    @SerializedName("leaveFrom")
    val leaveFrom: String? = "",
    @SerializedName("leaveTo")
    val leaveTo: String? = "",
    @SerializedName("LD_SUM_UM_ID")
    val leaveDays: Int? = null,
    val LD_Other_Reason: String,
    val leaveAppliedOn: String,
    val leaveModifiedOn: String

    /*
    *  val leaveStatus: String? = "",
    *  val approverComments:String? = null,
    * */

): BaseModel()
