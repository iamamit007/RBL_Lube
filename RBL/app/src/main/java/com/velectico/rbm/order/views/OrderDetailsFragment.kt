package com.velectico.rbm.order.views

import android.view.View
import androidx.databinding.ViewDataBinding
import com.velectico.rbm.R
import com.velectico.rbm.RBMLubricantsApplication
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.model.BeatTaskDetails
import com.velectico.rbm.beats.model.OrderListDetails
import com.velectico.rbm.beats.model.OrderProductListDetails
import com.velectico.rbm.databinding.FragmentOrderDetailsBinding
import com.velectico.rbm.databinding.FragmentOrderPreviewBinding
import com.velectico.rbm.order.adapters.OrderDetailsAdapter
import com.velectico.rbm.order.adapters.OrderPreviewListAdapter
import com.velectico.rbm.order.model.OrderCart

class OrderDetailsFragment : BaseFragment() {


    private lateinit var binding: FragmentOrderDetailsBinding
    private lateinit var orderCartList : List<OrderProductListDetails>
    private lateinit var adapter: OrderDetailsAdapter
    var taskDetails = BeatTaskDetails()
    var orderDetails = OrderListDetails()
    override fun getLayout(): Int {
        return R.layout.fragment_order_details
    }

    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentOrderDetailsBinding
        taskDetails = arguments!!.get("taskDetail") as BeatTaskDetails
        orderDetails = arguments!!.get("orderListDetail") as OrderListDetails
        binding.tvAddressTxt.text = taskDetails.dealerAddress
        binding.orederID.text = orderDetails.OH_Order_No
        binding.tvOrddate.text = orderDetails.orderDate
        binding.tvProdpriceTotal.text = "₹" +orderDetails.totalPrice
        binding.tvProdGst.text = "12%"

        if (RBMLubricantsApplication.globalRole == "Team" ) {
            binding.btnCancel.visibility = View.GONE

        }
        orderCartList = orderDetails.prod_details
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