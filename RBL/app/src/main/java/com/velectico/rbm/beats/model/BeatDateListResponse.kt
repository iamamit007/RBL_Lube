package com.velectico.rbm.beats.model

import com.google.gson.annotations.SerializedName
import com.velectico.rbm.base.model.BaseModel
import java.io.Serializable

data class BeatDateListResponse(
    val count: Int,
    @SerializedName("Schedule_dates")
    val scheduleDates: List<ScheduleDates>,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("status")
    val status: Int? = null
)


data class ScheduleDates(
    @SerializedName("beatMaster_Id")
    val beatMaster_Id: String? = null,
    @SerializedName("schedule_id")
    val schedule_id: String? = null,
    @SerializedName("schedule_startDate")
    val schedule_startDate: String? = null

)


data class GetBeatDateRequestParams(
    @field:SerializedName("userId")
    val userId: String?
): BaseModel()


data class GetBeatDeatilsRequestParams(
    @SerializedName("userId") var userId: String,
    @SerializedName("scheduleId") var scheduleId: String
): BaseModel()


data class BeatWiseTakListResponse(
    val count: Int?,
    @SerializedName("Schedule_Details")
    val details: List<TaskDetails>?,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("status")
    val status: Int? = null
):Serializable

data class TaskDetails(
    val beat_id: String? = null,
    val scheduleId: String? = null,
    val BM_Beat_Name: String? = null,

    val BM_Resp_Level: String? = null,
    val Area: String? = null,
    val BM_Area_List: String? = null,

    val endDate: String? = null,
    val AssigneeName: String? = null,
    val assigneeId: String? = null,
    val BS_Target_Qty: String? = null,

    val BS_Target_Amt: String? = null,
    val dealerId: String? = null,
    val distribId: String? = null,
    val Visit: String? = null

):Serializable


data class BeatTaskDetailsListResponse(
    val count: Int?,
    val Task_Details: List<BeatTaskDetails>?,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("status")
    val status: Int? = null
):Serializable

data class BeatTaskDetails(
    val taskId: String? = null,
    val dealerName: String? = null,
    val dealerAddress: String? = null,

    val distribName: String? = null,
    val distribAddress: String? = null,
    val BSD_Work_Assg_Comment: String? = null,

    val BSD_Targer_Qty: String? = null,
    val BSD_Targer_Amt: String? = null,
    val dealerId: String? = null,
    val distribId: String? = null
):Serializable
