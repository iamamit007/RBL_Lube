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
    @SerializedName("schedule_startDate")
    val schedule_startDate: String? = null


)


data class GetBeatDateRequestParams(
    @field:SerializedName("userId")
    val userId: String?
): BaseModel()


data class GetBeatDeatilsRequestParams(
    @SerializedName("userId") var userId: String,
    @SerializedName("scheduleDate") var scheduleId: String
): BaseModel()


data class BeatWiseTakListResponse(
    val count: Int?,
    @SerializedName("Schedule_List")
    val details: List<TaskDetails>?,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("status")
    val status: Int? = null,
    @SerializedName("orderAmt")
    val orderAmt: String? = null,
    @SerializedName("collectionAmt")
    val collectionAmt: String? = null,
    @SerializedName("visit")
    val visit: String? = null
):Serializable

data class TaskDetails(
    val beatMaster_Id: String? = null,
    val scheduleId: String? = null,
    val BM_Beat_Name: String? = null,
    val BM_Resp_Level: String? = null,
    val schedule_startDate: String? = null,
    val schedule_endDate: String? = null,
    val assigneeName: String? = null,
    val orderAmt: String? = null,
    val collectionAmt: String? = null,
    val visit: String? = null

):Serializable



data class  BeatTaskDetailsRequestParams(
    @SerializedName("userId") var userId: String,
    @SerializedName("scheduleId") var scheduleId: String
): BaseModel()

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



data class DealerDetailsResponse(
    val count: Int,
    @SerializedName("Details")
    val scheduleDates: List<DealerDetails>,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("status")
    val status: Int? = null,
    @SerializedName("actualOrderAmt")
    val actualOrderAmt: String? = null,
    @SerializedName("actualCollectionAmt")
    val actualCollectionAmt: String? = null

)

data class DealerDetails(
    val DM_Contact_Person: String?,
    val DM_Address: String?,
    val Grade: String?,
    val orderAmt: String?,
    val collectionAmt: String?,
    val lati: String?,
    val longi: String?
):Serializable

data class  DealerDetailsRequestParams(
    @SerializedName("userId") var userId: String?,
    @SerializedName("taskId") var taskId: String?,
    @SerializedName("dealerId") var dealerId: String?,
    @SerializedName("distribId") var distribId: String?
): BaseModel()

data class  BeatAllOrderListRequestParams(
    @SerializedName("userId") var userId: String,
    @SerializedName("dealerId") var dealerId: String
): BaseModel()

data class OrderHistoryDetailsResponse(
    val count: Int,
    @SerializedName("Details")
    val OrderList: List<OrderListDetails>,
    @SerializedName("status")
    val status: Int? = null

)

data class OrderListDetails(
    val OD_ID: String? = null,
    val OD_Order_No: String? = null,
    val OD_OH_ID: String? = null,
    val OD_Product_ID: String? = null,
    val OD_GST_Perc: String? = null,
    val OD_MRP: String? = null,
    val OD_Net_Price: String? = null,
    val OD_Total_Price: String? = null,
    val OD_Disc_Price: String? = null,
    val OD_Qty: String? = null,
    val OD_Scheme: String? = null,
    val Create_Date: String? = null,
    val Created_By: String? = null,
    val Modified_Date: String? = null,
    val Modified_By: String? = null,
    val OH_ID: String? = null,
    val OH_Order_No: String? = null,
    val OH_Sales_Rep_ID: String? = null,
    val OH_Dealer_ID: String? = null,
    val OH_Distrib_ID: String? = null,
    val OH_BSD_ID: String? = null,
    val OH_Ord_Date: String? = null,
    val OH_Status: String? = null,
    val OH_Payment_Mode: String? = null,
    val OH_Ord_Close_Date: String? = null,
    val OH_Ord_Cancel_Date: String? = null,
    val OH_Payment_Date: String? = null,
    val OH_Pay_Confirm_Status: String? = null

):Serializable

data class  CreateOrderListRequestParams(
    @SerializedName("userId") var userId: String,
    @SerializedName("catId") var catId: String,
    @SerializedName("segId") var segId: String
): BaseModel()

data class CreateOrderDetailsResponse(
    val count: Int,
    @SerializedName("Details")
    val CreateOrderList: List<CreateOrderListDetails>,
    @SerializedName("status")
    val status: Int? = null

)
data class CreateOrderListDetails(
    var PM_ID: String? = null,
    var PM_Type: String? = null,
    var PM_HSN: String? = null,
    var PM_Cat: String? = null,
    var PM_UOM: String? = null,
    var PM_Seg: String? = null,
    var PM_Brand: String? = null,
    var PM_Scheme: String? = null,
    var PM_GST_Perc: String? = null,
    var PM_SGST_Per: String? = null,
    var PM_CGST_Per: String? = null,
    var PM_MRP: String? = null,
    var PM_Net_Price: String? = null,
    var PM_Disc_Price: String? = null,
    var PM_Scheme_Photo: String? = null,
    var PM_Image_Path: String? = null,
    var PM_Feature: String? = null,
    var Create_Date: String? = null,
    var Created_By: String? = null,
    var Modified_Date: String? = null,
    var Modified_By: String? = null,
    var PM_Type_Name: String? = null,
    var PM_Cat_Name: String? = null,
    var PM_UOM_Detail: String? = null,
    var PM_Seg_Name: String? = null,
    var PM_Brand_name: String? = null,
    var PM_Scheme_name: String? = null,
    var PSM_Scheme_Details: List<PSM_Scheme_DetailsResponse>? = null
):Serializable

data class PSM_Scheme_DetailsResponse(
    var schemeId: String? = null,
    var schemeName: String? = null,
    var imagePath: String? = null
):Serializable

data class  BeatReportListRequestParams(
    @SerializedName("userId") var userId: String,
    @SerializedName("taskId") var taskId: String,
    @SerializedName("dealerId") var dealerId: String,
    @SerializedName("distribId") var distribId: String,
    @SerializedName("startDate") var startDate: String,
    @SerializedName("endDate") var endDate: String
): BaseModel()

data class BeatReportListDetailsResponse(
    val count: Int,
    @SerializedName("Details")
    val BeatReportList: List<BeatReportListDetails>,
    @SerializedName("status")
    val status: Int? = null

)
data class BeatReportListDetails(
    var reportDate: String? = null,
    var orderReasonId: String? = null,
    var orderReason: String? = null,
    var paymentReasonId: String? = null,
    var paymentReason: String? = null,
    var complainReasonId: String? = null,
    var complainReason: String? = null,
    var priceProblemId: String? = null,
    var priceProblem: String? = null,
    var packagingProblemId: String? = null,
    var packagingProblem: String? = null,
    var refOtherId: String? = null,
    var refOther: String? = null,
    var SR_Turnover_Range: String? = null,
    var SR_Follow_Up_Date: String? = null,
    var SR_Follow_Up_Reason: String? = null,
    var Create_Date: String? = null,
    var Created_By: String? = null

):Serializable

object


data class  OrderVSQualityRequestParams(
    @SerializedName("DM_Dropdown_Name") var userId: String?

): BaseModel()

data class OrderVSQualityResponse(
    val count: Int,
    @SerializedName("DropdownDetails")
    val BeatReportList: List<DropdownDetails>,
    @SerializedName("status")
    val status: Int? = null

)

data class DropdownDetails(
    val Exp_Head_Id: String?,
    val Exp_Head_Name: String?

):Serializable


data class  CreateBeatReportRequestParams(
    @SerializedName("userId") var userId: String?,
    @SerializedName("taskId") var taskId: String?,
    @SerializedName("dealerId") var dealerId: String?,
    @SerializedName("distribId") var distribId: String?,
    @SerializedName("orderStatus") var orderStatus: String?,
    @SerializedName("paymentStatus") var paymentStatus: String?,
    @SerializedName("complainStatus") var complainStatus: String?,
    @SerializedName("priceProblem") var priceProblem: String?,
    @SerializedName("packagProblem") var packagProblem: String?,
    @SerializedName("otherPref") var otherPref: String?,
    @SerializedName("turnoverRange") var turnoverRange: String?,
    @SerializedName("followupDate") var followupDate: String?,
    @SerializedName("followupReason") var followupReason: String?

): BaseModel()
data class CreateBeatReportResponse(
    val count: Int,
    val respMessage:String

):Serializable


data class  BeatDetailListRequestParams(
    @SerializedName("userId") var userId: String,
    @SerializedName("beatLevel") var beatLevel: String
): BaseModel()

data class BeatDetailListResponse(
    val count: Int,
    @SerializedName("BeatDetails")
    val BeatList: List<BeatListDetails>,
    @SerializedName("status")
    val status: Int? = null

)
data class BeatListDetails(
    var BM_ID: String? = null,
    var BM_Beat_Name: String? = null,
    var BM_DM_ID: String? = null,
    var BM_Area_List: String? = null,
    var BM_Resp_Level: String? = null,
    var Create_Date: String? = null,
    var Created_By: String? = null

):Serializable

data class  TaskForListRequestParams(
    @SerializedName("userId") var userId: String,
    @SerializedName("beatLevel") var beatLevel: String
): BaseModel()

data class TaskForListResponse(
    val count: Int,
    @SerializedName("taskFor")
    val TaskForList: List<TaskForList>,
    @SerializedName("status")
    val status: Int? = null,
    @SerializedName("source")
    val source: String? = null,
    @SerializedName("areaList")
    val areaList: String? = null


)
data class TaskForList(
    var taskLevel: String? = null,
    var respValue: String? = null

):Serializable

data class  LocationByLevelListRequestParams(
    @SerializedName("userId") var userId: String,
    @SerializedName("source") var source: String,
    @SerializedName("taskLevel") var taskLevel: String,
    @SerializedName("areaList") var areaList: String
): BaseModel()

data class LocationByLevelListResponse(
    val count: Int,
    @SerializedName("location")
    val LocationList: List<LocationList>,
    @SerializedName("status")
    val status: Int? = null

)
data class LocationList(
    var locId: String? = null,
    var locValue: String? = null

):Serializable

data class  AssignToListRequestParams(
    @SerializedName("userId") var userId: String,
    @SerializedName("source") var source: String,
    @SerializedName("taskLevel") var taskLevel: String,
    @SerializedName("locId") var locId: String
): BaseModel()

data class AssignToListResponse(
    val count: Int,
    @SerializedName("Details")
    val AssignToList: List<AssigntoList>,
    @SerializedName("status")
    val status: Int? = null

)
data class AssigntoList(
    var UM_ID: String? = null,
    var UM_Name: String? = null

):Serializable

data class  DealDistMechListRequestParams(
    @SerializedName("userId") var userId: String,
    @SerializedName("source") var source: String,
    @SerializedName("locId") var locId: String

): BaseModel()

data class DealDistMechListResponse(

    @SerializedName("")
    val DealDistMechList: List<DealDistMechList>


)
data class DealDistMechList(
    var UM_ID: String? = null,
    var UM_Name: String? = null,
    var UM_Role: String? = null,
    var DM_Area: String? = null,
    var AM_Area_Name: String? = null


):Serializable

data class  CreateBeatScheduleRequestParams(
    @SerializedName("userId") var userId: String,
    @SerializedName("beatMasterId") var beatMasterId: String,
    @SerializedName("startDate") var startDate: String,
    @SerializedName("endDate") var endDate: String

): BaseModel()

