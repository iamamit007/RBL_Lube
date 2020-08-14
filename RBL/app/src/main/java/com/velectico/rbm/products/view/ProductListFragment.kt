package com.velectico.rbm.products.view

import android.content.Context
import android.util.Log
import androidx.databinding.ViewDataBinding
import androidx.navigation.Navigation
import com.kaopiz.kprogresshud.KProgressHUD
import com.velectico.rbm.R
import com.velectico.rbm.RBMLubricantsApplication
import com.velectico.rbm.base.views.BaseActivity
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.model.CreateOrderDetailsResponse
import com.velectico.rbm.beats.model.CreateOrderListDetails
import com.velectico.rbm.beats.model.CreateOrderListRequestParams
import com.velectico.rbm.databinding.FragmentProductListBinding
import com.velectico.rbm.databinding.RowProductListBinding
import com.velectico.rbm.menuitems.viewmodel.MenuViewModel
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.products.adapters.ProductListAdapter
import com.velectico.rbm.utils.*
import retrofit2.Callback

/**
 * Fragement to display list of all the products
 */

//https://developer.android.com/topic/libraries/view-binding#kotlin
class ProductListFragment : BaseFragment() {

    private lateinit var binding: FragmentProductListBinding
    private lateinit var adapter : ProductListAdapter
    lateinit var menuViewModel: MenuViewModel
    private var orderCartList : List<CreateOrderListDetails> = emptyList()
    var segId= ""
    var catId= ""
    companion object {
        var seletedItemsChecked = HashSet<CreateOrderListDetails>()
    }
    override fun getLayout(): Int {
        return R.layout.fragment_product_list
    }

    override fun init(binding: ViewDataBinding) {
        //setHasOptionsMenu(true)

        this.binding = binding as FragmentProductListBinding
        menuViewModel = MenuViewModel.getInstance(activity as BaseActivity)
        catId = arguments?.getString(  "catId").toString()
        segId = arguments?.getString(  "segId").toString()



         if(menuViewModel.loginResponse.value?.userDetails?.get(0)?.uMRole.toString() == MECHANIC_ROLE || menuViewModel.loginResponse.value?.userDetails?.get(0)?.uMRole.toString() == DEALER_ROLE || menuViewModel.loginResponse.value?.userDetails?.get(0)?.uMRole.toString() == DISTRIBUTER_ROLE ){
             binding.fab.hide()
         }


        binding.fab.setOnClickListener {
            if (seletedItemsChecked.count() > 0){
                RBMLubricantsApplication.filterFrom = ""
                RBMLubricantsApplication.fromProductList = "Product"
                moveToOrder()
            }
            else{
                showToastMessage("Please select atleast one list to continue")
            }

        }

        binding.fabFilter.setOnClickListener {
            moveToFilter()
        }
        setUpRecyclerView()
        callCreateOrderList()

    }




    private fun setUpRecyclerView(){
       /* adapter = ProductListAdapter(
            requireActivity()
        );*/

        adapter = ProductListAdapter(object : ProductListAdapter.IProdListActionCallBack{
            override fun moveToProdDetails(position: Int, beatTaskId: String?,binding: RowProductListBinding) {
                Log.e("test","onAddTask"+beatTaskId)
                val navDirection =  ProductListFragmentDirections.actionProductListToProductDetailsListFragment(orderCartList[position])
                Navigation.findNavController(binding.navigateToDetails).navigate(navDirection)
            }
        });


        binding?.rvProductList?.adapter = adapter
        adapter.data = orderCartList
    }


    private fun moveToOrder(){
        val navDirection =  ProductListFragmentDirections.actionProductListToCreateOrderFragment("","")
        Navigation.findNavController(binding.fab).navigate(navDirection)
    }

    private fun moveToFilter(){

        val navDirection =  ProductListFragmentDirections.actionProductListToProductFilterFragment()
        Navigation.findNavController(binding.fabFilter).navigate(navDirection)
    }

    var hud: KProgressHUD? = null
    fun  showHud(){
        hud =  KProgressHUD.create(activity)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel("Please wait")
            .setCancellable(true)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)
            .show();
    }

    fun hide(){
        hud?.dismiss()
    }
    fun callCreateOrderList(){
        showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getCreateOrderList(
            CreateOrderListRequestParams(SharedPreferenceUtils.getLoggedInUserId(context as Context),segId,catId)
        )
        responseCall.enqueue(CreateOrderDetailsResponse as Callback<CreateOrderDetailsResponse>)
    }

    private val CreateOrderDetailsResponse = object : NetworkCallBack<CreateOrderDetailsResponse>() {
        override fun onSuccessNetwork(
            data: Any?,
            response: NetworkResponse<CreateOrderDetailsResponse>
        ) {
            hide()
            response.data?.status?.let { status ->
                Log.e("test333","OrderHistoryDetailsResponse status="+response.data)
                orderCartList.toMutableList().clear()
                if (response.data.count > 0) {
                    orderCartList = response.data.CreateOrderList!!.toMutableList()
                    setUpRecyclerView()
                } else {
                    showToastMessage("No data found")

                }

            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            // hide()
        }


    }
}
