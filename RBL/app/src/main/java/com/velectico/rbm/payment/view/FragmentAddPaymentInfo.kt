package com.velectico.rbm.payment.view

import android.content.Context
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.model.InvoicePaidRequestParams
import com.velectico.rbm.beats.model.InvoicePaidResponse
import com.velectico.rbm.databinding.FragmentAddPaymentInfoBinding
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.utils.SharedPreferenceUtils
import retrofit2.Callback


/**
 * A simple [Fragment] subclass.
 */
class FragmentAddPaymentInfo :  BaseFragment() {


    private lateinit var binding: FragmentAddPaymentInfoBinding
    var dealerId = "0"
    var distribId = "0"
    var pmtMode = ""
    override fun getLayout(): Int {
        return R.layout.fragment_add_payment_info
    }

    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentAddPaymentInfoBinding
        dealerId = arguments?.getString(  "dealid").toString()
        distribId = arguments?.getString(  "distribid").toString()
        val languages = resources.getStringArray(R.array.payment_mode_array)

        // access the spinner

        if (binding.spinnerPmtMode != null) {
            val adapter = context?.let {
                ArrayAdapter(
                    it,
                    android.R.layout.simple_spinner_dropdown_item, languages)
            }
            binding.spinnerPmtMode.adapter = adapter

            binding.spinnerPmtMode.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {
                   // showToastMessage(languages[position])
                    pmtMode = languages[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }
        binding.btnConfirmPmt.setOnClickListener {
           addPaymentApi()
        }

    }

    fun addPaymentApi(){

        if (pmtMode == "--Select Payment Mode--"){
            Toast.makeText(context, "Please select mode of payment", Toast.LENGTH_SHORT).show()
        }

        else if (binding.inputBlncAmt.text.toString()?.trim() == ""){
            Toast.makeText(context, "Please enter amount", Toast.LENGTH_SHORT).show()
        }
        else{
            val param = InvoicePaidRequestParams(
                SharedPreferenceUtils.getLoggedInUserId(context as Context),dealerId,distribId,binding.inputBlncAmt.text.toString(),
                pmtMode


            )

            val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
            val responseCall = apiInterface.getInvoicePaid(param)
            responseCall.enqueue(InvoicePaidResponse as Callback<InvoicePaidResponse>)

        }
    }

    private val InvoicePaidResponse = object : NetworkCallBack<InvoicePaidResponse>(){
        override fun onSuccessNetwork(
            data: Any?,
            response: NetworkResponse<InvoicePaidResponse>
        ) {
            response.data?.status?.let { status ->
                if (status == 1) {
                    Toast.makeText(context, response.data.respMessage, Toast.LENGTH_SHORT).show()
                    val navDirection =  FragmentAddPaymentInfoDirections.actionFragmentAddPaymentInfoToFragmentPaymentList()
                    Navigation.findNavController(binding.btnConfirmPmt).navigate(navDirection)
                    val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view!!.getWindowToken(), 0)
                }
                else{

                }
            }
        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {

        }

    }


}
