package com.velectico.rbm.redeem.view

import android.content.Context
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import androidx.navigation.Navigation
import com.kaopiz.kprogresshud.KProgressHUD
import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.databinding.FragmentRedeemPointsBinding
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.payment.view.FragmentPaymentListDirections
import com.velectico.rbm.redeem.model.ReedemRequestParams
import com.velectico.rbm.redeem.model.ReedemResponse
import com.velectico.rbm.redeem.model.SendQrRequestParams
import com.velectico.rbm.redeem.model.SendQrResponse
import com.velectico.rbm.utils.SharedPreferenceUtils
import retrofit2.Callback

class RedeemPoints :  BaseFragment() {


    private lateinit var binding: FragmentRedeemPointsBinding
    override fun getLayout(): Int {
        return R.layout.fragment_redeem_points
    }

    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentRedeemPointsBinding
        //binding.totalPoints
        binding.buttonRedeemHistory.setOnClickListener{

            val navDirection =  RedeemPointsDirections.actionRedeemPointsToRedeem()
            Navigation.findNavController(binding.buttonRedeemHistory).navigate(navDirection)
        }
        binding.btnLogin.setOnClickListener{
            redeem()
        }
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


    fun redeem(){
        if (binding.inputBlncAmt.text.toString().isEmpty()){
            showToastMessage("Enter amount to redeem")
        }
        else {
            showHud()
            val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
            val responseCall = apiInterface.redeemPoints(
                ReedemRequestParams(
                    SharedPreferenceUtils.getLoggedInUserId(context as Context),binding.inputBlncAmt.text.toString()
                )
            )
            responseCall.enqueue(ReedemResponse as Callback<ReedemResponse>)
        }
    }

    private val ReedemResponse = object : NetworkCallBack<ReedemResponse>(){
        override fun onSuccessNetwork(
            data: Any?,
            response: NetworkResponse<ReedemResponse>
        ) {
            response.data?.respMessage?.let { status ->
                Toast.makeText(activity!!, response.data?.respMessage, Toast.LENGTH_SHORT)
                    .show()
                hide()

                activity!!.onBackPressed()

            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()
        }

    }

}