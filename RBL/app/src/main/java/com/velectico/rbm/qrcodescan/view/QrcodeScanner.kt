package com.velectico.rbm.qrcodescan.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.google.android.material.navigation.NavigationView
import com.kaopiz.kprogresshud.KProgressHUD
import com.velectico.rbm.QrcodeScannerViewModel
import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.databinding.QrcodeScannerFragmentBinding
import com.velectico.rbm.menuitems.viewmodel.MenuViewModel
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.redeem.model.GetRedeemDetailsRequestParams
import com.velectico.rbm.redeem.model.SendQrRequestParams
import com.velectico.rbm.redeem.model.SendQrResponse
import com.velectico.rbm.reminder.model.CreateReminderRequestParams
import com.velectico.rbm.reminder.model.CreateReminderResponse
import com.velectico.rbm.utils.DateUtility
import com.velectico.rbm.utils.DateUtils
import com.velectico.rbm.utils.SharedPreferenceUtils
import pub.devrel.easypermissions.EasyPermissions
import retrofit2.Callback
import java.text.SimpleDateFormat
import java.util.*


class QrcodeScanner : BaseFragment(){
    private lateinit var binding: QrcodeScannerFragmentBinding
    private lateinit var codeScanner: CodeScanner

    private val RC_CAMERA_PERM = 123
    override fun getLayout(): Int {
        return R.layout.qrcode_scanner_fragment
    }

    override fun init(binding: ViewDataBinding) {

        this.binding = binding as QrcodeScannerFragmentBinding
        checkPermission()
        cameraTask()


    }

    private fun hasCameraPermission():Boolean {
        return EasyPermissions.hasPermissions(baseActivity, Manifest.permission.CAMERA)
    }
    fun cameraTask() {
        if (hasCameraPermission())
        {
            // Have permission, do the thing!
            //Toast.makeText(this, "TODO: Camera things", Toast.LENGTH_LONG).show()
            val scannerView = binding.scannerView
            val activity = requireActivity()
            codeScanner = CodeScanner(activity, scannerView)
            codeScanner.decodeCallback = DecodeCallback {
                activity.runOnUiThread {
                    Toast.makeText(activity, it.text, Toast.LENGTH_LONG).show()
                    sendQR("","","")
                }
            }
            codeScanner.startPreview()
        }
        else
        {
            // Ask for one permission
            EasyPermissions.requestPermissions(
                baseActivity,
                getString(R.string.rationale_camera),
                RC_CAMERA_PERM,
                Manifest.permission.CAMERA)
        }
    }


    private fun checkPermission(){
        val checkSelfPermission = ContextCompat.checkSelfPermission(baseActivity, android.Manifest.permission.CAMERA)
        if (checkSelfPermission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(baseActivity, arrayOf(android.Manifest.permission.CAMERA), 1)
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
