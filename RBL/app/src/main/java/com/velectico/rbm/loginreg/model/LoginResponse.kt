package com.velectico.rbm.loginreg.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.velectico.rbm.base.model.BaseModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LoginResponse(
    @SerializedName("status")
    val status: Int?,
    @SerializedName("userDashboardDetails")
    val userDashboardDetails: UserDashboardDetails?,
    @SerializedName("userDetails")
    val userDetails: List<UserDetail>?,
    @SerializedName("message")
    val message: String?


):Parcelable, BaseModel()

