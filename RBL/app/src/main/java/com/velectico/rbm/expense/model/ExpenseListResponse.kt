package com.velectico.rbm.expense.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ExpenseListResponse(
    val count: Int,
    val expenseDetails: List<Expense>,
    @SerializedName("status")
    val status: Int,
    @SerializedName("message")
    val message: String?
)

data class BidListResponse(
    val count: Int,
    @SerializedName("Details")
    val expenseDetails: List<Details>,
    @SerializedName("status")
    val status: Int,
    @SerializedName("message")
    val message: String?
)
data class Details (
    val BSD_Dealer_ID: String?,
    val BSD_Distrib_ID: String?,
    val taskName: String?
):Serializable


data class ExpenseResponse(
    @SerializedName("expenseDetails")
    val expenseDetails: List<ExpenseDetails>?,
    @SerializedName("status")
    val status: Int,
    @SerializedName("message")
    val message: String?
):Serializable
data class ExpenseDetails(
    val expenseId:String?,
val beatTaskId:String?,
val beatName:String?,
val userId:String?,
val userName:String?,
val expenseStatus:String?,
val approvedBy:String?,
val approvedByName:String?,
val appliedOnDate:String?,
val ER_Approve_Date:String?,
val recPhoto1 :String?,
val recPhoto2:String?,
val recPhoto3:String?,
val recPhoto4:String?,
val recPhoto5:String?,
val recPhoto6:String?,
    val details:List<EetailsA>?
):Serializable

data class EetailsA(
    val ERD_ID:String?,
    val expAmt:String?,
    val expDate:String?,
    val km_run:String?,
    val expType:String?

):Serializable