package com.velectico.rbm.beats.views

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.navigation.Navigation
import com.kaopiz.kprogresshud.KProgressHUD

import com.velectico.rbm.R
import com.velectico.rbm.RBMLubricantsApplication
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.model.*
import com.velectico.rbm.complaint.model.ComplainListDetails
import com.velectico.rbm.databinding.FragmentBeatTaskDealerDetailsBinding
import com.velectico.rbm.databinding.FragmentOrderListBinding
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.order.model.OrderHead
import com.velectico.rbm.order.views.OrderListFragmentDirections
import com.velectico.rbm.utils.SharedPreferenceUtils
import kotlinx.android.synthetic.main.fragment_beat_report.view.*
import retrofit2.Callback

/**
 * A simple [Fragment] subclass.
 */
class BeatTaskDealerDetailsFragment : BaseFragment() {

    private lateinit var binding: FragmentBeatTaskDealerDetailsBinding
    var taskDetails = BeatTaskDetails()
    var dealerDetails = DealerDetails()
    private var complaintList = ComplainListDetails()
    override fun getLayout(): Int {
        return R.layout.fragment_beat_task_dealer_details
    }


    override fun init(binding: ViewDataBinding) {
        checkPermission()
        taskDetails = arguments!!.get("beatTaskDetails") as BeatTaskDetails
        //showToastMessage(arguments.)
        this.binding = binding as FragmentBeatTaskDealerDetailsBinding
        if (RBMLubricantsApplication.globalRole == "Team" ){
            binding.btnNewOrder.visibility = View.INVISIBLE
            binding.btnComplaints.visibility = View.INVISIBLE
            binding.btnBeatReport.visibility = View.INVISIBLE
        }
        binding.btnPerformanceHistory.setOnClickListener {
            moveToPerformanceHistory()
        }

        binding.btnComplaints.setOnClickListener {
            moveToCreateComplaint()
        }

        binding.btnBeatReport.setOnClickListener {
            moveToBeatReport()
        }

        binding.btnNewOrder.setOnClickListener {
            moveToCreateOrder()
        }

        binding.btnOrderHistory.setOnClickListener {
            moveToOrderHistory()
        }
        binding.viewAllComplaintslayOut.setOnClickListener {
            moveToViewAllComplaints()
        }

        binding.viewAllTransBtn.setOnClickListener {
            moveToBeatPayAndTrans()
        }

        binding.allBeatReport.setOnClickListener {
            moveToAllBeatReport()
        }
        binding.tvDelearCallNo.setOnClickListener {
            callUser()
        }
    }

    private fun moveToPerformanceHistory(){
        val navDirection =  BeatTaskDealerDetailsFragmentDirections.actionBeatTaskDealerDetailsFragmentToBeatPerformanceHistory()
        Navigation.findNavController(binding.btnPerformanceHistory).navigate(navDirection)
    }

    private fun moveToCreateComplaint(){
        val navDirection =  BeatTaskDealerDetailsFragmentDirections.actionBeatTaskDealerDetailsFragmentToCreateComplaints(taskDetails,dealerDetails,complaintList)
        Navigation.findNavController(binding.btnComplaints).navigate(navDirection)
    }

    private fun moveToBeatReport(){
        val navDirection =  BeatTaskDealerDetailsFragmentDirections.actionBeatTaskDealerDetailsFragmentToBeatReportFragment(taskDetails,dealerDetails)
        Navigation.findNavController(binding.btnBeatReport).navigate(navDirection)
    }

    private fun moveToCreateOrder(){
        val navDirection =  BeatTaskDealerDetailsFragmentDirections.actionBeatTaskDealerDetailsFragmentToProductFilterFragment()
        Navigation.findNavController(binding.btnNewOrder).navigate(navDirection)
    }

    private fun moveToOrderHistory(){
        val navDirection =  BeatTaskDealerDetailsFragmentDirections.actionBeatTaskDealerDetailsFragmentToOrderListFragment(taskDetails)
        Navigation.findNavController(binding.btnOrderHistory).navigate(navDirection)
    }
    private fun moveToViewAllComplaints(){
        val navDirection =  BeatTaskDealerDetailsFragmentDirections.actionBeatTaskDealerDetailsFragmentToBeatSpecificComplaintList(taskDetails,dealerDetails)
        Navigation.findNavController(binding.viewAllComplaintslayOut).navigate(navDirection)
    }

    private fun moveToBeatPayAndTrans(){
        val navDirection =  BeatTaskDealerDetailsFragmentDirections.actionBeatTaskDealerDetailsFragmentToBeatPaymentListFragment()
        Navigation.findNavController(binding.viewAllTransBtn).navigate(navDirection)
    }

    private fun moveToAllBeatReport(){
        val navDirection =  BeatTaskDealerDetailsFragmentDirections.actionBeatTaskDealerDetailsFragmentToBeatReportListFragment(taskDetails,dealerDetails)
        Navigation.findNavController(binding.allBeatReport).navigate(navDirection)
    }
    private fun checkPermission(){
        val checkSelfPermission = ContextCompat.checkSelfPermission(baseActivity, android.Manifest.permission.CALL_PHONE)
        if (checkSelfPermission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(baseActivity, arrayOf(android.Manifest.permission.CAMERA), 1)
        }
    }

    override fun onResume() {
        super.onResume()
        val arg  = arguments!!.get("beatTaskDetails") as BeatTaskDetails
        callDealerDetails(arg)
    }

    private fun callUser(){
        val u = Uri.parse("tel:" + "919836256985")

        val i = Intent(Intent.ACTION_DIAL, u)

        try {
            // Launch the Phone app's dialer with a phone
            // number to dial a call.
            startActivity(i)
        } catch (s: SecurityException) {
            // show() method display the toast with
            // exception message.
            Log.e("Error::","error while opening call");
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
    fun callDealerDetails(arg:BeatTaskDetails){
       // showHud() DealerDetailsRequestParams(
        //            SharedPreferenceUtils.getLoggedInUserId(context as Context),"109","61","0")
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getDealerDetailsByBeat(
            DealerDetailsRequestParams(
            SharedPreferenceUtils.getLoggedInUserId(context as Context),arg.taskId,arg.dealerId,arg.distribId!!)
        )
        responseCall.enqueue(beatTaskDetailsListResponse as Callback<DealerDetailsResponse>)
    }
    private val beatTaskDetailsListResponse = object : NetworkCallBack<DealerDetailsResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<DealerDetailsResponse>) {
            response.data?.status?.let { status ->
                if (response.data.count!! > 0){

               // activity!!.runOnUiThread(java.lang.Runnable {
                    showToastMessage("Data has been loaded successfully!!")
                    Log.e("test222","BeatTaskDetailsListResponse status="+response.data)
                    binding.dealerDetails = response.data.scheduleDates[0]
                    dealerDetails = response.data.scheduleDates[0]
                    binding.actAmtVal.text = response.data.actualCollectionAmt
                    binding.tarAmtVal.text = response.data.scheduleDates[0].collectionAmt
                    binding.actQtyVal.text = response.data.actualOrderAmt
                    binding.tarQtyVal.text = response.data.scheduleDates[0].orderAmt
                    binding.dealerName.text = taskDetails.dealerName.toString()
                    if (taskDetails.distribName != null){
                        binding.gradeval.text = taskDetails.distribGrade
                        binding.type.text = "Distributor"
                    }
                    else{
                        binding.gradeval.text = taskDetails.dealerGrade
                        binding.type.text = "Dealer"
                    }
                    hide()
                }
                else{
                    showToastMessage("no data found!!")
                }


            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()
        }

    }

}
