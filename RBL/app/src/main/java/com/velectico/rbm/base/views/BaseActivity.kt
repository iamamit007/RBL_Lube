package com.velectico.rbm.base.views

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.velectico.rbm.R

/**
 * Created by mymacbookpro on 2020-04-26
 * Base class of all the activities within this project
 */
abstract class BaseActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
       // window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ViewDataBinding>(this, getLayout())
        init(savedInstanceState, binding)
    }

    protected abstract fun getLayout():Int

    protected abstract fun init(savedInstanceState: Bundle?, binding:ViewDataBinding)

    protected fun showToastMessage(message:String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun showAlertDialog(msg:String?){
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle(getString(R.string.alert_title))
            .setMessage(msg)
            .setCancelable(false)
            .setNegativeButton(getString(R.string.ok_button)) {d, _ -> }
            .show()
    }

    fun addFragment(fragment: BaseFragment, viewId: Int){
        supportFragmentManager.beginTransaction()
            .add(viewId, fragment, fragment::class.java.simpleName)
            .commit()
    }

    fun addFragmentWithBackStack(fragment: BaseFragment, viewId: Int, tag:String){
        supportFragmentManager.beginTransaction()
            .add(viewId, fragment, fragment::class.java.simpleName)
            .addToBackStack(tag)
            .commit()
    }

    fun replaceFragment(fragment: BaseFragment, tag:String, viewId: Int){
        supportFragmentManager.beginTransaction()
            .replace(viewId, fragment, fragment::class.java.simpleName)
            .commit()
    }

    fun removeFragment(fragment: BaseFragment, tag:String, viewId: Int){
        supportFragmentManager.beginTransaction()
            .remove(fragment)
            .commitNow()
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
    }
}