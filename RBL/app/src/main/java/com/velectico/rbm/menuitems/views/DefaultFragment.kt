package com.velectico.rbm.menuitems.views

import android.content.Context
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.google.android.material.navigation.NavigationView
import com.kaopiz.kprogresshud.KProgressHUD
import com.velectico.rbm.base.views.BaseActivity
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.R
import com.velectico.rbm.RBMLubricantsApplication
import com.velectico.rbm.beats.model.BeatTaskDetails
import com.velectico.rbm.beats.model.CreateBeatReportResponse
import com.velectico.rbm.beats.model.CreateBeatScheduleRequestParams
import com.velectico.rbm.beats.views.CreateBeatFragmentDirections
import com.velectico.rbm.databinding.DefaultFragmentBinding
import com.velectico.rbm.menuitems.viewmodel.AttendancResponse
import com.velectico.rbm.menuitems.viewmodel.AttendanceRequestParams
import com.velectico.rbm.menuitems.viewmodel.MenuViewModel
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.utils.*
import retrofit2.Callback
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by mymacbookpro on 2020-04-26
 * TODO: Add a class header comment!
 */
class DefaultFragment : BaseFragment(){
    private lateinit var binding:DefaultFragmentBinding
    private lateinit var menuViewModel: MenuViewModel
    private lateinit var navigationView: NavigationView
    var taskDetails = BeatTaskDetails()
    override fun getLayout(): Int = R.layout.default_fragment

    override fun init(binding: ViewDataBinding) {
        setHasOptionsMenu(true)
        this.binding = binding as DefaultFragmentBinding

        val animationSwing = AnimationUtils.loadAnimation(activity as BaseActivity, R.anim.swing_up_left)

       /* binding.attendanceCard.startAnimation(animationSwing);
        binding.targetCard.startAnimation(animationSwing);
        binding.paymentCard.startAnimation(animationSwing);
        binding.incentiveCard.startAnimation(animationSwing);
        binding.beatCard.startAnimation(animationSwing);
        binding.expenseCard.startAnimation(animationSwing);*/



        menuViewModel = MenuViewModel.getInstance(activity as BaseActivity)
        printLogMessage(TAG, "init")


        getDashBoardPrivilege(menuViewModel.loginResponse.value?.userDetails?.get(0)?.uMRole.toString(),binding);






        menuViewModel.allResourcesResponse.observe(this, Observer { listResponse ->
            listResponse?.let {
                printLogMessage(TAG, it.data.toString())
            }
        })

        menuViewModel.allUsersResponse.observe(this, Observer { listResponse ->
            listResponse?.let {
                printLogMessage(TAG, it.data.toString())
            }
        })

        menuViewModel.loading.observe(this, Observer { progress ->
            binding.progressLayout.visibility = if(progress) View.VISIBLE else View.GONE
        })

//        menuViewModel.errorLiveData.observe(this, Observer {
//            (activity as BaseActivity).showAlertDialog(it.errorMessage ?: "No data available")
//        })

        binding.beatButton.setOnClickListener {
            RBMLubricantsApplication.fromBeat = "Beat"
            RBMLubricantsApplication.globalRole = ""
            val navDirection =  DefaultFragmentDirections.actionHomeFragmentToDateWiseBeatListFragment("")
            Navigation.findNavController(binding.beatButton).navigate(navDirection)
        }

        binding.orderButton.setOnClickListener {
            RBMLubricantsApplication.fromBeat = ""
            val navDirection =  DefaultFragmentDirections.actionHomeFragmentToOrderListFragment(taskDetails)
            Navigation.findNavController(binding.orderButton).navigate(navDirection)
        }

        binding.leaveButton.setOnClickListener {
            RBMLubricantsApplication.globalRole = ""
            val navDirection =  DefaultFragmentDirections.actionHomeFragmentToLeaveListFragment()
            Navigation.findNavController(binding.leaveButton).navigate(navDirection)
        }

        binding.reminderButton.setOnClickListener {
            val navDirection =  DefaultFragmentDirections.actionHomeFragmentToReminderListFragment()
            Navigation.findNavController(binding.reminderButton).navigate(navDirection)
        }

        binding.profileButton.setOnClickListener {
            val navDirection =  DefaultFragmentDirections.actionHomeFragmentToUserProfileFragment()
            Navigation.findNavController(binding.profileButton).navigate(navDirection)
        }

        binding.teamButton.setOnClickListener {
            RBMLubricantsApplication.globalRole = "Team"
            val navDirection =  DefaultFragmentDirections.actionHomeFragmentToTeamDashboard()
            Navigation.findNavController(binding.teamButton).navigate(navDirection)
        }

        binding.expenseButton.setOnClickListener {
            val navDirection =  DefaultFragmentDirections.actionHomeFragmentToExpenseListFragment()
            Navigation.findNavController(binding.expenseButton).navigate(navDirection)
        }

        binding.complainButton.setOnClickListener {
            val navDirection =  DefaultFragmentDirections.actionHomeFragmentToComplaintList()
            Navigation.findNavController(binding.complainButton).navigate(navDirection)
        }

        binding.profileButtonLong.setOnClickListener {
            val navDirection =  DefaultFragmentDirections.actionHomeFragmentToUserProfileFragment()
            Navigation.findNavController(binding.profileButtonLong).navigate(navDirection)
        }

        binding.complainMechButton.setOnClickListener {
            val navDirection =  DefaultFragmentDirections.actionHomeFragmentToComplaintList()
            Navigation.findNavController(binding.complainMechButton).navigate(navDirection)
        }
        binding.scanQRLong.setOnClickListener {
            showToastMessage("scan")
            val navDirection =  DefaultFragmentDirections.actionHomeFragmentToQrcodescanner()
            Navigation.findNavController(binding.scanQRLong).navigate(navDirection)
        }
        binding.redeemButton.setOnClickListener {
            showToastMessage("redeem")
            val navDirection =  DefaultFragmentDirections.actionHomeFragmentToRedeem()
            Navigation.findNavController(binding.redeemButton).navigate(navDirection)
        }
        binding.productButton.setOnClickListener {

            val navDirection =  DefaultFragmentDirections.actionHomeFragmentToProductFilterFragment()
            Navigation.findNavController(binding.productButton).navigate(navDirection)
        }
        binding.orderDealerButton.setOnClickListener {
            val navDirection =  DefaultFragmentDirections.actionHomeFragmentToOrderListFragment(taskDetails)
            Navigation.findNavController(binding.orderDealerButton).navigate(navDirection)
        }
        binding.paymentDealerButton.setOnClickListener {
            val navDirection =  DefaultFragmentDirections.actionHomeFragmentToFragmentPaymentList()
            Navigation.findNavController(binding.paymentDealerButton).navigate(navDirection)
        }
        binding.paymentButton.setOnClickListener {
            val navDirection =  DefaultFragmentDirections.actionHomeFragmentToFragmentPaymentList()
            Navigation.findNavController(binding.paymentButton).navigate(navDirection)
        }
        binding.performanceButtonLong.setOnClickListener {
            val navDirection =  DefaultFragmentDirections.actionHomeFragmentToTeamListFragment(binding.performanceButtonLong.text.toString())
            Navigation.findNavController(binding.performanceButtonLong).navigate(navDirection)
        }
        binding.orderButtonLong.setOnClickListener {
            val navDirection =  DefaultFragmentDirections.actionHomeFragmentToOrderListFragment(taskDetails)
            Navigation.findNavController(binding.orderButtonLong).navigate(navDirection)
        }
    }

    override
    fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if(menuViewModel.loginResponse.value?.userDetails?.get(0)?.uMRole.toString() == SALES_LEAD_ROLE || menuViewModel.loginResponse.value?.userDetails?.get(0)?.uMRole.toString() == SALES_PERSON_ROLE){
            inflater.inflate(R.menu.attendance_btn, menu)
        }
     //   inflater.inflate(R.menu.attendance_btn, menu)
        super.onCreateOptionsMenu(menu , inflater)
    }


    override
    fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_attandance){
            //callAttendance()
            showToastMessage("attendance")
        }
        return true
    }



    companion object{
        const val TAG = "DefaultFragment"
    }


    fun getDashBoardPrivilege(uRole:String,binding: DefaultFragmentBinding){

       when (uRole) {
            //when (TEMP_CURRENT_LOGGED_IN) {

            SALES_LEAD_ROLE->{
                binding.beatButton.visibility = View.VISIBLE
                binding.expenseButton.visibility = View.VISIBLE
                binding.orderButton.visibility = View.VISIBLE
                binding.leaveButton.visibility = View.VISIBLE
                binding.paymentButton.visibility = View.VISIBLE
                binding.complainButton.visibility = View.VISIBLE
                binding.dealerButton.visibility = View.VISIBLE
                binding.reminderButton.visibility = View.VISIBLE
                binding.profileButton.visibility = View.VISIBLE
                binding.teamButton.visibility = View.VISIBLE
                binding.profileButtonLong.visibility = View.GONE

            }
            SALES_PERSON_ROLE->{
                binding.beatButton.visibility = View.VISIBLE
                binding.expenseButton.visibility = View.VISIBLE
                binding.orderButton.visibility = View.VISIBLE
                binding.leaveButton.visibility = View.VISIBLE
                binding.paymentButton.visibility = View.VISIBLE
                binding.complainButton.visibility = View.VISIBLE
                binding.dealerButton.visibility = View.VISIBLE
                binding.reminderButton.visibility = View.VISIBLE
                binding.profileButton.visibility = View.GONE
                binding.teamButton.visibility = View.GONE
                binding.lay5.visibility = View.GONE
                binding.profileButtonLong.visibility = View.VISIBLE
            }
            DISTRIBUTER_ROLE->{
                binding.lay1.visibility = View.GONE
                binding.lay2.visibility = View.GONE
                binding.lay3.visibility = View.VISIBLE
                binding.lay4.visibility = View.VISIBLE
                binding.lay5.visibility = View.VISIBLE
                binding.lay6.visibility = View.GONE
                binding.lay7.visibility = View.GONE
                binding.lay8.visibility = View.GONE
                binding.lay9.visibility = View.GONE
                binding.lay10.visibility = View.GONE
                binding.lay11.visibility = View.VISIBLE

            }
            DEALER_ROLE->{
                binding.lay1.visibility = View.GONE
                binding.lay2.visibility = View.GONE
                binding.lay3.visibility = View.GONE
                binding.lay4.visibility = View.GONE
                binding.lay5.visibility = View.GONE
                binding.lay6.visibility = View.VISIBLE
                binding.lay7.visibility = View.VISIBLE
                binding.lay8.visibility = View.VISIBLE
                binding.lay9.visibility = View.VISIBLE
                binding.lay10.visibility = View.VISIBLE
            }
            MECHANIC_ROLE ->{
                binding.beatButton.visibility = View.GONE
                binding.lay1.visibility = View.GONE
                binding.lay2.visibility = View.GONE
                binding.lay3.visibility = View.GONE
                binding.lay4.visibility = View.GONE
                binding.lay5.visibility = View.GONE
                binding.lay6.visibility = View.VISIBLE
                binding.lay7.visibility = View.VISIBLE
                binding.lay8.visibility = View.VISIBLE

            }




            else -> { // Note the block
                print("x is neither 1 nor 2")
            }
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

    fun callAttendance(){
        showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.doAttendance(
            AttendanceRequestParams(SharedPreferenceUtils.getLoggedInUserId(context as Context))
        )


        responseCall.enqueue(AttendancResponse as Callback<AttendancResponse>)

    }
    private val AttendancResponse = object : NetworkCallBack<AttendancResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<AttendancResponse>) {
            response.data?.respMessage?.let { status ->

                hide()
                showToastMessage( response.data?.respMessage!!)


            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()
        }

    }


//    override fun showToastMessage(message:String){
//        if (context!=null){
//            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
//        }
//
//    }




}