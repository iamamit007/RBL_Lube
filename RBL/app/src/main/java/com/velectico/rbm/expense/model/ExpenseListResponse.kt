package com.velectico.rbm.expense.model

import com.google.gson.annotations.SerializedName

data class ExpenseListResponse(
    val count: Int,
    val expenseDetails: List<Expense>,
    @SerializedName("status")
    val status: Int,
    @SerializedName("message")
    val message: String?
)