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

