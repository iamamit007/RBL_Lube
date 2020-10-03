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
import com.velectico.rbm.order.views.CreateOrderFragment.Companion.schemeItems
import com.velectico.rbm.order.views.CreateOrderFragment.Companion.seletedItems
import com.velectico.rbm.utils.GloblalDataRepository
import com.velectico.rbm.utils.SharedPreferenceUtils
import com.velectico.rbm.utils.productItemClickListener
import retrofit2.Callback


class OrderPreviewFragment : BaseFragment() {

    private lateinit var binding: FragmentOrderPreviewBinding
    private lateinit var orderCartList : List<OrderCart>
    private lateinit var adapter: OrderPreviewListAdapter
    var taskDetails = BeatTaskDetails()
    companion object{
        var orderItems:HashMap<String,String> = HashMap()
        var schemeItems:HashMap<String,String?> = HashMap()
        var seletedItems = HashSet<CreateOrderListDetails>()
        var sceheId = ""
    }
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
        caculateGross()
    }



    private fun setUp(){
    }


    private fun moveToOrderList(){
        val navDirection =  OrderPreviewFragmentDirections.actionOrderPreviewFragmentToOrderListFragment(taskDetails)
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
    var grossAmt = 0.0
    var grossGST = 0.0
    fun caculateGross(){
        for (i in seletedItems ){
            val total  = (i.PM_Disc_Price!!.toDouble())*(orderItems[i.PM_ID!!]!!.toDouble())
            val totalGST  = (i.PM_GST_Perc!!.toDouble())*(orderItems[i.PM_ID!!]!!.toDouble())
            grossAmt += total
            grossGST += totalGST

        }
        binding.tvProdIdTotal.setText("$grossAmt")
        binding.tvProdIdGst.setText("$grossGST")
    }
    fun callCreateOrderList(){

        showHud()
        var list = mutableListOf<OrderDetailsParams>()
        var total = 0.0
        var totalltr = 0.0
        for (i in seletedItems ){
            var total  = (i.PM_Disc_Price!!.toDouble())*(orderItems[i.PM_ID!!]!!.toDouble())
            if (CreateOrderFragment.qtyType == "Bucket" && i.PM_Pcs_OR_Bucket == "bucket"){
                total = (i.PM_Net_Price!!.toDouble())*(CreateOrderFragment.orderItems[i.PM_ID!!]!!.toDouble())
                totalltr = (i.PM_UOM_Detail!!.toDouble())* (CreateOrderFragment.orderItems[i.PM_ID!!]!!.toDouble())
            }
            else if (CreateOrderFragment.qtyType == "Pieces" && i.PM_Pcs_OR_Bucket == "pcs"){
                total = ((i.PM_MRP!!.toDouble())/(i.PM_Quantity_Val!!.toDouble())*(orderItems[i.PM_ID!!]!!.toDouble()))
                totalltr = ((i.PM_Quantity_Val!!.toDouble())*(CreateOrderFragment.orderItems[i.PM_ID!!]!!.toDouble()))
            }
            else if (CreateOrderFragment.qtyType == "Carton"){
                total = (i.PM_Carton_Price!!.toDouble())*(CreateOrderFragment.orderItems[i.PM_ID!!]!!.toDouble())
                totalltr = (i.PM_UOM_Detail!!.toDouble())* (CreateOrderFragment.orderItems[i.PM_ID!!]!!.toDouble()*(i.PM_Unit_For_Carton!!.toDouble()))
            }
            grossAmt += total
            list.add(OrderDetailsParams(i.PM_ID!!,
                sceheId,orderItems[i.PM_ID!!]!!,i.PM_MRP!!,i.PM_Disc_Price!!,i.PM_Net_Price!!,i.PM_GST_Perc!!,
                total.toString(),i.PM_Pcs_OR_Bucket.toString(),totalltr.toString()))

        }
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val  model  = CreateOrderPRParams (SharedPreferenceUtils.getLoggedInUserId(context as Context),GloblalDataRepository.getInstance().scheduleId,GloblalDataRepository.getInstance().taskId,GloblalDataRepository.getInstance().delalerId,"2020-07-31","cash",list.toList()!!)
        val responseCall = apiInterface.createOrder(
            model
        )
        responseCall.enqueue(CreateOrderDetailsResponse as Callback<CreateOrderResponse>)
        binding.tvProdIdTotal.text = "₹ "+grossAmt
    }


    private val CreateOrderDetailsResponse = object : NetworkCallBack<CreateOrderResponse>() {
        override fun onSuccessNetwork(
            data: Any?,
            response: NetworkResponse<CreateOrderResponse>
        ) {
            hide()
            response.data?.status?.let { status ->
                seletedItems.clear()
                orderItems.clear()
                schemeItems.clear()
                Log.e("test333","OrderHistoryDetailsResponse status="+response.data)
                moveToOrderList()

            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            // hide()
        }


    }


}