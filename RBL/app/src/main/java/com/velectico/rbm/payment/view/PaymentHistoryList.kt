package com.velectico.rbm.payment.view

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.navigation.Navigation
import com.google.android.material.textfield.TextInputEditText

import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseActivity
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.model.PaymentHistoryDetails
import com.velectico.rbm.beats.model.paymentHistoryListResponse
import com.velectico.rbm.beats.model.paymentHistoryRequestParams
import com.velectico.rbm.databinding.FragmentPaymentListFragmentBinding
import com.velectico.rbm.databinding.PaymentHistoryListFragmentBinding
import com.velectico.rbm.menuitems.viewmodel.MenuViewModel
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.payment.adapter.BeatPaymentListAdapter
import com.velectico.rbm.payment.models.PaymentInfo
import com.velectico.rbm.redeem.model.GetRedeemDetailsRequestParams
import com.velectico.rbm.redeem.model.GetRedeemDetailsResponse
import com.velectico.rbm.utils.DEALER_ROLE
import com.velectico.rbm.utils.SharedPreferenceUtils
import retrofit2.Callback


class PaymentHistoryList :  BaseFragment() {
    private lateinit var binding: PaymentHistoryListFragmentBinding;
    private var beatList: List<PaymentHistoryDetails> = emptyList()
    private lateinit var adapter: BeatPaymentListAdapter
    private var currentDatePicketParentView: TextInputEditText? = null
    private lateinit var menuViewModel: MenuViewModel
    var dealerId = "0"
    var distribId = "0"
    override fun getLayout(): Int {
        return R.layout.payment_history_list_fragment
    }

    override fun init(binding: ViewDataBinding) {
        this.binding = binding as PaymentHistoryListFragmentBinding
        menuViewModel = MenuViewModel.getInstance(activity as BaseActivity)
        dealerId = arguments?.getString(  "dealerid").toString()
        distribId = arguments?.getString(  "distribid").toString()
        callApiList()
        setUpRecyclerView()


    }


    private fun setUpRecyclerView() {

        adapter = BeatPaymentListAdapter();
        binding.rvPaymentList.adapter = adapter
        adapter.beatList = beatList
    }


    fun callApiList(){

        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getPaymentHistory(

            paymentHistoryRequestParams(SharedPreferenceUtils.getLoggedInUserId(context as Context),dealerId,distribId)
        )
        responseCall.enqueue(paymentHistoryListResponse as Callback<paymentHistoryListResponse>)
    }

    private val paymentHistoryListResponse = object : NetworkCallBack<paymentHistoryListResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<paymentHistoryListResponse>) {

            response.data?.status?.let { status ->
                Log.e("test0000","OrderHistoryDetailsResponse status="+response.data)

                beatList.toMutableList().clear()
                if (response.data.count > 0){
                    beatList = response.data.paymentHistoryDetails!!.toMutableList()

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

}
