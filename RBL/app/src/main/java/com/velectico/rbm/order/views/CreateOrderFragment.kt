package com.velectico.rbm.order.views

import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.navigation.Navigation
import com.kaopiz.kprogresshud.KProgressHUD
import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.model.*
import com.velectico.rbm.databinding.FragmentCreateOrderBinding
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.order.adapters.OrderCartListAdapter
import com.velectico.rbm.order.adapters.OrderHeadListAdapter
import com.velectico.rbm.order.model.OrderCart
import com.velectico.rbm.order.model.OrderHead
import com.velectico.rbm.utils.ImageUtils
import com.velectico.rbm.utils.SharedPreferenceUtils
import com.velectico.rbm.utils.productItemClickListener
import retrofit2.Callback


class CreateOrderFragment : BaseFragment(),productItemClickListener {
    private lateinit var binding : FragmentCreateOrderBinding
    private var orderCartList : List<CreateOrderListDetails> = emptyList()
    private lateinit var adapter: OrderCartListAdapter
    private var locationManager : LocationManager? = null

    var segId= ""
    var catId= ""
    private lateinit var listener: productItemClickListener
    override fun getLayout(): Int {
        return R.layout.fragment_create_order
    }
    companion object{
        var orderItems:HashMap<String,String> = HashMap()
        var schemeItems:HashMap<String,String?> = HashMap()
        var seletedItems = HashSet<CreateOrderListDetails>()
    }


    override fun init(binding: ViewDataBinding) {

        this.binding = binding as FragmentCreateOrderBinding
        //orderCartList = OrderCart().getDummyOrderCart()
        catId = arguments?.getString(  "catId").toString()
        segId = arguments?.getString(  "segId").toString()
        showToastMessage(catId)
        binding.btnCheckOut.setOnClickListener {
            moveToOrderPreview()
        }

        binding.fabFilter.setOnClickListener {
            moveToProdFilter()
        }
        callCreateOrderList()
        listener = this
        setUpRecyclerView()

    }

    private fun setUpRecyclerView() {
        adapter = OrderCartListAdapter(context!!,listener);
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
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getCreateOrderList(
            CreateOrderListRequestParams(SharedPreferenceUtils.getLoggedInUserId(context as Context),segId,catId)
        )
        responseCall.enqueue(CreateOrderDetailsResponse as Callback<CreateOrderDetailsResponse>)
    }

    private val CreateOrderDetailsResponse = object : NetworkCallBack<CreateOrderDetailsResponse>() {
        override fun onSuccessNetwork(
            data: Any?,
            response: NetworkResponse<CreateOrderDetailsResponse>
        ) {
            hide()
            response.data?.status?.let { status ->
                Log.e("test333","OrderHistoryDetailsResponse status="+response.data)
                orderCartList.toMutableList().clear()
                if (response.data.count > 0) {
                    orderCartList = response.data.CreateOrderList!!.toMutableList()
                    setUpRecyclerView()
                } else {
                    showToastMessage("No data found")

                }

            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            // hide()
        }


    }
    override fun onItemClick() {
        caculateGross()

    }


    var grossGST = 0.0
    fun caculateGross(){
        var grossAmt = 0.0
        if (seletedItems.size !=0){
            for (i in seletedItems ){
                val total  = (i.PM_Net_Price!!.toDouble())*(orderItems[i.PM_ID!!]!!.toDouble())
                grossAmt += total

            }
        }else{
            binding.tvProdId.setText("$grossAmt")

        }

        //binding.tvProdIdGst.setText("$grossGST")
    }

}