package com.velectico.rbm.products.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.navigation.Navigation

import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.databinding.FragmentOrderFilterBinding
import com.velectico.rbm.databinding.FragmentProductFilterBinding
import com.velectico.rbm.order.views.OrderFilterDirections

/**
 * A simple [Fragment] subclass.
 */
class ProductFilterFragment : BaseFragment()  {
    private lateinit var binding: FragmentProductFilterBinding

    override fun getLayout(): Int {
        return R.layout.fragment_product_filter
    }


    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentProductFilterBinding

        binding.btnOrderHistory.setOnClickListener {

            val navDirection =  ProductFilterFragmentDirections.actionProductFilterFragmentToCreateOrderFragment()
            Navigation.findNavController(binding.btnOrderHistory).navigate(navDirection)
        }




    }

}
