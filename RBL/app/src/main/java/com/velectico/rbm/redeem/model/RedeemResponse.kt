package com.velectico.rbm.redeem.model

import com.google.gson.annotations.SerializedName
import com.velectico.rbm.base.model.BaseModel
import java.io.Serializable

data class  SendQrRequestParams(
    @SerializedName("userId") var userId: String?,
    @SerializedName("QR_Code") var QR_Code: String?,
    @SerializedName("QR_Value") var QR_Value: String?,
    @SerializedName("QR_Points") var QR_Points: String?

): BaseModel()
data class SendQrResponse(
    val count: Int,
    val respMessage:String,
    val scanned:String,
    val QAD_ID:String


): Serializable

data class  ReedemRequestParams(
    @SerializedName("userId") var userId: String?,
    @SerializedName("QR_Points") var QR_Points: String?

): BaseModel()
data class ReedemResponse(
    val count: Int,
    val respMessage:String,
    val QRD_ID:String


): Serializable

data class  GetRedeemDetailsRequestParams(
    @SerializedName("userId") var userId: String,
    @SerializedName("startDate") var startDate: String,
    @SerializedName("endDate") var endDate: String
): BaseModel()


data class GetRedeemDetailsResponse(
    val count: Int,
    @SerializedName("Details")
    val RedeemListDetail: List<RedeemListDetails>,
    @SerializedName("status")
    val status: Int? = null,
    @SerializedName("TotalPoint")
    val TotalPoint:String? = null
)
data class RedeemListDetails(
    var QRD_ID: String? = null,
    var QRD_Req_Date: String? = null,
    var QRD_Payment_Date: String? = null,
    var QRD_Status: String? = null,
    var QRD_Point_Amt: String? = null

): Serializable