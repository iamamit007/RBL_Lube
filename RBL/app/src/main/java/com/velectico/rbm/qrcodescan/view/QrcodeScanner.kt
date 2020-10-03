package com.velectico.rbm.qrcodescan.view

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import com.google.zxing.integration.android.IntentIntegrator
import com.kaopiz.kprogresshud.KProgressHUD
import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.databinding.QrcodeScannerFragmentBinding
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.redeem.model.SendQrRequestParams
import com.velectico.rbm.redeem.model.SendQrResponse
import com.velectico.rbm.utils.SharedPreferenceUtils
import retrofit2.Callback


class QrcodeScanner : BaseFragment(){
    private lateinit var binding: QrcodeScannerFragmentBinding
   // private lateinit var codeScanner: CodeScanner
   private var qrScan: IntentIntegrator? = null
    override fun getLayout(): Int {
        return R.layout.qrcode_scanner_fragment
    }

    override fun init(binding: ViewDataBinding) {

        this.binding = binding as QrcodeScannerFragmentBinding
        //qrScan = IntentIntegrator(baseActivity)
       // qrScan!!.initiateScan()
//            val scannerView = binding.scannerView
//            val activity = requireActivity()
//            codeScanner = CodeScanner(activity, scannerView)
//            codeScanner.decodeCallback = DecodeCallback {
//                activity.runOnUiThread {
//                    Toast.makeText(activity, it.text, Toast.LENGTH_LONG).show()
//
//                    //sendQR("","","")
//                }
//            }
//            codeScanner.startPreview()


    }
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        val result =
            IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            //if qrcode has nothing in it
            if (result.contents == null) {
                showToastMessage("Result Not Found")
                //Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show()
            } else {
//                //if qr contains data
//                try {
//                    //converting the data to json
//                    JSONObject obj = new JSONObject(result.getContents());
//                    //setting values to textviews
//                    Toast.makeText(this, obj.getString("QR_Points"), Toast.LENGTH_LONG).show();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    //if control comes here
//                    //that means the encoded format not matches
//                    //in this case you can display whatever data is available on the qrcode
//                    //to a toast
                //Toast.makeText(this, result.contents, Toast.LENGTH_LONG).show()
                showToastMessage(result.contents)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
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
                    showToastMessage("QR Code already Scanned")
                }
                else{
                    showToastMessage("Scanned Success")
                }
                activity!!.onBackPressed()

            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()
        }

    }




}
