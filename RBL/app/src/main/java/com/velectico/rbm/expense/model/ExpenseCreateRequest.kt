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