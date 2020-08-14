package com.velectico.rbm.products.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.ViewDataBinding
import androidx.navigation.Navigation
import com.kaopiz.kprogresshud.KProgressHUD

import com.velectico.rbm.R
import com.velectico.rbm.RBMLubricantsApplication
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.model.DropdownDetails
import com.velectico.rbm.beats.model.OrderVSQualityRequestParams
import com.velectico.rbm.beats.model.OrderVSQualityResponse
import com.velectico.rbm.databinding.FragmentOrderFilterBinding
import com.velectico.rbm.databinding.FragmentProductFilterBinding
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.order.views.OrderFilterDirections
import kotlinx.android.synthetic.main.fragment_beat_report.view.*
import retrofit2.Callback
import java.util.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class ProductFilterFragment : BaseFragment()  {
    private lateinit var binding: FragmentProductFilterBinding
    var segId= ""
    var catId= ""
    override fun getLayout(): Int {
        return R.layout.fragment_product_filter
    }
    var hud: KProgressHUD? = null
    fun  showHud(){
        if (hud!=null){

            hud!!.show()
        }
    }

    fun hide(){
        hud?.dismiss()

    }
    var dataList : List<DropdownDetails> = emptyList<DropdownDetails>()
    val orderVSQualityResponseResponse = object : NetworkCallBack<OrderVSQualityResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<OrderVSQualityResponse>) {
            response.data?.status?.let { status ->

                hide()
                dataList  = response.data.BeatReportList
                var statList: MutableList<String> = ArrayList()
                for (i in dataList){
                    statList.add(i.Exp_Head_Name!!)
                }
                val adapter2 = context?.let {
                    ArrayAdapter(
                        it,
                        android.R.layout.simple_spinner_item, statList)
                }
                binding.spinnerCategory.adapter = adapter2


            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()


        }

    }


    var segdataList : List<DropdownDetails> = emptyList<DropdownDetails>()
    val segmentResponseResponse = object : NetworkCallBack<OrderVSQualityResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<OrderVSQualityResponse>) {
            response.data?.status?.let { status ->

                hide()
                segdataList  = response.data.BeatReportList
                var statList: MutableList<String> = ArrayList()
                for (i in segdataList){
                    statList.add(i.Exp_Head_Name!!)
                }
                val adapter2 = context?.let {
                    ArrayAdapter(
                        it,
                        android.R.layout.simple_spinner_item, statList)
                }
                binding.spinnerSegment.adapter = adapter2


            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()


        }

    }


    fun initHud(){
        hud =  KProgressHUD.create(activity)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel("Please wait")
            .setCancellable(true)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)
    }
    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentProductFilterBinding
        //showToastMessage(RBMLubricantsApplication.filterFrom)
        initHud()
        binding.btnOrderHistory.setOnClickListener {
            if (RBMLubricantsApplication.filterFrom == "Product"){
                val navDirection =  ProductFilterFragmentDirections.actionProductFilterFragmentToProductList(catId, segId)
                Navigation.findNavController(binding.btnOrderHistory).navigate(navDirection)
            }
            else {
                RBMLubricantsApplication.fromProductList = ""
                val navDirection =
                    ProductFilterFragmentDirections.actionProductFilterFragmentToCreateOrderFragment(
                        catId,
                        segId
                    )
                Navigation.findNavController(binding.btnOrderHistory).navigate(navDirection)
            }
        }
        binding.btnClr.setOnClickListener {
            callApi("Product Segment")
            callApi("Product Category")
        }
         callApi("Product Segment")
         callApi("Product Category")

        binding.spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (dataList.size > 0 ){
                    val x = dataList[position]
                    catId = x.Exp_Head_Id!!

                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }
        binding.spinnerSegment.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (segdataList.size > 0 ){
                    val x = segdataList[position]
                    segId = x.Exp_Head_Id!!

                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }
    }



    fun callApi(type:String){
        // DealerDetailsRequestParams(
        //            SharedPreferenceUtils.getLoggedInUserId(context as Context),"109","61","0")
        showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getOrdervsQualityList(
            OrderVSQualityRequestParams(type))
        when(type){
            "Product Segment"->{
                responseCall.enqueue(orderVSQualityResponseResponse as Callback<OrderVSQualityResponse>)
            }
            "Product Category"->{
                responseCall.enqueue(segmentResponseResponse as Callback<OrderVSQualityResponse>)
            }

        }
    }
}
