package com.velectico.rbm

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import com.velectico.rbm.base.views.BaseActivity
import com.velectico.rbm.databinding.ActivitySplashBinding
import com.velectico.rbm.loginreg.view.LoginActivity


class SplashActivity : BaseActivity() {
    private val CAMERA_PERMISSION_CODE = 100
    private lateinit var binding: ActivitySplashBinding
    //http://androidforbeginners.blogspot.com/2010/06/how-to-tile-background-image-in-android.html
    override fun getLayout(): Int {
        return R.layout.activity_splash
    }

    override fun init(savedInstanceState: Bundle?, binding: ViewDataBinding) {
        this.binding = binding as ActivitySplashBinding
        //checkPermission(Manifest.permission.CAMERA,
            //CAMERA_PERMISSION_CODE);
        Handler().postDelayed({
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, SPLASH_TIME_OUT)

        requestPermission()
    }

    companion object{
        const val SPLASH_TIME_OUT:Long = 5000 // 3 sec
    }

    private fun checkPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(this, permission)
            == PackageManager.PERMISSION_DENIED
        ) {

            // Requesting the permission
            ActivityCompat.requestPermissions(
                this, arrayOf(permission),
                requestCode
            )
        } else {
            Toast.makeText(
                this,
                "Permission already granted",
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super
            .onRequestPermissionsResult(
                requestCode,
                permissions,
                grantResults
            )
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.size > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(
                    this,
                    "Camera Permission Granted",
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else {
                requestPermission()
                Toast.makeText(
                    this,
                    "Camera Permission Denied",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }


    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.CAMERA),
            700
        )
    }
    

}
