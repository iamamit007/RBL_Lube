package com.velectico.rbm.order.views

import android.view.View
import androidx.databinding.ViewDataBinding
import com.velectico.rbm.R
import com.velectico.rbm.RBMLubricantsApplication
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.databinding.FragmentOrderDetailsBinding
import com.velectico.rbm.databinding.FragmentOrderPreviewBinding
import com.velectico.rbm.order.adapters.OrderDetailsAdapter
import com.velectico.rbm.order.adapters.OrderPreviewListAdapter
import com.velectico.rbm.order.model.OrderCart

class OrderDetailsFragment : BaseFragment() {


    private lateinit var binding: FragmentOrderDetailsBinding
    private lateinit var orderCartList : List<OrderCart>
    private lateinit var adapter: OrderDetailsAdapter

    override fun getLayout(): Int {
        return R.layout.fragment_order_details
    }

    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentOrderDetailsBinding
        if (RBMLubricantsApplication.globalRole == "Team" ) {
            binding.btnCancel.visibility = View.GONE

        }
        orderCartList = OrderCart().getDummyOrderCart()
        setUpRecyclerView()


    }

    private fun setUpRecyclerView() {
        adapter = OrderDetailsAdapter();
        binding.rvCartList.adapter = adapter
        adapter.orderCart = orderCartList
    }


    private fun setUp(){


    }
}