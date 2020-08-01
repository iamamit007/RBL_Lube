package com.velectico.rbm.beats.views

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.chivorn.smartmaterialspinner.SmartMaterialSpinner
import com.google.gson.annotations.SerializedName
import com.kaopiz.kprogresshud.KProgressHUD
import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.model.*
import com.velectico.rbm.beats.viewmodel.BeatSharedViewModel
import com.velectico.rbm.databinding.FragmentCreateBeatBinding
import com.velectico.rbm.expense.viewmodel.ExpenseViewModel
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.order.views.OrderListFragmentDirections
import com.velectico.rbm.utils.DateUtility
import com.velectico.rbm.utils.DateUtils
import com.velectico.rbm.utils.GloblalDataRepository
import com.velectico.rbm.utils.SharedPreferenceUtils
import kotlinx.android.synthetic.main.fragment_beat_report.view.*
import retrofit2.Callback
import java.text.SimpleDateFormat
import java.util.*

/**
 * UI to create Beats
 */
class CreateBeatFragment : BaseFragment() , OnDateSetListener {
    private lateinit var binding: FragmentCreateBeatBinding;
    private var cuurentDatePicketParentView : com.google.android.material.textfield.TextInputEditText? = null;
    private lateinit var mBeatSharedViewModel: BeatSharedViewModel
    private lateinit var mAssignments : MutableList<BeatAssignments>;
    var beatLevel = ""
    var masterId = ""


    override fun getLayout(): Int {
        return R.layout.fragment_create_beat
    }

    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentCreateBeatBinding;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBeatSharedViewModel = BeatSharedViewModel.getInstance(baseActivity)
        mBeatSharedViewModel.setBeat(Beats())
        mBeatSharedViewModel.beats.value?.beatAssignments = BeatAssignments().getBlankList()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addListeners();
        initSpinner();

    }

    fun addListeners(){
        //et_beat_name
        this.binding.etBeatName.setOnClickListener {
            this.binding.spBeatName.performClick()
        }
        this.binding.etAssignTo.setOnClickListener {
            this.binding.spSalesperson.performClick()
        }
        this.binding.etStartDate.setOnClickListener {
            cuurentDatePicketParentView = this.binding.etStartDate;
            showDatePickerDialog();
        }
        this.binding.etEndDate.setOnClickListener {
            cuurentDatePicketParentView = this.binding.etEndDate
            showDatePickerDialog();
        }
        this.binding.btnAssignTask.setOnClickListener{
            var errMsg = mBeatSharedViewModel.beats.value?.isValidaData();
            if(errMsg!=null){
                showToastMessage(errMsg)
            }
            else{
                callSubmit()
                // val action : CreateBeatFragmentDirections.Action = CreateBeatFragmentDirections.actionCreateBeatFragmentToAssignBeatToLocation(mBeatSharedViewModel.beats.value as Beats)
                // Navigation.findNavController(binding.btnAssignTask).navigate(action);






            }

        }

    }

    //callback function to set set once date is selected from datepicker
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val tempDate: Date = DateUtility.getDateFromYearMonthDay(year, month, dayOfMonth)
        val subDateString: String =
            DateUtility.getStringDateFromTimestamp((tempDate.time), DateUtility.dd_MM_yy)
        cuurentDatePicketParentView?.setText(subDateString)
        if( cuurentDatePicketParentView == binding.etStartDate){
            mBeatSharedViewModel.beats.value?.startDate = tempDate.time
        }
        else if(cuurentDatePicketParentView == binding.etEndDate){
            mBeatSharedViewModel.beats.value?.endDate = tempDate.time
        }
    }

    //Function to show date picker dialog box for start and end date
    fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val dayOfMonth = calendar[Calendar.DAY_OF_MONTH]
        DatePickerDialog(requireActivity(), this, year, month, dayOfMonth).show()
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
    private fun initSpinner() {
        hud =  KProgressHUD.create(activity)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel("Please wait")
            .setCancellable(true)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)
        //https://github.com/Chivorns/SmartMaterialSpinner
        var provinceList: MutableList<String> = ArrayList()
        provinceList.add("Region")
        provinceList.add("Zone")
        provinceList.add("District")
        provinceList.add("Area")

        binding.spSalesperson.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View?, position: Int, id: Long) {
                binding.etAssignTo.setText( dataList[position].BM_Beat_Name)
                mBeatSharedViewModel.beats.value?.salesPersonId = position.toString()
                mBeatSharedViewModel.beats.value?.salesPersonName = dataList[position].BM_Beat_Name
                masterId = dataList[position].BM_DM_ID!!

            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }

        binding.spBeatName.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View?, position: Int, id: Long) {
                binding.etBeatName.setText( provinceList[position])
                beatLevel = provinceList[position].get(0).toString().toUpperCase()
                callApi(type = beatLevel)
                mBeatSharedViewModel.beats.value?.beatId = position.toString()
                mBeatSharedViewModel.beats.value?.beatName =  provinceList[position]
            }
            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }

        binding.spBeatName.setItem(provinceList)

        //https://stackoverflow.com/questions/48343622/how-to-fix-parameter-specified-as-non-null-is-null-on-rotating-screen-in-a-fragm
       // binding.spSalesperson.setSelection(2)
    }


    fun callApi(type:String){
        showHud()
        // DealerDetailsRequestParams(
        //            SharedPreferenceUtils.getLoggedInUserId(context as Context),"109","61","0")
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getBeatDetailList(
            BeatDetailListRequestParams(SharedPreferenceUtils.getLoggedInUserId(context as Context),type)
        )
        responseCall.enqueue(orderVSQualityResponseResponse as Callback<BeatDetailListResponse>)

        }
    private  var dataList : List<BeatListDetails> = emptyList<BeatListDetails>()
    private val orderVSQualityResponseResponse = object : NetworkCallBack<BeatDetailListResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<BeatDetailListResponse>) {
            response.data?.status?.let { status ->

                  hide()
                dataList  = response.data.BeatList
                var statList: MutableList<String> = ArrayList()
                for (i in dataList){
                    statList.add(i.BM_Beat_Name!!)
                }

                binding.spSalesperson.setItem(statList)
                binding.spSalesperson.setSelection(0)

            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()
        }

    }


    fun callSubmit(){
        showHud()
        val inpFormat =  SimpleDateFormat(DateUtility.dd_MM_yy, Locale.US);
        val  outputformat =  SimpleDateFormat("yyyy-MM-dd", Locale.US);
        val stdate =  DateUtils.parseDate(binding.etStartDate.text.toString(),inpFormat,outputformat)
        val endate =  DateUtils.parseDate(binding.etEndDate.text.toString(),inpFormat,outputformat)
        showToastMessage(stdate)
        // DealerDetailsRequestParams(
        //            SharedPreferenceUtils.getLoggedInUserId(context as Context),"109","61","0")
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.createBeatSchedule(
            CreateBeatScheduleRequestParams(SharedPreferenceUtils.getLoggedInUserId(context as Context),masterId,stdate,endate)
           // CreateBeatScheduleRequestParams((SharedPreferenceUtils.getLoggedInUserId(context as Context),masterId,"2020-07-22","2020-07-25")
        )
        responseCall.enqueue(createBeatScheduleResponseResponse as Callback<CreateBeatReportResponse>)

    }
    private val createBeatScheduleResponseResponse = object : NetworkCallBack<CreateBeatReportResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<CreateBeatReportResponse>) {
            response.data?.respMessage?.let { status ->

                hide()
                showToastMessage( response.data?.respMessage!!)
                GloblalDataRepository.getInstance().scheduleId =   response.data?.beatScheduleId
                val navDirection =  CreateBeatFragmentDirections.actionCreateBeatFragmentToAssignBeatToLocation(binding.etStartDate.text.toString(),binding.etEndDate.text.toString())
                Navigation.findNavController(binding.btnAssignTask).navigate(navDirection)

            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()
        }

    }
    }


