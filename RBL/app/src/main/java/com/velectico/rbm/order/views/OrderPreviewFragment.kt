package com.velectico.rbm.order.views

import androidx.databinding.ViewDataBinding
import androidx.navigation.Navigation
import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.databinding.FragmentCreateOrderBinding
import com.velectico.rbm.databinding.FragmentOrderPreviewBinding
import com.velectico.rbm.order.adapters.OrderCartListAdapter
import com.velectico.rbm.order.adapters.OrderPreviewListAdapter
import com.velectico.rbm.order.model.OrderCart


class OrderPreviewFragment : BaseFragment() {

    private lateinit var binding: FragmentOrderPreviewBinding
    private lateinit var orderCartList : List<OrderCart>
    private lateinit var adapter: OrderPreviewListAdapter

    override fun getLayout(): Int {
        return R.layout.fragment_order_preview
    }

    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentOrderPreviewBinding
        binding.btnPlaceOrder.setOnClickListener {
            moveToOrderList()
        }
        orderCartList = OrderCart().getDummyOrderCart()
        setUpRecyclerView()


    }

    private fun setUpRecyclerView() {
        adapter = OrderPreviewListAdapter();
        binding.rvCartList.adapter = adapter
        adapter.orderCart = orderCartList
    }


    private fun setUp(){


    }


    private fun moveToOrderList(){
        val navDirection =  OrderPreviewFragmentDirections.actionOrderPreviewFragmentToOrderListFragment("")
        Navigation.findNavController(binding.btnPlaceOrder).navigate(navDirection)
    }


}