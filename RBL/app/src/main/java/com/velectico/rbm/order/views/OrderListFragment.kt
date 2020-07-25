package com.velectico.rbm.order.views

import android.util.Log
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.navigation.Navigation
import com.velectico.rbm.R
import com.velectico.rbm.RBMLubricantsApplication
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.adapters.BeatDateListAdapter
import com.velectico.rbm.beats.views.BeatListFragmentDirections
import com.velectico.rbm.beats.views.DateWiseBeatListFragmentDirections
import com.velectico.rbm.databinding.FragmentOrderListBinding
import com.velectico.rbm.databinding.RowBeatListDatesBinding
import com.velectico.rbm.databinding.RowOrderHeadListBinding
import com.velectico.rbm.order.adapters.OrderDetailsAdapter
import com.velectico.rbm.order.adapters.OrderHeadListAdapter
import com.velectico.rbm.order.model.OrderHead
import com.velectico.rbm.utils.SALES_LEAD_ROLE


class OrderListFragment : BaseFragment()  {
    private lateinit var binding: FragmentOrderListBinding
    private lateinit var orderHeadList : List<OrderHead>
    private lateinit var adapter: OrderHeadListAdapter

    override fun getLayout(): Int {
        return R.layout.fragment_order_list
    }


    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentOrderListBinding
        orderHeadList = OrderHead().getDummyOrderList()
        setUpRecyclerView()
        if (RBMLubricantsApplication.fromBeat == "Beat" ){
            binding.tilDealer.visibility = View.GONE

        }
        if (RBMLubricantsApplication.globalRole == "Team" ) {
            binding.fab.visibility = View.GONE

        }
        binding.fab.setOnClickListener {
            moveToCreateOrder()

        }

        binding.fabFilter.setOnClickListener {
            moveToFilterOrder()
        }


    }

    private fun setUpRecyclerView() {
      //  adapter = OrderHeadListAdapter();
        adapter = OrderHeadListAdapter(object : OrderHeadListAdapter.IBeatDateListActionCallBack{
            override fun moveToOrderDetails(position: Int, beatTaskId: String?,binding: RowOrderHeadListBinding) {
                Log.e("test","onAddTask"+beatTaskId)

                val navDirection =  OrderListFragmentDirections.actionOrderListFragmentToOrderDetailsFragment2()
                Navigation.findNavController(binding.navigateToDetails).navigate(navDirection)


            }
        });










        binding.rvOrderList.adapter = adapter
        adapter.orderList = orderHeadList
    }

    private fun moveToCreateOrder(){
        val navDirection =  OrderListFragmentDirections.actionOrderListFragmentToProductFilterFragment()
        Navigation.findNavController(binding.fab).navigate(navDirection)
    }

    private fun moveToFilterOrder(){
        val navDirection =  OrderListFragmentDirections.actionOrderListFragmentToOrderFilter()
        Navigation.findNavController(binding.fabFilter).navigate(navDirection)
    }





}