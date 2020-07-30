package com.velectico.rbm.beats.views

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import com.kaopiz.kprogresshud.KProgressHUD

import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.adapters.BeatReportListAdapter
import com.velectico.rbm.beats.model.*
import com.velectico.rbm.complaint.model.ComplaintListRequestParams
import com.velectico.rbm.databinding.FragmentBeatReportListBinding
import com.velectico.rbm.databinding.FragmentTeamPerformanceDetailsBinding
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.teamlist.adapter.TeamPerformanceDetailsAdapter
import com.velectico.rbm.teamlist.model.TeamPerformanceModel
import com.velectico.rbm.utils.DateUtility
import com.velectico.rbm.utils.DateUtils
import com.velectico.rbm.utils.SharedPreferenceUtils
import kotlinx.android.synthetic.main.fragment_beat_report.view.*
import retrofit2.Callback
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class BeatReportListFragment : BaseFragment() , DatePickerDialog.OnDateSetListener {

    private lateinit var binding: FragmentBeatReportListBinding
    private var cuurentDatePicketParentView : com.google.android.material.textfield.TextInputEditText? = null;
    private var reportList : List<BeatReportListDetails> = emptyList()
    private lateinit var adapter: BeatReportListAdapter
    var taskDetails = BeatTaskDetails()
    var dlrDtl = DealerDetails()
    var startDate = "2020-07-28"
    var enddate = "2020-07-31"

    override fun getLayout(): Int {
        return R.layout.fragment_beat_report_list
    }


    override fun init(binding: ViewDataBinding) {

        this.binding = binding as FragmentBeatReportListBinding
       // startDate = ""
       // enddate = ""
        //reportList = BeatReport().getDummyBeatComList()
        taskDetails = arguments!!.get("taskDetail") as BeatTaskDetails
        dlrDtl = arguments!!.get("dealerDetails") as DealerDetails
        callApiBeatReportList(startDate,enddate)
        setUpRecyclerView()
        binding.paymentFromEt?.setOnClickListener {
            cuurentDatePicketParentView = this.binding.paymentFromEt;
            showDatePickerDialog()
        }
        binding.leaveFromEt?.setOnClickListener {
            cuurentDatePicketParentView = this.binding.leaveFromEt;
            showDatePickerDialog1()
        }
        binding.searchBtn?.setOnClickListener {
            if (binding.paymentFromEt.text.toString().isEmpty() || binding.leaveFromEt.text.toString().isEmpty() ){
                Toast.makeText(activity,"Date is Manadatory", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            var inputformat =  SimpleDateFormat(DateUtility.dd_MM_yy, Locale.US);
            var  outputformat =  SimpleDateFormat("yyyy-MM-dd", Locale.US);
            startDate = DateUtils.parseDate(binding.paymentFromEt.text.toString(),inputformat,outputformat)
            enddate = DateUtils.parseDate(binding.leaveFromEt.text.toString(),inputformat,outputformat)
            callApiBeatReportList(startDate,enddate)
        }


    }
    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val dayOfMonth = calendar[Calendar.DAY_OF_MONTH]
        DatePickerDialog(requireActivity(), this, year, month, dayOfMonth).show()
    }
    private fun showDatePickerDialog1() {
        val calendar = Calendar.getInstance()
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val dayOfMonth = calendar[Calendar.DAY_OF_MONTH]
        DatePickerDialog(requireActivity(), this, year, month, dayOfMonth).show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val tempDate: Date = DateUtility.getDateFromYearMonthDay(year, month, dayOfMonth)
        val subDateString: String =
            DateUtility.getStringDateFromTimestamp((tempDate.time), DateUtility.dd_MM_yy)
        if( cuurentDatePicketParentView == binding.paymentFromEt) {
            binding.paymentFromEt?.setText(subDateString)
        }
        else if ( cuurentDatePicketParentView == binding.leaveFromEt) {
            binding.leaveFromEt?.setText(subDateString)
        }

    }


    private fun setUpRecyclerView() {
        adapter = BeatReportListAdapter();
        binding.rvBeatComplaintList.adapter = adapter
        adapter.teamList = reportList
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
    fun callApiBeatReportList(date1:String,date2:String){
        showHud()

        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getBeatReportList(
            //BeatAllOrderListRequestParams("7001507620","61")

            BeatReportListRequestParams(SharedPreferenceUtils.getLoggedInUserId(context as Context),taskDetails.taskId.toString(),taskDetails.dealerId.toString(),taskDetails.distribId.toString(),date1,date2)

            //BeatReportListRequestParams("7001507620","109","61","0","2020-07-26","2020-07-26")
        )
        responseCall.enqueue(BeatReportListDetailsResponse as Callback<BeatReportListDetailsResponse>)
    }

    private val BeatReportListDetailsResponse = object : NetworkCallBack<BeatReportListDetailsResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<BeatReportListDetailsResponse>) {
            hide()
            response.data?.status?.let { status ->
                Log.e("test444","OrderHistoryDetailsResponse status="+response.data)
                reportList.toMutableList().clear()
                if (response.data.count > 0){
                    reportList = response.data.BeatReportList!!.toMutableList()
                    setUpRecyclerView()
                }
                else{
                    showToastMessage("No data found")
                }

            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            // hide()
        }

    }

}
