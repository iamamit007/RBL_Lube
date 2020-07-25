package com.velectico.rbm.products.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding

import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseActivity
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.databinding.FragmentProductDetailsListBinding
import com.velectico.rbm.databinding.FragmentProductListBinding
import com.velectico.rbm.menuitems.viewmodel.MenuViewModel
import com.velectico.rbm.utils.MECHANIC_ROLE
import kotlinx.android.synthetic.main.fragment_product_details_list.*

/**
 * A simple [Fragment] subclass.
 */
class ProductDetailsListFragment : BaseFragment() {

    private lateinit var binding: FragmentProductDetailsListBinding

    private lateinit var menuViewModel: MenuViewModel
    override fun getLayout(): Int {
        return R.layout.fragment_product_details_list
    }

    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentProductDetailsListBinding
        menuViewModel = MenuViewModel.getInstance(activity as BaseActivity)

        if(menuViewModel.loginResponse.value?.userDetails?.get(0)?.uMRole.toString() == MECHANIC_ROLE){
           binding.schemeLayout.visibility = View.GONE
            binding.tvGstNo.visibility = View.GONE
            binding.tvGstNo.visibility = View.GONE
            binding.tvMrp.visibility = View.GONE
            binding.tvDiscPrice.visibility = View.GONE
            binding.tvSplPrice.visibility = View.GONE
       }
        binding.descLayout.setOnClickListener {
            cardDescription.visibility = View.VISIBLE;
            featurecard.visibility = View.GONE;
            schemecard.visibility = View.GONE;

        }

        binding.featureLayout.setOnClickListener {
            cardDescription.visibility = View.GONE;
            featurecard.visibility = View.VISIBLE;
            schemecard.visibility = View.GONE;

        }

        binding.schemeLayout.setOnClickListener {
            cardDescription.visibility = View.GONE;
            featurecard.visibility = View.GONE;
            schemecard.visibility = View.VISIBLE;

        }




    }

}
