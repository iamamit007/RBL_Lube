package com.velectico.rbm.profile.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.google.gson.Gson

import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.databinding.FragmentUserProfileBinding
import com.velectico.rbm.loginreg.model.LoginResponse
import com.velectico.rbm.loginreg.model.UserDetail
import com.velectico.rbm.menuitems.viewmodel.MenuViewModel

/**
 * Fragment to display user information
 */
class UserProfileFragment : BaseFragment() {
    private  lateinit var menuViewModel: MenuViewModel
    private  lateinit var binding: FragmentUserProfileBinding

    override fun getLayout(): Int {
        return R.layout.fragment_user_profile
    }

    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentUserProfileBinding
        menuViewModel = MenuViewModel.getInstance(baseActivity)
        binding.userdetail = getUserData()
    }

    fun getUserData(): UserDetail? {
        val json = """{"status":1,"userDetails":[{"UM_ID":"29","UM_Name":"mech123","UM_Phone":"7894568574","UM_Email":"","UM_Login_Id":"7890123456","UM_Password":"44e5c9469bb6ec789f441206bd8b374253aed419","UM_Is_Password_New":"Y","UM_Login_Status":"O","UM_Active_Inactive":"A","UM_Role":"M","Create_Date":"2020-05-01 12:11:41","Created_By":"sam","Modified_Date":"0000-00-00 00:00:00","Modified_By":"","MM_ID":"7","MM_UM_ID":"29","DM_Dealer":"19","MM_Address":"test","MM_DOB":"2020-05-23","MM_Bank_Name":"test","MM_Bank_Br_Name":"test","MM_Bank_IFSC_code":"test","MM_Bank_AC_No":"45454","MM_Paytm_No":"77777755555554","MM_GPay_No":"33333","MM_Paypal_No":"444441","MM_Points":"0"}],"userDashboardDetails":{"noOfDaysPresent":"25","noOfDaysAbsent":"5","targetShortfall":"15000","paymentShortfall":"2000","incentiveAchived":"1500"}}"""
        val gson = Gson()
        val loginResponse: LoginResponse = gson.fromJson(json, LoginResponse::class.java)
        return loginResponse.userDetails?.get(0)
        //return menuViewModel.loginResponse.value?.userDetails?.get(0)
    }

}
