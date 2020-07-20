package com.velectico.rbm.menuitems.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.velectico.rbm.base.viewmodel.BaseViewModel
import com.velectico.rbm.base.views.BaseActivity
import com.velectico.rbm.base.model.UIError
import com.velectico.rbm.loginreg.model.LoginResponse
import com.velectico.rbm.menuitems.model.ResourceListResponse
import com.velectico.rbm.network.apiconstants.ENDPOINT_GET_ALL_USERS
import com.velectico.rbm.network.apiconstants.ENDPOINT_GET_RESOURCES
import com.velectico.rbm.network.apiconstants.READ_ALL_RESOURCES_DATA
import com.velectico.rbm.network.apiconstants.READ_ALL_USERS_DATA
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.INetworkManager
import com.velectico.rbm.network.manager.ManagerFactory
import com.velectico.rbm.network.manager.getNetworkManager
import com.velectico.rbm.network.request.NetworkRequest
import com.velectico.rbm.network.response.NetworkResponse

/**
 * Created by mymacbookpro on 2020-05-01
 * TODO: Add a class header comment!
 */
class MenuViewModel(private val networkManager: INetworkManager) : BaseViewModel(){

    val loading = MutableLiveData<Boolean>()
    val errorLiveData = MutableLiveData<UIError>()
    var allResourcesResponse = MutableLiveData<ResourceListResponse>()
    var allUsersResponse = MutableLiveData<ResourceListResponse>()
    var loginResponse = MutableLiveData<LoginResponse>()

    fun readAllResources(){
        loading.postValue(true)

        val readResourceRequest = NetworkRequest<Unit>(
            apiName = READ_ALL_RESOURCES_DATA,
            endPoint = ENDPOINT_GET_RESOURCES
        )
        networkManager.makeAsyncCall(request = readResourceRequest, callBack = readAllResourcesResponse)
    }

    private val readAllResourcesResponse = object : NetworkCallBack<ResourceListResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<ResourceListResponse>) {
            allResourcesResponse.value = response.data
            loading.postValue(false)
        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            loading.postValue(false)
            errorLiveData.postValue(
                UIError(
                    error.errorCode ?: 0, error.errorMessage
                )
            )
        }

    }

    fun readAllUsers(){
        loading.postValue(true)

        val params = HashMap<String, String>()
        params["page"] = "2"

        val readUsersRequest = NetworkRequest<Unit>(
            apiName = READ_ALL_USERS_DATA,
            endPoint = ENDPOINT_GET_ALL_USERS,
            queryParameters = params
        )
        networkManager.makeAsyncCall(request = readUsersRequest, callBack = readAllUsersResponse)
    }

    private val readAllUsersResponse = object : NetworkCallBack<ResourceListResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<ResourceListResponse>) {
            allUsersResponse.value = response.data
            loading.postValue(false)
        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            loading.postValue(false)
            errorLiveData.postValue(
                UIError(
                    error.errorCode ?: 0, error.errorMessage
                )
            )
        }

    }

    companion object{
        private val factory = MenuViewModelFactory()

        fun getInstance(activity: BaseActivity): MenuViewModel {
            return ViewModelProviders.of(activity, factory)[MenuViewModel::class.java]
        }
    }
}

class MenuViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MenuViewModel(getNetworkManager(ManagerFactory.TEST)) as T
    }
}