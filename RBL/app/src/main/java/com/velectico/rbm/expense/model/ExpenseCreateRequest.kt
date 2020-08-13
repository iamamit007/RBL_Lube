package com.velectico.rbm.expense.model

import com.google.gson.annotations.SerializedName
import com.velectico.rbm.base.model.BaseModel
import okhttp3.MultipartBody
import java.io.File
import java.io.Serializable

data class ExpenseCreateRequest(
    @SerializedName("userId") var userId: String?,
    @SerializedName("taskId") val taskId: String?,
    @SerializedName("expName") val expName: String?,
    @SerializedName("ExpDetails") val expDetails:List<ExpDetailsRequest>?
): BaseModel()

data class ExpDetailsRequest(
    @SerializedName("expHeadId") var expHeadId: String?,
    @SerializedName("expAmt") val expAmt: String?,
    @SerializedName("km_run") val km_run: String?,
    @SerializedName("expDate") val expDate: String?
): BaseModel()

data class CreateExpenseResponse(
    val status: Int?,
    val respMessage: String?,
    val expensId: Int?
):Serializable

data class ComplaintCreateRequest(
    val userId: String?,
    val complaintype: String?,
    val CR_Distrib_ID: String?,
    val CR_Dealer_ID: String?,
    val CR_Mechanic_ID: String?,
    val CR_Qty : String?,
    val CR_Batch_no : String?,
    val CR_Remarks : String?,
    val recPhoto : File?,
    val taskId : String?,
    val prodName : String?
): BaseModel()