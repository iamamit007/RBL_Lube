package com.velectico.rbm.beats.views

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.velectico.rbm.utils.SharedPreferenceUtils
import retrofit2.Callback

/**
 * A simple [Fragment] subclass.
 */
class BeatReportListFragment : BaseFragment()  {

    private lateinit var binding: FragmentBeatReportListBinding
    private var reportList : List<BeatReportListDetails> = emptyList()
    private lateinit var adapter: BeatReportListAdapter
    var taskDetails = BeatTaskDetails()
    var dlrDtl = DealerDetails()
    override fun getLayout(): Int {
        return R.layout.fragment_beat_report_list
    }


    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentBeatReportListBinding
        //reportList = BeatReport().getDummyBeatComList()
        taskDetails = arguments!!.get("taskDetail") as BeatTaskDetails
        dlrDtl = arguments!!.get("dealerDetails") as DealerDetails
        callApiBeatReportList()
        setUpRecyclerView()

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
    fun callApiBeatReportList(){
        showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getBeatReportList(
            //BeatAllOrderListRequestParams("7001507620","61")
            BeatReportListRequestParams(SharedPreferenceUtils.getLoggedInUserId(context as Context),taskDetails.taskId.toString(),taskDetails.dealerId.toString(),taskDetails.distribId.toString(),"2020-07-28","2020-07-31")

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
