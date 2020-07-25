package com.velectico.rbm.payment.view

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.navigation.Navigation
import com.google.android.material.textfield.TextInputEditText
import com.velectico.rbm.payment.viewmodel.FragmentPaymentListViewModel
import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseActivity
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.views.BeatListFragmentDirections
import com.velectico.rbm.beats.views.DateWiseBeatListFragmentDirections
import com.velectico.rbm.databinding.*
import com.velectico.rbm.leave.model.LeaveReason
import com.velectico.rbm.leave.view.ApplyLeaveFragmentArgs
import com.velectico.rbm.leave.view.LeaveListFragment
import com.velectico.rbm.menuitems.viewmodel.MenuViewModel
import com.velectico.rbm.payment.adapter.BeatPaymentListAdapter
import com.velectico.rbm.payment.adapter.PaymentListAdapter
import com.velectico.rbm.payment.models.PaymentInfo
import com.velectico.rbm.reminder.adapter.ReminderListAdapter
import com.velectico.rbm.reminder.model.ReminderList
import com.velectico.rbm.utils.DEALER_ROLE
import com.velectico.rbm.utils.DISTRIBUTER_ROLE
import com.velectico.rbm.utils.SALES_LEAD_ROLE
import com.velectico.rbm.utils.TEMP_CURRENT_LOGGED_IN


class FragmentPaymentList :  BaseFragment()  {
    private lateinit var binding: FragmentPaymentListFragmentBinding;
    private lateinit var beatList : List<PaymentInfo>
    private lateinit var adapter: BeatPaymentListAdapter
    private var currentDatePicketParentView : TextInputEditText? = null
    private lateinit var menuViewModel: MenuViewModel
    override fun getLayout(): Int {
        return R.layout.fragment_payment_list_fragment
    }

    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentPaymentListFragmentBinding
        menuViewModel = MenuViewModel.getInstance(activity as BaseActivity)
      if(menuViewModel.loginResponse.value?.userDetails?.get(0)?.uMRole.toString() == DEALER_ROLE ||
          menuViewModel.loginResponse.value?.userDetails?.get(0)?.uMRole.toString() == DISTRIBUTER_ROLE) {
          binding.dealerListspinner.visibility = View.GONE
      }
        /*when (TEMP_CURRENT_LOGGED_IN) {
            DEALER_ROLE ->{
                binding.dealerListspinner.visibility = View.GONE
            }
        }*/
        beatList = PaymentInfo().getDummyBeatList()
        setUpRecyclerView()
        /*binding.fab.setOnClickListener {
            moveToCreateBeat()
        }*/
        binding.paymenthistory1.setOnClickListener{
            showToastMessage("popup")
            val navDirection =  FragmentPaymentListDirections.actionFragmentPaymentListToPaymentHistoryList()
            Navigation.findNavController(binding.paymenthistory1).navigate(navDirection)
        }
        binding.paybtn1.setOnClickListener{
            showToastMessage("pay")
            val navDirection =  FragmentPaymentListDirections.actionFragmentPaymentListToFragmentAddPaymentInfo()
            Navigation.findNavController(binding.paybtn1).navigate(navDirection)

        }

    }


    private fun setUpRecyclerView() {

//
//        adapter = PaymentListAdapter(object : PaymentListAdapter.IBeatListActionCallBack{
//            override fun moveToPaymentDetails(position: Int, beatTaskId: String?,binding: RowPaymentListBinding) {
//                Log.e("test","onAddTask"+beatTaskId)
//                val navDirection =  FragmentPaymentListDirections.actionFragmentPaymentListToFragmentAddPaymentInfo()
//                Navigation.findNavController(binding.navigateToTaskDetails).navigate(navDirection)
//            }
//        });
        adapter = BeatPaymentListAdapter();
        binding.rvPaymentList.adapter = adapter
        adapter.beatList = beatList
    }

    /*private fun moveToPaymentDetails(){
      val navDirection =  BeatListFragmentDirections.actionMoveToCreateBeat()
         Navigation.findNavController(binding.fab).navigate(navDirection)
     }*/


}
