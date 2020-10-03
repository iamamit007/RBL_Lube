package com.velectico.rbm.payment.view

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.ViewDataBinding
import androidx.navigation.Navigation
import com.google.android.material.textfield.TextInputEditText
import com.velectico.rbm.payment.viewmodel.FragmentPaymentListViewModel
import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseActivity
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.model.*
import com.velectico.rbm.beats.views.BeatListFragmentDirections
import com.velectico.rbm.beats.views.DateWiseBeatListFragmentDirections
import com.velectico.rbm.databinding.*
import com.velectico.rbm.leave.model.LeaveReason
import com.velectico.rbm.leave.view.ApplyLeaveFragmentArgs
import com.velectico.rbm.leave.view.LeaveListFragment
import com.velectico.rbm.menuitems.viewmodel.MenuViewModel
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.payment.adapter.BeatPaymentListAdapter
import com.velectico.rbm.payment.adapter.OutstandingPaymentListAdapter
import com.velectico.rbm.payment.adapter.PaymentListAdapter
import com.velectico.rbm.payment.models.PaymentInfo
import com.velectico.rbm.reminder.adapter.ReminderListAdapter
import com.velectico.rbm.reminder.model.ReminderList
import com.velectico.rbm.utils.*
import retrofit2.Callback
import java.util.ArrayList


class FragmentPaymentList :  BaseFragment()  {
    private lateinit var binding: FragmentPaymentListFragmentBinding;
    private var beatList : List<PaymentDetails> = emptyList()
    private lateinit var adapter: OutstandingPaymentListAdapter
    private var currentDatePicketParentView : TextInputEditText? = null
    private lateinit var menuViewModel: MenuViewModel
    var userId = ""
    var dealerId = "0"
    var distribId = "0"
    override fun getLayout(): Int {
        return R.layout.fragment_payment_list_fragment
    }

    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentPaymentListFragmentBinding
        menuViewModel = MenuViewModel.getInstance(activity as BaseActivity)
      if(menuViewModel.loginResponse.value?.userDetails?.get(0)?.uMRole.toString() == DEALER_ROLE ||
          menuViewModel.loginResponse.value?.userDetails?.get(0)?.uMRole.toString() == DISTRIBUTER_ROLE) {
          binding.dealerListspinner.visibility = View.GONE
      }
        userId = SharedPreferenceUtils.getLoggedInUserId(context as Context)
        val languages = resources.getStringArray(R.array.array_dealDist)

        // access the spinner

        if (binding.dealerListspinner != null) {
            val adapter = context?.let {
                ArrayAdapter(
                    it,
                    android.R.layout.simple_spinner_dropdown_item, languages)
            }
            binding.dealerListspinner.adapter = adapter

            binding.dealerListspinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {
                    //showToastMessage(languages[position])

                    if (languages[position] == "Dealer"){

                        callDealApi()
                    }
                    else{

                        callDistApi()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }
        /*when (TEMP_CURRENT_LOGGED_IN) {
            DEALER_ROLE ->{
                binding.dealerListspinner.visibility = View.GONE
            }
        }*/
        //beatList = PaymentInfo().getDummyBeatList()
        callApiList()
        setUpRecyclerView()
        /*binding.fab.setOnClickListener {
            moveToCreateBeat()
        }*/
        binding.paymenthistory1.setOnClickListener{
            //showToastMessage("popup")
            val navDirection =  FragmentPaymentListDirections.actionFragmentPaymentListToPaymentHistoryList(dealerId,distribId)
            Navigation.findNavController(binding.paymenthistory1).navigate(navDirection)
        }
        binding.paybtn1.setOnClickListener{
            //showToastMessage("pay")
            val navDirection =  FragmentPaymentListDirections.actionFragmentPaymentListToFragmentAddPaymentInfo(dealerId,distribId)
            Navigation.findNavController(binding.paybtn1).navigate(navDirection)

        }

    }


    private fun setUpRecyclerView() {

        adapter = OutstandingPaymentListAdapter();
        binding.rvPaymentList.adapter = adapter
        adapter.beatList = beatList
    }


    fun callApiList(){

        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getOutstandingInvoice(

            outstandingInvoiceRequestParams(SharedPreferenceUtils.getLoggedInUserId(context as Context),dealerId,distribId)
        )
        responseCall.enqueue(outstandingInvoiceListResponse as Callback<outstandingInvoiceListResponse>)
    }

    private val outstandingInvoiceListResponse = object : NetworkCallBack<outstandingInvoiceListResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<outstandingInvoiceListResponse>) {

            response.data?.status?.let { status ->
                Log.e("test0000","OrderHistoryDetailsResponse status="+response.data)

                beatList.toMutableList().clear()
                if (response.data.count > 0){
                    beatList = response.data.paymentList!!.toMutableList()
                    binding.totalamt.text = "Total due amount ₹ "+response.data.totalOutStanding.toString()
                    setUpRecyclerView()

                }
                else{
                    showToastMessage("No data found")
                }

            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            // hide()
        }

    }
    private  var distNameList : List<DistDropdownDetails> = emptyList<DistDropdownDetails>()
    private val distNameResponse = object : NetworkCallBack<DistListResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<DistListResponse>) {
            response.data?.status?.let { status ->
                //showToastMessage(response.data.DistList.toString())

                distNameList  = response.data.DistList
                var statList: MutableList<String> = ArrayList()
                for (i in distNameList){
                    statList.add(i.UM_Name!!)
                    distribId = i.UM_ID!!
                }
                val adapter2 = context?.let {
                    ArrayAdapter(
                        it,
                        android.R.layout.simple_spinner_dropdown_item, statList)
                }

                if (binding.dealerListspinner.selectedItem == "Distributor"){
                    binding.dealerListspinner2.adapter = adapter2
                }

            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {

        }

    }

    fun callDistApi(){
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.distDropDownList(DistListRequestParams(userId)
        )

        responseCall.enqueue(distNameResponse as Callback<DistListResponse>)

    }

    private  var dealNameList : List<DealDropdownDetails> = emptyList<DealDropdownDetails>()
    private val dealNameResponse = object : NetworkCallBack<DealListResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<DealListResponse>) {
            response.data?.status?.let { status ->
                //showToastMessage(response.data.DealList.toString())

                dealNameList  = response.data.DealList
                var statList: MutableList<String> = ArrayList()
                for (i in dealNameList){
                    statList.add(i.UM_Name!!)
                    dealerId = i.UM_ID!!
                }
                val adapter2 = context?.let {
                    ArrayAdapter(
                        it,
                        android.R.layout.simple_spinner_dropdown_item, statList)
                }
                if (binding.dealerListspinner.selectedItem == "Dealer"){
                    binding.dealerListspinner2.adapter = adapter2
                }



            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {

        }

    }

    fun callDealApi(){
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.dealDropDownList(
            DealListRequestParams(userId)
        )

        responseCall.enqueue(dealNameResponse as Callback<DealListResponse>)

    }

}
