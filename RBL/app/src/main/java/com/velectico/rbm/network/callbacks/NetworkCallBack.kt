package com.velectico.rbm.network.callbacks

import android.util.Log
import com.velectico.rbm.network.apiconstants.ERROR_CODE_OTHER
import com.velectico.rbm.network.apiconstants.ERROR_CODE_TIME_OUT
import com.velectico.rbm.network.apiconstants.ERROR_CODE_UNKNOWN_HOST
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.R
import com.velectico.rbm.RBMLubricantsApplication
import retrofit2.Call
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Created by mymacbookpro on 2020-04-26
 * TODO: Add a class header comment!
 */
abstract class NetworkCallBack<T> : INetworkCallBack<T> {

    override fun onResponse(call: Call<T>, response: Response<T>) {
        Log.e("test","URL=>"+response.raw().request().url())
        if(response.isSuccessful){
            onSuccessNetwork(call, NetworkResponse(response.code(), response.body(), response.headers()))
        } else{
            onFailureNetwork(call, NetworkError(response.code(), response.message()))
        }
    }

    final override fun onFailure(call: Call<T>, t: Throwable) {
        var networkError = when(t){
            is UnknownHostException -> NetworkError(
                ERROR_CODE_UNKNOWN_HOST,
                RBMLubricantsApplication.getAppContext()?.getString(R.string.generic_no_internet) ?: "")
            is SocketTimeoutException -> NetworkError(
                ERROR_CODE_TIME_OUT,
                RBMLubricantsApplication.getAppContext()?.getString(R.string.request_timeout) ?: "")
            else -> if(t.message != null) NetworkError(ERROR_CODE_OTHER, t.message) else NetworkError(
                ERROR_CODE_OTHER,
                RBMLubricantsApplication.getAppContext()?.getString(R.string.generic_no_internet) ?: "")
        }
        onFailureNetwork(call, networkError)
    }

}