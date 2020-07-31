package com.velectico.rbm.order.views

import android.content.Context
import android.util.Log
import androidx.databinding.ViewDataBinding
import androidx.navigation.Navigation
import com.google.gson.annotations.SerializedName
import com.kaopiz.kprogresshud.KProgressHUD
import com.velectico.rbm.R
import com.velectico.rbm.base.model.BaseModel
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.model.*
import com.velectico.rbm.databinding.FragmentCreateOrderBinding
import com.velectico.rbm.databinding.FragmentOrderPreviewBinding
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.order.adapters.OrderCartListAdapter
import com.velectico.rbm.order.adapters.OrderPreviewListAdapter
import com.velectico.rbm.order.model.OrderCart
import com.velectico.rbm.order.views.CreateOrderFragment.Companion.orderItems
import com.velectico.rbm.order.views.CreateOrderFragment.Companion.seletedItems
import com.velectico.rbm.utils.GloblalDataRepository
import com.velectico.rbm.utils.SharedPreferenceUtils
import retrofit2.Callback


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
          //
            callCreateOrderList()
        }
        setUpRecyclerView()


    }

    private fun setUpRecyclerView() {
        adapter = OrderPreviewListAdapter(orderItems)
        binding.rvCartList.adapter = adapter
        val x = mutableListOf<CreateOrderListDetails>()
       for (i in seletedItems ){
           x.add(i)
       }
        adapter.orderCart = CreateOrderFragment.seletedItems.toList()
        adapter.notifyDataSetChanged()
    }



    private fun setUp(){
    }


    private fun moveToOrderList(){
        val navDirection =  OrderPreviewFragmentDirections.actionOrderPreviewFragmentToOrderListFragment("")
        Navigation.findNavController(binding.btnPlaceOrder).navigate(navDirection)
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
    fun callCreateOrderList(){

        showHud()
        var list = mutableListOf<OrderDetailsParams>()
        for (i in seletedItems ){
            val total  = (i.PM_Disc_Price!!.toDouble())*(orderItems[i.PM_ID!!]!!.toDouble())
            list.add(OrderDetailsParams(i.PM_ID!!,"1",orderItems[i.PM_ID!!]!!,i.PM_MRP!!,i.PM_Disc_Price!!,i.PM_Net_Price!!,i.PM_GST_Perc!!,total.toString()))

        }
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val  model  = CreateOrderPRParams (SharedPreferenceUtils.getLoggedInUserId(context as Context),GloblalDataRepository.getInstance().scheduleId,GloblalDataRepository.getInstance().taskId,GloblalDataRepository.getInstance().delalerId,"2020-07-31","cash",list.toList()!!)
        val responseCall = apiInterface.createOrder(
            model
        )
        responseCall.enqueue(CreateOrderDetailsResponse as Callback<CreateOrderResponse>)
    }

    private val CreateOrderDetailsResponse = object : NetworkCallBack<CreateOrderResponse>() {
        override fun onSuccessNetwork(
            data: Any?,
            response: NetworkResponse<CreateOrderResponse>
        ) {
            hide()
            response.data?.status?.let { status ->
                Log.e("test333","OrderHistoryDetailsResponse status="+response.data)
                moveToOrderList()

            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            // hide()
        }


    }

}