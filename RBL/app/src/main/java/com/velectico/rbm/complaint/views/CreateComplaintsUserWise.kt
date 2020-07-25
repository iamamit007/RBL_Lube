package com.velectico.rbm.complaint.views

import android.location.LocationManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding

import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseActivity
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.databinding.FragmentCreateComplaintsBinding
import com.velectico.rbm.databinding.FragmentCreateComplaintsUserWiseBinding
import com.velectico.rbm.databinding.FragmentCreateOrderBinding
import com.velectico.rbm.menuitems.viewmodel.MenuViewModel
import com.velectico.rbm.order.adapters.OrderCartListAdapter
import com.velectico.rbm.order.model.OrderCart
import com.velectico.rbm.utils.DEALER_ROLE
import com.velectico.rbm.utils.DISTRIBUTER_ROLE
import com.velectico.rbm.utils.MECHANIC_ROLE
import com.velectico.rbm.utils.SALES_LEAD_ROLE

/**
 * A simple [Fragment] subclass.
 */
class CreateComplaintsUserWise : BaseFragment() {
    private lateinit var binding: FragmentCreateComplaintsUserWiseBinding
    private lateinit var menuViewModel: MenuViewModel
    override fun getLayout(): Int {
        return R.layout.fragment_create_complaints_user_wise
    }


    override fun init(binding: ViewDataBinding) {

        this.binding = binding as FragmentCreateComplaintsUserWiseBinding
        menuViewModel = MenuViewModel.getInstance(activity as BaseActivity)
        if(menuViewModel.loginResponse.value?.userDetails?.get(0)?.uMRole.toString() == DISTRIBUTER_ROLE ||
            menuViewModel.loginResponse.value?.userDetails?.get(0)?.uMRole.toString() == MECHANIC_ROLE ||
            menuViewModel.loginResponse.value?.userDetails?.get(0)?.uMRole.toString() == DEALER_ROLE){

            binding.spinner11.visibility = View.GONE
            binding.dealerList.visibility = View.GONE
        }


    }
}