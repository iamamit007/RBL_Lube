package com.velectico.rbm.leave.view

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.google.android.material.textfield.TextInputEditText
import com.kaopiz.kprogresshud.KProgressHUD
import com.velectico.rbm.R
import com.velectico.rbm.RBMLubricantsApplication
import com.velectico.rbm.base.views.BaseActivity
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.model.*
import com.velectico.rbm.databinding.FragmentApplyLeaveBinding
import com.velectico.rbm.leave.model.*
import com.velectico.rbm.leave.viewmodel.LeaveViewModel
import com.velectico.rbm.menuitems.viewmodel.MenuViewModel
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.utils.*
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import retrofit2.Callback
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by mymacbookpro on 2020-04-26
 * TODO: Add a class header comment!
 */
class ApplyLeaveFragment : BaseFragment(), View.OnClickListener, DatePickerDialog.OnDateSetListener{
    private lateinit var binding:FragmentApplyLeaveBinding
    private lateinit var leaveViewModel: LeaveViewModel
    private var currentDatePicketParentView : TextInputEditText? = null
    private var leaveReasons: List<LeaveReason> = ArrayList<LeaveReason>()
    private var selectedLeaveReason : LeaveReason?=null
    private var flow : String?= null
    private var leaveID : String = ""
    private lateinit var menuViewModel: MenuViewModel


//    companion object{
//        var LeaveListModel:LeaveListModel? = null
//    }

    override fun getLayout(): Int {
        return R.layout.fragment_apply_leave
    }

    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentApplyLeaveBinding
        menuViewModel = MenuViewModel.getInstance(activity as BaseActivity)
        binding.tvProdCode.text = menuViewModel.loginResponse.value?.userDashboardDetails?.noOfDaysPresent.toString()
        binding.tvProdCat.text = menuViewModel.loginResponse.value?.userDashboardDetails?.noOfDaysAbsent.toString()
        observeViewModelData()
        getRoleWiseView(menuViewModel.loginResponse.value?.userDetails?.get(0)?.uMRole.toString(),binding)
        setUp()
        initHud()
        //getLeaveListFromServer()
        //showToastMessage(RBMLubricantsApplication.globalRole)
        if (RBMLubricantsApplication.globalRole == "Team" ){
            binding.approveBtn.visibility = View.VISIBLE
            binding.rejectBtn.visibility = View.VISIBLE
            binding.applyBtn.visibility = View.GONE
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
    fun initHud(){
        hud =  KProgressHUD.create(activity)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel("Please wait")
            .setCancellable(true)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)
    }

    override fun onResume() {
        super.onResume()
        callApi("Leave Reason")
    }

    fun callApi(type:String){
        // DealerDetailsRequestParams(
        //            SharedPreferenceUtils.getLoggedInUserId(context as Context),"109","61","0")
        showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getOrdervsQualityList(
            OrderVSQualityRequestParams(type)
        )
        responseCall.enqueue(segmentResponseResponse as Callback<OrderVSQualityResponse>)

    }

    var reasonId:String = ""
    var segdataList : List<DropdownDetails> = emptyList<DropdownDetails>()
    val segmentResponseResponse = object : NetworkCallBack<OrderVSQualityResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<OrderVSQualityResponse>) {
            response.data?.status?.let { status ->

                hide()
                segdataList  = response.data.BeatReportList
                var statList: MutableList<String> = java.util.ArrayList()
                for (i in segdataList){
                    statList.add(i.Exp_Head_Name!!)
                }
                val adapter2 = context?.let {
                    ArrayAdapter(
                        it,
                        android.R.layout.simple_spinner_dropdown_item, statList)
                }
                binding.spLeaveReason.adapter = adapter2
                binding.spLeaveReason.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(adapterView: AdapterView<*>, view: View?, position: Int, id: Long) {
                        if (segdataList.size > 0 ){
                            val x = segdataList[position]
                            reasonId = x.Exp_Head_Id!!
                            binding.leaveNameEt.setText(x.Exp_Head_Name)

                        }
                    }

                    override fun onNothingSelected(adapterView: AdapterView<*>) {}
                }

            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()


        }

    }
    fun callCreateApi(){
        // DealerDetailsRequestParams(
        //            SharedPreferenceUtils.getLoggedInUserId(context as Context),"109","61","0")
        showHud()
        val userId = if (RBMLubricantsApplication.globalRole == "Team" ){
            GloblalDataRepository.getInstance().teamUserId
        }
        else{
            SharedPreferenceUtils.getLoggedInUserId(context as Context)
        }
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.createLeave(
            ApplyLeaveRequest(reasonId, userId,binding.leaveFromEt.text.toString(),binding.leaveToEt.text.toString(),binding.leaveCommentsEt.text.toString(),"")
        )
        responseCall.enqueue(createResponse as Callback<ApplyLeaveResponse>)

    }

    fun callACCEPEREJECT(status:String){
        // DealerDetailsRequestParams(
        //            SharedPreferenceUtils.getLoggedInUserId(context as Context),"109","61","0")
        showHud()
        val userId = if (RBMLubricantsApplication.globalRole == "Team" ){
            GloblalDataRepository.getInstance().teamUserId
        }
        else{
            SharedPreferenceUtils.getLoggedInUserId(context as Context)
        }
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        Log.d("VVVVV",""+ ApplyLeaveRequest(reasonId, userId,binding.leaveFromEt.text.toString(),binding.leaveToEt.text.toString(),binding.leaveCommentsEt.text.toString(),leaveID)
        )
        val responseCall = apiInterface.accepeRejectLeave(
            ApproveRejectLeaveListRequest(SharedPreferenceUtils.getLoggedInUserId(context as Context),status,leaveID)
        )
        responseCall.enqueue(createResponse as Callback<ApplyLeaveResponse>)

    }



    fun callUpdateApi(){
        // DealerDetailsRequestParams(
        //            SharedPreferenceUtils.getLoggedInUserId(context as Context),"109","61","0")
        showHud()
        val userId = if (RBMLubricantsApplication.globalRole == "Team" ){
            GloblalDataRepository.getInstance().teamUserId
        }
        else{
            SharedPreferenceUtils.getLoggedInUserId(context as Context)
        }
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.createLeave(
            ApplyLeaveRequest(reasonId, userId,binding.leaveFromEt.text.toString(),binding.leaveToEt.text.toString(),binding.leaveCommentsEt.text.toString(),"")
        )
        responseCall.enqueue(createResponse as Callback<ApplyLeaveResponse>)

    }


    val createResponse = object : NetworkCallBack<ApplyLeaveResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<ApplyLeaveResponse>) {
            response.data?.status?.let { status ->

                hide()
              showToastMessage(response.data.respMessage)
                activity!!.onBackPressed()

            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()


        }

    }


    private fun setUp() {
        binding.leaveFromEt.setOnClickListener(this)
        binding.leaveToEt.setOnClickListener(this)
        binding.applyBtn.setOnClickListener(this)
        binding.approveBtn.setOnClickListener(this)
        binding.rejectBtn.setOnClickListener(this)

        binding.leaveNameEt.setOnClickListener {
            binding.spLeaveReason.performClick()
        }

        arguments?.let {
            flow = ApplyLeaveFragmentArgs.fromBundle(it).flow
            val position = ApplyLeaveFragmentArgs.fromBundle(it).item
            if(flow == LeaveListFragment.EDIT){
                val model = GloblalDataRepository.getInstance().leaveListModel
                leaveID = model.leaveID
                reasonId = model.leaveReasonId
                binding.leaveNameEt.setText(model.leaveName)
                binding.leaveCommentsEt.setText(model.LD_Other_Reason)
                val inpFormat =  SimpleDateFormat("yyyy-MM-dd", Locale.US);
                val outputformat =  SimpleDateFormat("dd-MMM-yy", Locale.US);
                val stdate =  DateUtils.parseDate(model.leaveFrom,inpFormat,outputformat)
                val endate =  DateUtils.parseDate(model.leaveTo,inpFormat,outputformat)
                binding.leaveFromEt.setText(stdate)
                binding.leaveToEt.setText(endate)
//                selectedLeaveReason = LeaveReason(
//                    DD_Dropdown_Val = leaveViewModel.leaveListData[position.toInt()].leaveName as String,
//                    DD_ID = leaveViewModel.leaveListData[position.toInt()].leaveReasonId)
            }
        }
    }

    private fun showCustomDatePicker(minStartDate:String = ""){
        var now = DateUtility.dateStrToCalendar(minStartDate)
        val year = now[Calendar.YEAR]
        val dpd: DatePickerDialog = DatePickerDialog.newInstance(
            this, year,  // Initial year selection
            now[Calendar.MONTH],  // Initial month selection
            now[Calendar.DAY_OF_MONTH] // Inital day selection
        )
        val calenderMaxDate = Calendar.getInstance()
        calenderMaxDate[Calendar.YEAR] = year + 1
        dpd.minDate = now
        dpd.maxDate = calenderMaxDate
        fragmentManager?.let {
            dpd.show(it, DATE_PICKER)
        }
        val holidays = arrayOf("20-05-2020", "21-05-2020", "25-05-2020")
        val disabledDays = DateUtility.getDisabledDatesArr(holidays).toTypedArray()
        dpd.highlightedDays = disabledDays
        dpd.disabledDays = disabledDays
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.leaveFromEt -> { currentDatePicketParentView = this.binding.leaveFromEt;
                showCustomDatePicker(binding.leaveFromEt.text.toString());
            }
            R.id.leaveToEt -> { currentDatePicketParentView = this.binding.leaveToEt;
                showCustomDatePicker(binding.leaveFromEt.text.toString());
            }
            R.id.applyBtn -> {
                if(flow == LeaveListFragment.EDIT){
                    callUpdateApi()
                }else{
                    applyLeaveFromServer()
                }

            }
            R.id.approveBtn ->{

                callACCEPEREJECT("A")

            }
            R.id.rejectBtn ->{
                callACCEPEREJECT("A")


            }
        }
    }


    private fun observeViewModelData() {
        leaveViewModel = LeaveViewModel.getInstance(activity as BaseActivity)
        leaveViewModel.leaveReasonResponse.observe(viewLifecycleOwner, Observer { listResponse ->
            listResponse?.let {
                leaveReasons = listResponse.leaveReasons
                initLeaveTypeSpinner()
            }
        })

        leaveViewModel.applyLeaveResponse.observe(viewLifecycleOwner, Observer { listResponse ->
            listResponse?.let {
                showAlertDialog(listResponse.respMessage)
            }
        })

        leaveViewModel.loading.observe(viewLifecycleOwner, Observer { progress ->

            binding?.progressLayout?.visibility = if(progress) View.VISIBLE else View.GONE
        })

        leaveViewModel.errorLiveData.observe(viewLifecycleOwner, Observer {
            (activity as BaseActivity).showAlertDialog(it.errorMessage ?: getString(R.string.no_data_available))
        })
    }

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val tempDate: Date = DateUtility.getDateFromYearMonthDay(year, monthOfYear, dayOfMonth)
        val subDateString: String = DateUtility.getStringDateFromTimestamp((tempDate.time), DateUtility.YYYY_DASH_MM_DASH_DD)
        currentDatePicketParentView?.setText(subDateString)
    }

    companion object{
        const val TAG = "ApplyLeaveFragment"
        const val DATE_PICKER = "DatePickerDialog"
    }

    private fun initLeaveTypeSpinner() {
        //https://github.com/Chivorns/SmartMaterialSpinner
        var leaveTypeListStringArray: MutableList<String> = ArrayList()
        for (leaveType in this.leaveReasons){
            leaveTypeListStringArray.add(leaveType.DD_Dropdown_Val)
        }
        binding.spLeaveReason.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View?, position: Int, id: Long) {
                binding.leaveNameEt.setText( leaveTypeListStringArray[position])
                selectedLeaveReason = leaveReasons[position]
            }
            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }
        binding.spLeaveReason.setItem(leaveTypeListStringArray)
    }

    private fun applyLeaveFromServer(){
        val fromDate = binding.leaveFromEt.text.toString()?.trim();
        val toDate = binding.leaveToEt.text.toString()?.trim();
        if(reasonId.isEmpty()){
            showToastMessage("Please select leave reason")
        }
        else if(binding.leaveFromEt==null || fromDate == ""){
            showToastMessage("Please select from date")
        }
        else if(binding.leaveToEt==null || toDate == ""){
            showToastMessage("Please select to date")
        }
        else{
            binding?.progressLayout?.visibility = View.VISIBLE
            val userId : String = SharedPreferenceUtils.getLoggedInUserId(context as Context)
            val isEdit = flow == LeaveListFragment.EDIT
            callCreateApi()


        }
    }

    private fun getLeaveListFromServer() {
        binding?.progressLayout?.visibility = View.VISIBLE
        leaveViewModel.getLeaveReasonAPICall()
    }

    private fun showAlertDialog(msg:String?){
        val dialog = AlertDialog.Builder(context as Context)
        dialog.setTitle(getString(R.string.alert_title))
            .setMessage(msg)
            .setCancelable(false)
            .setNegativeButton(getString(R.string.ok_button)) {d, _ ->
                d.dismiss()
                activity?.onBackPressed()
            }
            .show()
    }

    private fun getRoleWiseView(uRole:String,binding: FragmentApplyLeaveBinding) {

        when (uRole) {

            SALES_LEAD_ROLE -> {

                binding.applyBtn.visibility = View.VISIBLE
            }
            SALES_PERSON_ROLE -> {

                binding.applyBtn.visibility = View.VISIBLE
            }



        }


    }
}