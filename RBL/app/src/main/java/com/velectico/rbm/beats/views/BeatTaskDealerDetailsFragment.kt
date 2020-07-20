package com.velectico.rbm.beats.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.navigation.Navigation

import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.databinding.FragmentBeatTaskDealerDetailsBinding
import com.velectico.rbm.databinding.FragmentOrderListBinding
import com.velectico.rbm.order.model.OrderHead
import com.velectico.rbm.order.views.OrderListFragmentDirections

/**
 * A simple [Fragment] subclass.
 */
class BeatTaskDealerDetailsFragment : BaseFragment() {

    private lateinit var binding: FragmentBeatTaskDealerDetailsBinding

    override fun getLayout(): Int {
        return R.layout.fragment_beat_task_dealer_details
    }


    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentBeatTaskDealerDetailsBinding
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

        binding.viewAllTransLayout.setOnClickListener {
            moveToBeatPayAndTrans()
        }

        binding.allBeatReport.setOnClickListener {
            moveToAllBeatReport()
        }
    }

    private fun moveToPerformanceHistory(){
        val navDirection =  BeatTaskDealerDetailsFragmentDirections.actionBeatTaskDealerDetailsFragmentToBeatPerformanceHistory()
        Navigation.findNavController(binding.btnPerformanceHistory).navigate(navDirection)
    }

    private fun moveToCreateComplaint(){
        val navDirection =  BeatTaskDealerDetailsFragmentDirections.actionBeatTaskDealerDetailsFragmentToCreateComplaints()
        Navigation.findNavController(binding.btnComplaints).navigate(navDirection)
    }

    private fun moveToBeatReport(){
        val navDirection =  BeatTaskDealerDetailsFragmentDirections.actionBeatTaskDealerDetailsFragmentToBeatReportFragment()
        Navigation.findNavController(binding.btnBeatReport).navigate(navDirection)
    }

    private fun moveToCreateOrder(){
        val navDirection =  BeatTaskDealerDetailsFragmentDirections.actionBeatTaskDealerDetailsFragmentToCreateOrderFragment()
        Navigation.findNavController(binding.btnNewOrder).navigate(navDirection)
    }

    private fun moveToOrderHistory(){
        val navDirection =  BeatTaskDealerDetailsFragmentDirections.actionBeatTaskDealerDetailsFragmentToOrderListFragment()
        Navigation.findNavController(binding.btnOrderHistory).navigate(navDirection)
    }
    private fun moveToViewAllComplaints(){
        val navDirection =  BeatTaskDealerDetailsFragmentDirections.actionBeatTaskDealerDetailsFragmentToBeatSpecificComplaintList()
        Navigation.findNavController(binding.viewAllComplaintslayOut).navigate(navDirection)
    }

    private fun moveToBeatPayAndTrans(){
        val navDirection =  BeatTaskDealerDetailsFragmentDirections.actionBeatTaskDealerDetailsFragmentToBeatPaymentListFragment()
        Navigation.findNavController(binding.viewAllTransLayout).navigate(navDirection)
    }

    private fun moveToAllBeatReport(){
        val navDirection =  BeatTaskDealerDetailsFragmentDirections.actionBeatTaskDealerDetailsFragmentToBeatReportListFragment()
        Navigation.findNavController(binding.allBeatReport).navigate(navDirection)
    }
}
