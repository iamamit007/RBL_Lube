package com.velectico.rbm.leave.view

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AlertDialog
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.google.android.material.textfield.TextInputEditText
import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseActivity
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.databinding.DefaultFragmentBinding
import com.velectico.rbm.databinding.FragmentApplyLeaveBinding
import com.velectico.rbm.expense.views.CreateExpenseFragmentDirections
import com.velectico.rbm.leave.model.ApplyLeaveRequest
import com.velectico.rbm.leave.model.LeaveListModel
import com.velectico.rbm.leave.model.LeaveReason
import com.velectico.rbm.leave.model.LeaveReasonResponse
import com.velectico.rbm.leave.viewmodel.LeaveViewModel
import com.velectico.rbm.masterdata.model.MasterDataItem
import com.velectico.rbm.masterdata.model.MasterDataResponse
import com.velectico.rbm.menuitems.viewmodel.MenuViewModel
import com.velectico.rbm.utils.*
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
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

    override fun getLayout(): Int {
        return R.layout.fragment_apply_leave
    }

    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentApplyLeaveBinding
        menuViewModel = MenuViewModel.getInstance(activity as BaseActivity)
        observeViewModelData()
        getRoleWiseView(menuViewModel.loginResponse.value?.userDetails?.get(0)?.uMRole.toString(),binding)
        setUp()
        getLeaveListFromServer()
    }

    private fun setUp() {
        binding.leaveFromEt.setOnClickListener(this)
        binding.leaveToEt.setOnClickListener(this)
        binding.applyBtn.setOnClickListener(this)

        binding.leaveNameEt.setOnClickListener {
            binding.spLeaveReason.performClick()
        }

        arguments?.let {
            flow = ApplyLeaveFragmentArgs.fromBundle(it).flow
            val position = ApplyLeaveFragmentArgs.fromBundle(it).item
            if(flow == LeaveListFragment.EDIT){
                leaveID = leaveViewModel.leaveListData[position.toInt()].leaveID
                binding.leaveNameEt.setText(leaveViewModel.leaveListData[position.toInt()].leaveName)
                binding.leaveCommentsEt.setText(leaveViewModel.leaveListData[position.toInt()].LD_Other_Reason)
                binding.leaveFromEt.setText(leaveViewModel.leaveListData[position.toInt()].leaveFrom)
                binding.leaveToEt.setText(leaveViewModel.leaveListData[position.toInt()].leaveTo)
                selectedLeaveReason = LeaveReason(
                    DD_Dropdown_Val = leaveViewModel.leaveListData[position.toInt()].leaveName as String,
                    DD_ID = leaveViewModel.leaveListData[position.toInt()].leaveReasonId)
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

                }
                applyLeaveFromServer()

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
        if(selectedLeaveReason==null){
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
            leaveViewModel.applyLeaveAPICall(
                ApplyLeaveRequest(leaveFromDate = fromDate,
                    leaveReasonId =  selectedLeaveReason?.DD_ID?.toInt() as Int,
                    leaveReasonOther = binding.leaveCommentsEt.text.toString(),
                    leaveToDate = toDate,
                    userId = userId,
                    leaveId = leaveID
                ),isEdit
            )
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
                binding.approveBtn.visibility = View.GONE
                binding.rejectBtn.visibility = View.GONE
            }
            SALES_PERSON_ROLE -> {
                binding.approveBtn.visibility = View.GONE
                binding.rejectBtn.visibility = View.GONE
            }
            DISTRIBUTER_ROLE -> {
                binding.applyBtn.visibility = View.GONE
                binding.approveBtn.visibility = View.VISIBLE
                binding.rejectBtn.visibility = View.VISIBLE
            }


        }
        binding.applyBtn.visibility = View.GONE
        binding.approveBtn.visibility = View.VISIBLE
        binding.rejectBtn.visibility = View.VISIBLE

    }
}