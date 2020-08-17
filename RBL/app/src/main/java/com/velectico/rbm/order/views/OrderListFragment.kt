package com.velectico.rbm.order.views

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.ViewDataBinding
import androidx.navigation.Navigation
import com.kaopiz.kprogresshud.KProgressHUD
import com.velectico.rbm.R
import com.velectico.rbm.RBMLubricantsApplication
import com.velectico.rbm.base.views.BaseActivity
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.model.*
import com.velectico.rbm.databinding.FragmentOrderListBinding
import com.velectico.rbm.databinding.RowOrderHeadListBinding
import com.velectico.rbm.menuitems.viewmodel.MenuViewModel
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.order.adapters.OrderHeadListAdapter
import com.velectico.rbm.utils.DEALER_ROLE
import com.velectico.rbm.utils.DISTRIBUTER_ROLE
import com.velectico.rbm.utils.GloblalDataRepository
import com.velectico.rbm.utils.SharedPreferenceUtils
import retrofit2.Callback
import java.util.ArrayList


class OrderListFragment : BaseFragment()  {
    private lateinit var binding: FragmentOrderListBinding
    private var orderHeadList : List<OrderListDetails> = emptyList()
    private lateinit var adapter: OrderHeadListAdapter
    var orderStatus = ""
    var taskDetails = BeatTaskDetails()
    var userId = ""
    var dealerId = "0"
    var distribId = "0"
    private lateinit var menuViewModel: MenuViewModel
    override fun getLayout(): Int {
        return R.layout.fragment_order_list
    }



    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentOrderListBinding
        menuViewModel = MenuViewModel.getInstance(activity as BaseActivity)
        binding.spinnerDeal.visibility = View.GONE
        val languages = resources.getStringArray(R.array.array_dealDist)

        // access the spinner

        if (binding.spinnerType != null) {
            val adapter = context?.let {
                ArrayAdapter(
                    it,
                    android.R.layout.simple_spinner_item, languages)
            }
            binding.spinnerType.adapter = adapter

            binding.spinnerType.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {
                    showToastMessage(languages[position])

                    if (languages[position] == "Dealer"){
                        //binding.spinnerDeal.visibility = View.VISIBLE
                        //binding.spinnerDealDis.visibility = View.GONE
                        callDealApi()
                    }
                    else{
                        //binding.spinnerDeal.visibility = View.GONE
                        //binding.spinnerDealDis.visibility = View.VISIBLE
                        callDistApi()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }
        //showToastMessage(SharedPreferenceUtils.getLoggedInUserId(context as Context))
        //orderHeadList = OrderHead().getDummyOrderList()
        taskDetails = arguments!!.get("taskDetails") as BeatTaskDetails
        //showToastMessage(taskDetails.toString())
            if (taskDetails.dealerId != null){
                dealerId = taskDetails.dealerId.toString()
                distribId = taskDetails.distribId.toString()

            }

        if (RBMLubricantsApplication.globalRole == "Team" ) {
            binding.fab.visibility = View.GONE

            userId = GloblalDataRepository.getInstance().teamUserId
        }
        else{
            userId = SharedPreferenceUtils.getLoggedInUserId(context as Context)
        }
        if (RBMLubricantsApplication.fromBeat == "Beat" ){
            binding.spinnerDealDis.visibility = View.GONE
            binding.spinnerType.visibility = View.GONE
        }
        else{
            //callDistApi()
            //callDealApi()
        }
        //showToastMessage(spinnerType.selectedItem.toString())

        binding.fab.setOnClickListener {
            moveToCreateOrder()

        }

        binding.fabFilter.setOnClickListener {
            moveToFilterOrder()
        }
        binding.allButton.setOnClickListener{
            orderStatus = ""
            //setUpRecyclerView()
            callApiOrderList()
        }
        binding.pendingButton.setOnClickListener{
            orderStatus = "O"
            //setUpRecyclerView()
            callApiOrderList()
        }
        binding.completedButton.setOnClickListener{
            orderStatus = "C"
            //setUpRecyclerView()
            callApiOrderList()
        }
        if(menuViewModel.loginResponse.value?.userDetails?.get(0)?.uMRole.toString() == DEALER_ROLE){
            binding.spinnerDealDis.visibility = View.GONE
            binding.spinnerType.visibility = View.GONE
            dealerId = menuViewModel.loginResponse.value?.userDetails?.get(0)?.uMID.toString()
            callApiOrderList()
            return
        }
        if(menuViewModel.loginResponse.value?.userDetails?.get(0)?.uMRole.toString() == DISTRIBUTER_ROLE){
            binding.spinnerDealDis.visibility = View.GONE
            binding.spinnerType.visibility = View.GONE
            distribId = menuViewModel.loginResponse.value?.userDetails?.get(0)?.uMID.toString()
            callApiOrderList()
            return
        }

        setUpRecyclerView()
        callApiOrderList()







        binding.spinnerDealDis.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View?, position: Int, id: Long) {

                if (binding.spinnerType.selectedItem == "Dealer"){
                    if (dealNameList.size > 0 ){
                        val x = dealNameList[position]
                        dealerId = x.UM_ID!!
                        callApiOrderList()

                    }

                }
                else if (binding.spinnerType.selectedItem == "Distributor"){
                    if (distNameList.size > 0) {
                        val x = distNameList[position]
                        distribId = x.UM_ID!!
                        callApiOrderList()
                    }
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }

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
    fun callApiOrderList(){
        showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getBeatAllOrderHistory(
            //BeatAllOrderListRequestParams("7001507620","61","0",orderStatus)
            BeatAllOrderListRequestParams(userId,dealerId,distribId,orderStatus)
        )
        responseCall.enqueue(OrderHistoryDetailsResponse as Callback<OrderHistoryDetailsResponse>)
    }

    private val OrderHistoryDetailsResponse = object : NetworkCallBack<OrderHistoryDetailsResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<OrderHistoryDetailsResponse>) {
            hide()
            response.data?.status?.let { status ->
                Log.e("test33333333","OrderHistoryDetailsResponse status="+response.data)
                orderHeadList.toMutableList().clear()
                if (response.data.count > 0){
                    orderHeadList = response.data.OrderList!!.toMutableList()
                    //binding.rvOrderList.visibility = View.VISIBLE
                    setUpRecyclerView()
                }
                else{
                    showToastMessage("No data found")
                    //binding.pendingButton.visibility = View.GONE
                    //binding.completedButton.visibility = View.GONE
                    //binding.allButton.visibility = View.GONE
                    //binding.rvOrderList.visibility = View.GONE
                }

            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
           // hide()
        }

    }




private fun setUpRecyclerView() {
      //  adapter = OrderHeadListAdapter();
        adapter = OrderHeadListAdapter(object : OrderHeadListAdapter.IBeatDateListActionCallBack{
            override fun moveToOrderDetails(position: Int, beatTaskId: String?,binding: RowOrderHeadListBinding) {
                Log.e("test","onAddTask"+beatTaskId)

                val navDirection =  OrderListFragmentDirections.actionOrderListFragmentToOrderDetailsFragment2(taskDetails,orderHeadList[position])
                Navigation.findNavController(binding.navigateToDetails).navigate(navDirection)


            }
        });










        binding.rvOrderList.adapter = adapter
        adapter.orderList = orderHeadList
    }

    private fun moveToCreateOrder(){
        val navDirection =  OrderListFragmentDirections.actionOrderListFragmentToProductFilterFragment()
        Navigation.findNavController(binding.fab).navigate(navDirection)
    }

    private fun moveToFilterOrder(){
        RBMLubricantsApplication.filterFrom = ""
        val navDirection =  OrderListFragmentDirections.actionOrderListFragmentToOrderFilter()
        Navigation.findNavController(binding.fabFilter).navigate(navDirection)
    }


    private  var distNameList : List<DistDropdownDetails> = emptyList<DistDropdownDetails>()
    private val distNameResponse = object : NetworkCallBack<DistListResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<DistListResponse>) {
            response.data?.status?.let { status ->
                //showToastMessage(response.data.DistList.toString())
                hide()
                distNameList  = response.data.DistList
                var statList: MutableList<String> = ArrayList()
                for (i in distNameList){
                    statList.add(i.UM_Name!!)
                    distribId = i.UM_ID!!
                }
                val adapter2 = context?.let {
                    ArrayAdapter(
                        it,
                        android.R.layout.simple_spinner_item, statList)
                }

            if (binding.spinnerType.selectedItem == "Distributor"){
                binding.spinnerDealDis.adapter = adapter2
            }

            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()
        }

    }

    fun callDistApi(){
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.distDropDownList(DistListRequestParams(userId)
        )

                responseCall.enqueue(distNameResponse as Callback<DistListResponse>)

    }

    private  var dealNameList : List<DealDropdownDetails> = emptyList<DealDropdownDetails>()
    private val dealNameResponse = object : NetworkCallBack<DealListResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<DealListResponse>) {
            response.data?.status?.let { status ->
                //showToastMessage(response.data.DealList.toString())
                hide()
                dealNameList  = response.data.DealList
                var statList: MutableList<String> = ArrayList()
                for (i in dealNameList){
                    statList.add(i.UM_Name!!)
                    dealerId = i.UM_ID!!
                }
                val adapter2 = context?.let {
                    ArrayAdapter(
                        it,
                        android.R.layout.simple_spinner_item, statList)
                }
                if (binding.spinnerType.selectedItem == "Dealer"){
                    binding.spinnerDealDis.adapter = adapter2
                }



            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()
        }

    }

    fun callDealApi(){
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.dealDropDownList(
            DealListRequestParams(userId)
        )

        responseCall.enqueue(dealNameResponse as Callback<DealListResponse>)

    }


}