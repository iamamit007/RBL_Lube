package com.velectico.rbm.beats.views

import android.content.Context
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import androidx.navigation.Navigation
import com.google.gson.annotations.SerializedName
import com.kaopiz.kprogresshud.KProgressHUD

import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.adapters.BeatListAdapter
import com.velectico.rbm.beats.adapters.BeatTargetAssignmentAdapter
import com.velectico.rbm.beats.adapters.orderDetailsBeatTask
import com.velectico.rbm.beats.model.*
import com.velectico.rbm.databinding.FragmentAssignTargetToBeatBinding
import com.velectico.rbm.databinding.FragmentBeatListBinding
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.utils.GloblalDataRepository
import com.velectico.rbm.utils.SharedPreferenceUtils
import retrofit2.Callback
import java.util.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class AssignTargetToBeat : BaseFragment() {

    private lateinit var binding: FragmentAssignTargetToBeatBinding;
    private lateinit var beatList : List<DealDistMechList>
    private lateinit var adapter: BeatTargetAssignmentAdapter
    override fun getLayout(): Int {
        return R.layout.fragment_assign_target_to_beat
    }

    companion object{
        var seletedItems = HashSet<DealDistMechList>()
        var orderDetailsBeatTaskList : MutableList<orderDetailsBeatTask> = mutableListOf<orderDetailsBeatTask>()


    }

    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentAssignTargetToBeatBinding
        hud =  KProgressHUD.create(activity)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel("Please wait")
            .setCancellable(true)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)
        binding.btnSaveTask.setOnClickListener {
           moveToBeatList()
            saveTaskToBEat()


        }
    }

    fun saveTaskToBEat(){

        showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val list :MutableList<AssignDetailsParams> = mutableListOf()

        for ((i,j) in seletedItems.withIndex()){
            if (j.UM_Role == "R"){
                val x = AssignDetailsParams(j.UM_ID!!,"0","0",orderDetailsBeatTaskList[i].orderAmount!!,orderDetailsBeatTaskList[i].paymentAmount!!,"")
                list.add(x)
            }else if (j.UM_Role == "D") {
                val x = AssignDetailsParams("0",j.UM_ID!!,"0",orderDetailsBeatTaskList[i].orderAmount!!,orderDetailsBeatTaskList[i].paymentAmount!!,"")
                list.add(x)
            }else if (j.UM_Role == "M"){
                val x = AssignDetailsParams("0","0",j.UM_ID!!,orderDetailsBeatTaskList[i].orderAmount!!,orderDetailsBeatTaskList[i].paymentAmount!!,"")
                list.add(x)
            }

        }
        val responseCall = apiInterface.assignTask(
            //DealDistMechListRequestParams( SharedPreferenceUtils.getLoggedInUserId(context as Context),AssignBeatToLocation.source,AssignBeatToLocation.areaList)
           AssignTaskRequestParams( SharedPreferenceUtils.getLoggedInUserId(context as Context),"63",list)
          //  AssignTaskRequestParams( SharedPreferenceUtils.getLoggedInUserId(context as Context),GloblalDataRepository.getInstance().scheduleId,list)
        )
        responseCall.enqueue(assignTaskResponseResponse as Callback<CreateBeatReportResponse>)
    }


    private val assignTaskResponseResponse = object : NetworkCallBack<CreateBeatReportResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<CreateBeatReportResponse>) {
            hide()
            Toast.makeText(activity!!,"${response.data?.respMessage}",Toast.LENGTH_SHORT).show()
        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()
        }

    }

    override fun onResume() {
        super.onResume()
        callApi()
    }

    private fun setUpRecyclerView() {
        adapter = BeatTargetAssignmentAdapter(beatList)
        binding.rvTargetList.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun moveToBeatList(){
        val navDirection =  AssignTargetToBeatDirections.actionAssignTargetToBeatToDateWiseBeatListFragment("")
        Navigation.findNavController(binding.btnSaveTask).navigate(navDirection)
    }

    fun callApi(){
        showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)

        val responseCall = apiInterface.getDealDistMechList(
            //DealDistMechListRequestParams( SharedPreferenceUtils.getLoggedInUserId(context as Context),AssignBeatToLocation.source,AssignBeatToLocation.areaList)
            DealDistMechListRequestParams( SharedPreferenceUtils.getLoggedInUserId(context as Context),"R","5")
        )
        responseCall.enqueue(orderVSQualityResponseResponse as Callback<DealDistMechListResponse>)

    }
    private  var dataList : List<TaskForList> = emptyList<TaskForList>()

    private val orderVSQualityResponseResponse = object : NetworkCallBack<DealDistMechListResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<DealDistMechListResponse>) {
            hide()
            if (response.data?.count != null && response.data?.count!! > 0){
                beatList = response.data.DealDistMechList
                setUpRecyclerView()
            }else{
                Toast.makeText(activity!!,"No Data Found",Toast.LENGTH_SHORT).show()
                moveToBeatList()
            }


        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()
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


}
