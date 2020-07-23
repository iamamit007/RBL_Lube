package com.velectico.rbm.order.views

import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.navigation.Navigation
import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.databinding.FragmentCreateOrderBinding
import com.velectico.rbm.order.adapters.OrderCartListAdapter
import com.velectico.rbm.order.adapters.OrderHeadListAdapter
import com.velectico.rbm.order.model.OrderCart
import com.velectico.rbm.order.model.OrderHead
import com.velectico.rbm.utils.ImageUtils


class CreateOrderFragment : BaseFragment() {
    private lateinit var binding : FragmentCreateOrderBinding
    private lateinit var orderCartList : List<OrderCart>
    private lateinit var adapter: OrderCartListAdapter
    private var locationManager : LocationManager? = null

    override fun getLayout(): Int {
        return R.layout.fragment_create_order
    }


    override fun init(binding: ViewDataBinding) {

        this.binding = binding as FragmentCreateOrderBinding
        orderCartList = OrderCart().getDummyOrderCart()
        setUpRecyclerView()
        binding.btnCheckOut.setOnClickListener {
            moveToOrderPreview()
        }

        binding.fabFilter.setOnClickListener {
            moveToProdFilter()
        }


    }

    private fun setUpRecyclerView() {
        adapter = OrderCartListAdapter();
        binding.rvCartList.adapter = adapter
        adapter.orderCart = orderCartList
        setUp()
    }


    private fun setUp(){
        checkPermission()

    }

    private fun checkPermission(){
        val checkSelfPermission = ContextCompat.checkSelfPermission(baseActivity, android.Manifest.permission.ACCESS_FINE_LOCATION)
        if (checkSelfPermission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(baseActivity, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }
    }

    private fun moveToOrderPreview(){
        val navDirection =  CreateOrderFragmentDirections.actionCreateOrderFragmentToOrderPreviewFragment()
        Navigation.findNavController(binding.fab).navigate(navDirection)
    }

    private fun moveToProdFilter(){
        val navDirection =  CreateOrderFragmentDirections.actionCreateOrderFragmentToProductFilterFragment()
        Navigation.findNavController(binding.fabFilter).navigate(navDirection)

    }



}