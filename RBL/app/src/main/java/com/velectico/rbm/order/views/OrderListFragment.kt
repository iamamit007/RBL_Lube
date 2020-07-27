package com.velectico.rbm.order.views

import android.content.Context
import android.util.Log
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.navigation.Navigation
import com.kaopiz.kprogresshud.KProgressHUD
import com.velectico.rbm.R
import com.velectico.rbm.RBMLubricantsApplication
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.adapters.BeatDateListAdapter
import com.velectico.rbm.beats.model.*
import com.velectico.rbm.beats.views.BeatListFragmentDirections
import com.velectico.rbm.beats.views.DateWiseBeatListFragmentDirections
import com.velectico.rbm.databinding.FragmentOrderListBinding
import com.velectico.rbm.databinding.RowBeatListDatesBinding
import com.velectico.rbm.databinding.RowOrderHeadListBinding
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.order.adapters.OrderDetailsAdapter
import com.velectico.rbm.order.adapters.OrderHeadListAdapter
import com.velectico.rbm.order.model.OrderHead
import com.velectico.rbm.utils.SALES_LEAD_ROLE
import com.velectico.rbm.utils.SharedPreferenceUtils
import retrofit2.Callback


class OrderListFragment : BaseFragment()  {
    private lateinit var binding: FragmentOrderListBinding
    private var orderHeadList : List<OrderListDetails> = emptyList()
    private lateinit var adapter: OrderHeadListAdapter

    override fun getLayout(): Int {
        return R.layout.fragment_order_list
    }


    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentOrderListBinding
        //orderHeadList = OrderHead().getDummyOrderList()

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
        setUpRecyclerView()
        callApiOrderList()

    }
    var hud: KProgressHUD? = null
    fun  showHud(){
        hud =  KProgressHUD.create(activity)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel("Please wait")
            .setCancellable(true)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)
            .show();
    }

    fun hide(){
        hud?.dismiss()
    }
    fun callApiOrderList(){
        showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getBeatAllOrderHistory(
            BeatAllOrderListRequestParams("7001507620","61")
        )
        responseCall.enqueue(OrderHistoryDetailsResponse as Callback<OrderHistoryDetailsResponse>)
    }

    private val OrderHistoryDetailsResponse = object : NetworkCallBack<OrderHistoryDetailsResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<OrderHistoryDetailsResponse>) {
            hide()
            response.data?.status?.let { status ->
                Log.e("test222","OrderHistoryDetailsResponse status="+response.data)
                orderHeadList.toMutableList().clear()
                if (response.data.OrderList!!.isEmpty()){
                    showToastMessage("No data found")
                }else{
                    orderHeadList = response.data.OrderList!!.toMutableList()
                    setUpRecyclerView()
                }

            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
           // hide()
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