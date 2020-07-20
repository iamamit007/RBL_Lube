package com.velectico.rbm.qrcodescan.view

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
import com.velectico.rbm.QrcodeScannerViewModel
import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.databinding.QrcodeScannerFragmentBinding
import com.velectico.rbm.menuitems.viewmodel.MenuViewModel


class QrcodeScanner : BaseFragment(){
    private lateinit var binding: QrcodeScannerFragmentBinding
    private lateinit var codeScanner: CodeScanner


    override fun getLayout(): Int {
        return R.layout.qrcode_scanner_fragment
    }

    override fun init(binding: ViewDataBinding) {

        this.binding = binding as QrcodeScannerFragmentBinding
        checkPermission()
        val scannerView = binding.scannerView
        val activity = requireActivity()
        codeScanner = CodeScanner(activity, scannerView)
        codeScanner.decodeCallback = DecodeCallback {
            activity.runOnUiThread {
                Toast.makeText(activity, it.text, Toast.LENGTH_LONG).show()
            }
        }
        codeScanner.startPreview()

    }




    private fun checkPermission(){
        val checkSelfPermission = ContextCompat.checkSelfPermission(baseActivity, android.Manifest.permission.CAMERA)
        if (checkSelfPermission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(baseActivity, arrayOf(android.Manifest.permission.CAMERA), 1)
        }
    }

}
