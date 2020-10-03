package com.velectico.rbm.qrcodescan.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.zxing.integration.android.IntentIntegrator
import com.kaopiz.kprogresshud.KProgressHUD
import com.velectico.rbm.R
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.redeem.model.SendQrRequestParams
import com.velectico.rbm.redeem.model.SendQrResponse
import com.velectico.rbm.utils.SharedPreferenceUtils
import kotlinx.android.synthetic.main.qrscanner_lay.view.*
import org.json.JSONObject
import retrofit2.Callback


class QRScanner :Fragment(){
    private lateinit var rootView: View
    private lateinit var qrScan: IntentIntegrator
    var qrCode = ""
    var qrVal = ""
    var qrPoint = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView =  inflater.inflate(R.layout.qrscanner_lay, container, false)
        qrScan = IntentIntegrator.forSupportFragment(this)
        qrScan.initiateScan()
//        val str = "QR_Code:0DLIPzteso3. QR-Value:14. QR_Points:20"
//
//        val separate2 = str.split(".", ":").map { it.trim() }
//        //val sep = separate2.toString().split(":").map { it.trim() }
//        println(separate2[5])

        return rootView
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result =
            IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            //if qrcode has nothing in it
            if (result.contents == null) {

                Toast.makeText(context, "Result Not Found", Toast.LENGTH_LONG).show()
            } else {

                rootView.scantxt.text = "Scan Details: "+result.contents.toString()
                val str = result.contents.toString()
                val separate2 = str.split(".", ":").map { it.trim() }
                qrCode = separate2[1]
                qrVal = separate2[3]
                qrPoint = separate2[5]
                sendQR(qrCode,qrVal,qrPoint)
                Toast.makeText(context, result.contents, Toast.LENGTH_LONG).show()

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun sendQR(QrCode:String,Qrvalue:String,Qrpoint:String){


        showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.sendQrDetails(
            SendQrRequestParams(SharedPreferenceUtils.getLoggedInUserId(context as Context),QrCode,Qrvalue,Qrpoint)
        )
        responseCall.enqueue(SendQrResponse as Callback<SendQrResponse>)

    }

    private val SendQrResponse = object : NetworkCallBack<SendQrResponse>(){
        override fun onSuccessNetwork(
            data: Any?,
            response: NetworkResponse<SendQrResponse>
        ) {
            response.data?.respMessage?.let { status ->
                hide()
                if (response.data?.respMessage == "QR Code already Scanned"){
                    Toast.makeText(context, "QR Code already Scanned", Toast.LENGTH_LONG).show()

                }
                else{
                    Toast.makeText(context, "Scanned Successfully", Toast.LENGTH_LONG).show()
                }
                //activity!!.onBackPressed()

            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()
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
}