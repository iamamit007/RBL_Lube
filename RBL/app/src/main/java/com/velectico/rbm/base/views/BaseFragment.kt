package com.velectico.rbm.base.views

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.velectico.rbm.expense.viewmodel.ExpenseViewModel
import com.velectico.rbm.masterdata.viewmodel.MasterDataViewModel
import com.velectico.rbm.network.manager.INetworkManager

/**
 * Created by mymacbookpro on 2020-04-26
 * Base class of all the fragments within this project
 */

abstract class BaseFragment : Fragment(), IBaseFragment {

    //open lateinit var networkManager: INetworkManager
    lateinit var baseActivity: BaseActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is BaseActivity){
            baseActivity = context as BaseActivity
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, getLayout(), container, false)
        binding?.let {
            init(binding)
        }
        return binding.root
    }

    protected fun showToastMessage(message:String){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    protected fun printLogMessage(tag:String, message:String){
        Log.d(tag, message)
    }

    fun addFragment(fragment: BaseFragment, viewId: Int){
        childFragmentManager.beginTransaction()
            .add(viewId, fragment, fragment::class.java.simpleName)
            .commit()
    }


    fun replaceFragment(fragment: BaseFragment, tag:String, viewId: Int){
        childFragmentManager.beginTransaction()
            .replace(viewId, fragment, fragment::class.java.simpleName)
            .commit()
    }

    fun removeFragment(fragment: BaseFragment, tag:String, viewId: Int){
        childFragmentManager.beginTransaction()
            .remove(fragment)
            .commitNow()
    }

    companion object{
        const val BASE_FRAGMENT = "BaseFragment"
    }
}