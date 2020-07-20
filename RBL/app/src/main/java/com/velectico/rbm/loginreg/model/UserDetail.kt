package com.velectico.rbm.loginreg.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.velectico.rbm.base.model.BaseModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserDetail(
    @SerializedName("Create_Date")
    val createDate: String?,
    @SerializedName("Created_By")
    val createdBy: String?,
    @SerializedName("DM_Dealer")
    val dMDealer: String?,
    @SerializedName("MM_Address")
    val mMAddress: String?,
    @SerializedName("MM_Bank_AC_No")
    val mMBankACNo: String?,
    @SerializedName("MM_Bank_Br_Name")
    val mMBankBrName: String?,
    @SerializedName("MM_Bank_IFSC_code")
    val mMBankIFSCCode: String?,
    @SerializedName("MM_Bank_Name")
    val mMBankName: String?,
    @SerializedName("MM_DOB")
    val mMDOB: String?,
    @SerializedName("MM_GPay_No")
    val mMGPayNo: String?,
    @SerializedName("MM_ID")
    val mMID: String?,
    @SerializedName("MM_Paypal_No")
    val mMPaypalNo: String?,
    @SerializedName("MM_Paytm_No")
    val mMPaytmNo: String?,
    @SerializedName("MM_Points")
    val mMPoints: String?,
    @SerializedName("MM_UM_ID")
    val mMUMID: String?,
    @SerializedName("Modified_By")
    val modifiedBy: String?,
    @SerializedName("Modified_Date")
    val modifiedDate: String?,
    @SerializedName("UM_Active_Inactive")
    val uMActiveInactive: String?,
    @SerializedName("UM_Email")
    val uMEmail: String?,
    @SerializedName("UM_ID")
    val uMID: String?,
    @SerializedName("UM_Is_Password_New")
    val uMIsPasswordNew: String?,
    @SerializedName("UM_Login_Id")
    val uMLoginId: String?,
    @SerializedName("UM_Login_Status")
    val uMLoginStatus: String?,
    @SerializedName("UM_Name")
    val uMName: String?,
    @SerializedName("UM_Password")
    val uMPassword: String?,
    @SerializedName("UM_Phone")
    val uMPhone: String?,
    @SerializedName("UM_Role")
    val uMRole: String?
): Parcelable, BaseModel()