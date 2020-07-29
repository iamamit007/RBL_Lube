package com.velectico.rbm.beats.views

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.google.gson.annotations.SerializedName
import com.kaopiz.kprogresshud.KProgressHUD

import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.adapters.BeatAssignmentTaskListAdapter
import com.velectico.rbm.beats.model.*
import com.velectico.rbm.beats.viewmodel.BeatSharedViewModel
import com.velectico.rbm.databinding.FragmentAssignBeatToLocationBinding
import com.velectico.rbm.databinding.FragmentAssignTaskForBeatBinding
import com.velectico.rbm.databinding.FragmentBeatListBinding
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.utils.SharedPreferenceUtils
import retrofit2.Callback
import java.util.ArrayList


class AssignBeatToLocation : BaseFragment() {

    private lateinit var binding: FragmentAssignBeatToLocationBinding
    private lateinit var mBeatSharedViewModel: BeatSharedViewModel


    override fun getLayout(): Int {
        return R.layout.fragment_assign_beat_to_location
    }

    override fun init(binding: ViewDataBinding) {

        this.binding = binding as FragmentAssignBeatToLocationBinding
        binding.btnAssignTaskToLocation.setOnClickListener {
            moveToBeatTarget()
        }

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.onCreate(savedInstanceState)
        mBeatSharedViewModel = BeatSharedViewModel.getInstance(baseActivity)
        hud =  KProgressHUD.create(activity)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel("Please wait")
            .setCancellable(true)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)

        var provinceList: MutableList<String> = ArrayList()
        provinceList.add("Region")
        provinceList.add("Zone")
        provinceList.add("District")
        provinceList.add("Area")
        binding.spLocation.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View?, position: Int, id: Long) {
                binding.etHigherLocation.setText( dataList[position].respValue)


            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }
        binding.spBeatName.setItem(provinceList)


        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View?, position: Int, id: Long) {
                binding.etHigherLocation.setText( dataList[position].respValue)
                taskLevel = dataList[position].taskLevel!!
                callApi2(dataList[position].taskLevel!!)
            }
            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }

        binding.spLocation.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View?, position: Int, id: Long) {
                binding.etLocation.setText( locationList[position].locValue)
//                beatLevel = provinceList[position].get(0).toString().toUpperCase()
//                callApi(type = beatLevel)
//                mBeatSharedViewModel.beats.value?.beatId = position.toString()
//                mBeatSharedViewModel.beats.value?.beatName =  provinceList[position]
                callApi3(taskLevel,locationList[position].locValue!!)
            }
            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        callApi()
       // binding.spBeatName.setItem(provinceList)
    }

    var source:String = ""
    var areaList:String = ""
    var taskLevel:String = ""
    fun callApi(){
        showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getTaskForList(
            TaskForListRequestParams( SharedPreferenceUtils.getLoggedInUserId(context as Context),"38")
        )
        responseCall.enqueue(orderVSQualityResponseResponse as Callback<TaskForListResponse>)

    }
    private  var dataList : List<TaskForList> = emptyList<TaskForList>()
    private val orderVSQualityResponseResponse = object : NetworkCallBack<TaskForListResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<TaskForListResponse>) {
            response.data?.status?.let { status ->

                hide()
                source = response.data.source!!
                areaList = response.data.areaList!!
                dataList  = response.data.TaskForList[0]
                var statList: MutableList<String> = ArrayList()
                for (i in response.data.TaskForList[0]){
                    statList.add(i.respValue!!)
                }

               // binding.spBeatName.setItem(statList)
                    //binding.spBeatName.setSelection(0)

                val adapter2 = context?.let {
                    ArrayAdapter(
                        it,
                        android.R.layout.simple_spinner_item, statList)
                }
                binding.spinner.adapter = adapter2
              //  taskLevel = dataList[0].taskLevel!!
               // callApi2(dataList[0].taskLevel!!)

            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()
        }

    }

    fun callApi2(level:String){
        showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getLocationByLevelList(
            LocationByLevelListRequestParams( SharedPreferenceUtils.getLoggedInUserId(context as Context),source,level,areaList)
        )
        responseCall.enqueue(getLocationByLevelListResponse as Callback<LocationByLevelListResponse>)

    }
    private  var locationList : List<LocationList> = emptyList<LocationList>()
    private val getLocationByLevelListResponse = object : NetworkCallBack<LocationByLevelListResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<LocationByLevelListResponse>) {
            response.data?.status?.let { status ->

                hide()
                locationList  = response.data.LocationList
                var statList: MutableList<String> = ArrayList()
                for (i in locationList){
                    statList.add(i.locValue!!)
                }

                binding.spLocation.setItem(statList)
                binding.spLocation.setSelection(0)
              //  callApi3(locationList)

            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()
        }

    }


    fun callApi3(level:String,areaId:String){
        showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getAssignToList(
            AssignToListRequestParams( SharedPreferenceUtils.getLoggedInUserId(context as Context),source,level,areaId)
        )
        responseCall.enqueue(getAssignToListResponse as Callback<AssignToListResponse>)

    }
    private  var personList : List<AssigntoList> = emptyList<AssigntoList>()
    private val getAssignToListResponse = object : NetworkCallBack<AssignToListResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<AssignToListResponse>) {
            response.data?.status?.let { status ->

                hide()
                personList  = response.data.AssignToList
                var statList: MutableList<String> = ArrayList()
                for (i in personList){
                    statList.add(i.UM_Name!!)
                }

                binding.spSalesperson.setItem(statList)
                binding.spSalesperson.setSelection(0)

            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()
        }

    }

    private fun moveToBeatTarget(){

        val navDirection =  AssignBeatToLocationDirections.actionAssignBeatToLocationToAssignTargetToBeat()
        Navigation.findNavController(binding.btnAssignTaskToLocation).navigate(navDirection)
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
