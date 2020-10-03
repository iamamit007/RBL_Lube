package com.velectico.rbm.beats.views

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.ViewDataBinding
import androidx.navigation.Navigation
import com.kaopiz.kprogresshud.KProgressHUD

import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.model.*
import com.velectico.rbm.beats.viewmodel.BeatSharedViewModel
import com.velectico.rbm.databinding.FragmentAssignBeatToLocationBinding
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.utils.DateUtils
import com.velectico.rbm.utils.GloblalDataRepository
import com.velectico.rbm.utils.SharedPreferenceUtils
import retrofit2.Callback
import java.text.SimpleDateFormat
import java.util.*


class AssignBeatToLocation : BaseFragment() {

    private lateinit var binding: FragmentAssignBeatToLocationBinding
    private lateinit var mBeatSharedViewModel: BeatSharedViewModel
    var startDate = ""
    var endDate = ""
        companion object{

        var source:String = ""
        var areaList:String = ""
        var taskLevel:String = ""
        var personId:String = ""

    }
    override fun getLayout(): Int {
        return R.layout.fragment_assign_beat_to_location
    }

    override fun init(binding: ViewDataBinding) {
        //showToastMessage(GloblalDataRepository.getInstance().scheduleId)
        this.binding = binding as FragmentAssignBeatToLocationBinding
        startDate = arguments?.getString(  "startDate").toString()
        endDate = arguments?.getString(  "endDate").toString()
        binding.btnAssignTaskToLocation.setOnClickListener {
            moveToBeatTarget()
        }
        val inpFormat =  SimpleDateFormat("dd/MM/yy", Locale.US);
        val  outputformat =  SimpleDateFormat("dd-MMM-yy", Locale.US);
        val stdate =  DateUtils.parseDate(startDate,inpFormat,outputformat)
        val endate =  DateUtils.parseDate(endDate,inpFormat,outputformat)

        binding.tvBeatScheduleName.text = "Add Beat Task (" +stdate +" to " + endate +")"
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





        binding.spinner3.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View?, position: Int, id: Long) {
                //binding.etLocation.setText( personList[position].UM_Name)
                personId = personList[position].UM_ID!!


            }
            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        callApi()
       // binding.spBeatName.setItem(provinceList)
    }

    fun callApi(){
        showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getTaskForList(
            TaskForListRequestParams( SharedPreferenceUtils.getLoggedInUserId(context as Context), GloblalDataRepository.getInstance().scheduleId)
        )
        responseCall.enqueue(orderVSQualityResponseResponse as Callback<TaskForListResponse>)

    }
    private  var dataList : List<TaskForList> = emptyList<TaskForList>()
    private val orderVSQualityResponseResponse = object : NetworkCallBack<TaskForListResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<TaskForListResponse>) {
            response.data?.status?.let { status ->

                hide()
                if ((response.data.count) > 0){
                    source = response.data.source!!
                    areaList = response.data.areaList!!
                    dataList = response.data.TaskForList
                    var statList: MutableList<String> = ArrayList()
                    for (i in response.data.TaskForList) {
                        statList.add(i.respValue!!)
                    }

                    // binding.spBeatName.setItem(statList)
                    //binding.spBeatName.setSelection(0)

                    val adapter2 = context?.let {
                        ArrayAdapter(
                            it,
                            android.R.layout.simple_spinner_dropdown_item, statList
                        )
                    }
                    binding.spinner.adapter = null

                    binding.spinner.adapter = adapter2
                    binding.spinner.setSelection(0, false)
                    binding.spinner.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                adapterView: AdapterView<*>,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                //   if ( binding.spinner.tag == 0){
                                binding.spinner.tag = 1
                                //binding.etHigherLocation.setText(dataList[position].respValue)
                                taskLevel = dataList[position].taskLevel!!
                                Handler().postDelayed({
                                    callApi2(dataList[position].taskLevel!!)
                                }, 1000)
                                //    }


                            }

                            override fun onNothingSelected(adapterView: AdapterView<*>) {}
                        }

                    //  taskLevel = dataList[0].taskLevel!!
                    // callApi2(dataList[0].taskLevel!!)
                }
                else{
                showToastMessage("No data found")
            }
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
                locationList.toMutableList().clear()
                locationList  = response.data.LocationList[0]
                var statList: MutableList<String> = ArrayList()
                for (i in locationList){
                    statList.add(i.locValue!!)
                }

                val adapter2 = context?.let {
                    ArrayAdapter(
                        it,
                        android.R.layout.simple_spinner_dropdown_item, statList)
                }
                binding.spinner2.adapter = null
                binding.spinner2.adapter = adapter2
                binding.spinner2.setSelection(0,false)
                binding.spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(adapterView: AdapterView<*>, view: View?, position: Int, id: Long) {
                        //binding.etHigherLocation.setText( locationList[position].locValue)
                        areaList = locationList[position].locId!!
                        Handler().postDelayed({
                            callApi3(taskLevel,locationList[position].locId!!)
                        }, 1000)

                    }
                    override fun onNothingSelected(adapterView: AdapterView<*>) {}
                }
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
            //AssignToListRequestParams( SharedPreferenceUtils.getLoggedInUserId(context as Context),"R","R","5")
        )
        responseCall.enqueue(getAssignToListResponse as Callback<AssignToListResponse>)

    }
    private  var personList : List<AssigntoList> = emptyList<AssigntoList>()
    private val getAssignToListResponse = object : NetworkCallBack<AssignToListResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<AssignToListResponse>) {
            response.data?.status?.let { status ->

                hide()
                if (response.data.AssignToList != null){
                personList  = response.data.AssignToList
                var statList: MutableList<String> = ArrayList()
                for (i in personList){
                    statList.add(i.UM_Name!!)
                }
                val adapter2 = context?.let {
                    ArrayAdapter(
                        it,
                        android.R.layout.simple_spinner_dropdown_item, statList)
                }
                binding.spinner3.adapter = null
                binding.spinner3.adapter = adapter2
                binding.spinner3.setSelection(0,false)}
                else{
                    showToastMessage("No Data found")
                }




            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()
        }

    }

    private fun moveToBeatTarget(){

        val navDirection =  AssignBeatToLocationDirections.actionAssignBeatToLocationToAssignTargetToBeat(startDate,endDate)
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
