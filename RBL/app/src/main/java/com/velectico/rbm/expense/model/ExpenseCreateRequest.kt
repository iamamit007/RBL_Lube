package com.velectico.rbm.expense.model

import com.velectico.rbm.base.model.BaseModel
import okhttp3.MultipartBody
import java.io.File

data class ExpenseCreateRequest(
    val userId: String?,
    val beatTaskId: Int,
    val Exp_Head_Id: Int,
    val misExpenseAmt: Int,
    val appliedOnDate : String?,
    val applieedByUserId : String?,
    val recPhoto : File?
): BaseModel()