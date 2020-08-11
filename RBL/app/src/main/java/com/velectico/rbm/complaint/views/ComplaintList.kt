package com.velectico.rbm.complaint.views

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.ViewDataBinding
import androidx.navigation.Navigation
import com.kaopiz.kprogresshud.KProgressHUD

import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseActivity
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.adapters.BeatListAdapter
import com.velectico.rbm.beats.model.*
import com.velectico.rbm.beats.views.BeatListFragmentDirections
import com.velectico.rbm.complaint.adapter.ComplaintListAdapter
import com.velectico.rbm.complaint.model.ComplainListDetails
import com.velectico.rbm.complaint.model.ComplaintListRequestParams
import com.velectico.rbm.complaint.model.ComplaintListResponse
import com.velectico.rbm.complaint.model.ComplaintModel
import com.velectico.rbm.databinding.FragmentBeatListBinding
import com.velectico.rbm.databinding.FragmentComplaintListBinding
import com.velectico.rbm.databinding.RowBeatListBinding
import com.velectico.rbm.databinding.RowComplaintListBinding
import com.velectico.rbm.menuitems.viewmodel.MenuViewModel
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.utils.MECHANIC_ROLE
import com.velectico.rbm.utils.SALES_LEAD_ROLE
import com.velectico.rbm.utils.SharedPreferenceUtils
import com.velectico.rbm.utils.TEMP_CURRENT_LOGGED_IN
import retrofit2.Callback
import java.util.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class ComplaintList : BaseFragment() {

    private lateinit var menuViewModel: MenuViewModel
    private lateinit var binding: FragmentComplaintListBinding;
    private var complaintList : List<ComplainListDetails> = emptyList()
    private lateinit var adapter: ComplaintListAdapter
    var dealerId = "0"
    var distribId = "0"
    var orderStatus = "O"
    override fun getLayout(): Int {
        return R.layout.fragment_complaint_list
    }

    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentComplaintListBinding
        //complaintList = ComplaintModel().getDummyComplaintList()
        menuViewModel = MenuViewModel.getInstance(activity as BaseActivity)
        binding.spinnerDist.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (distNameList.size > 0 ){
                    val x = distNameList[position]
                    distribId = x.UM_ID!!
                    callApiList()
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }
        binding.spinnerDeal1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (dealNameList.size > 0 ){
                    val x = dealNameList[position]
                    dealerId = x.UM_ID!!
                    callApiList()

                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }
        callDistApi()
        callDealApi()

        setUpRecyclerView()
        binding.expenseButton.setOnClickListener{
            orderStatus = "C"
            callApiList()
            //setUpRecyclerView()
        }
        binding.beatButton.setOnClickListener{
            orderStatus = "O"
            callApiList()
            //setUpRecyclerView()
        }

//        if(menuViewModel.loginResponse.value?.userDetails?.get(0)?.uMRole.toString() == MECHANIC_ROLE){
//            binding.fab.hide()
//        }


        binding.fab.setOnClickListener {
            moveToCreateComplaint()
        }

        binding.fabFilter.setOnClickListener {
            moveToFilterComplaint()
        }
    }


    private fun setUpRecyclerView() {


        adapter = ComplaintListAdapter(object : ComplaintListAdapter.IComplaintListActionCallBack{
            override fun moveToComplainDetails(position: Int, beatTaskId: String?,binding: RowComplaintListBinding) {
                Log.e("test","onAddTask"+beatTaskId)
                val navDirection =  ComplaintListDirections.actionComplaintListToCreateComplaintsUserWise()
                Navigation.findNavController(binding.navigateToDetails).navigate(navDirection)
            }
        })
        binding.rvBeatList.adapter = adapter
        adapter.complaintList = complaintList;
    }

     private fun moveToCreateComplaint(){
      val navDirection =  ComplaintListDirections.actionComplaintListToCreateComplaintsUserWise()
         Navigation.findNavController(binding.fab).navigate(navDirection)
     }

    private fun moveToFilterComplaint(){
        val navDirection =  ComplaintListDirections.actionComplaintListToComplaintFilterFragment()
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
                }
                val adapter2 = context?.let {
                    ArrayAdapter(
                        it,
                        android.R.layout.simple_spinner_item, statList)
                }
                binding.spinnerDist.adapter = adapter2


            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()
        }

    }

    fun callDistApi(){
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.distDropDownList(
            DistListRequestParams(SharedPreferenceUtils.getLoggedInUserId(context as Context))
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
                }
                val adapter2 = context?.let {
                    ArrayAdapter(
                        it,
                        android.R.layout.simple_spinner_item, statList)
                }
                binding.spinnerDeal1.adapter = adapter2


            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()
        }

    }

    fun callDealApi(){
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.dealDropDownList(
            DealListRequestParams(SharedPreferenceUtils.getLoggedInUserId(context as Context))
        )

        responseCall.enqueue(dealNameResponse as Callback<DealListResponse>)

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

    fun callApiList(){
        showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getComplaintList(

            ComplaintListRequestParams(SharedPreferenceUtils.getLoggedInUserId(context as Context),"0",dealerId,distribId,"",orderStatus)
        )
        responseCall.enqueue(ComplaintListResponse as Callback<ComplaintListResponse>)
    }

    private val ComplaintListResponse = object : NetworkCallBack<ComplaintListResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<ComplaintListResponse>) {
            hide()
            response.data?.status?.let { status ->
                Log.e("test0000","OrderHistoryDetailsResponse status="+response.data)
                complaintList.toMutableList().clear()
                if (response.data.count > 0){
                    complaintList = response.data.ComplaintList!!.toMutableList()
                    setUpRecyclerView()
                    // binding.rvComplaintList.visibility = View.VISIBLE
                }
                else{
                    showToastMessage("No data found")
                    //binding.resolvButton.visibility = View.GONE
                    //binding.pendButton.visibility = View.GONE
                    //binding.rvComplaintList.visibility = View.GONE
                }

            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            // hide()
        }

    }
}
