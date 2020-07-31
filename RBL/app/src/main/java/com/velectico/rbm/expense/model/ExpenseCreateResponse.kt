package com.velectico.rbm.expense.model

import com.google.gson.annotations.SerializedName

data class ExpenseCreateResponse(
    val expenseId: Int,
    val expenseStatus: String,
    val image: String,
    val respMessage: String,
    val status: Int
)

data class ComplaintCreateResponse(
    val complainId: Int,
    val respMessage: String,
    val status: Int
)