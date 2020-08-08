package com.velectico.rbm.payment.view

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.transition.Slide
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.ViewDataBinding
import androidx.navigation.Navigation

import com.velectico.rbm.R
import com.velectico.rbm.RBMLubricantsApplication
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.views.BeatListFragmentDirections
import com.velectico.rbm.databinding.FragmentBeatPaymentListBinding
import com.velectico.rbm.databinding.FragmentPaymentListFragmentBinding
import com.velectico.rbm.databinding.RowPaymentListBinding
import com.velectico.rbm.menuitems.views.DefaultFragmentDirections
import com.velectico.rbm.payment.adapter.BeatPaymentListAdapter
import com.velectico.rbm.payment.adapter.PaymentListAdapter
import com.velectico.rbm.payment.models.PaymentInfo

/**
 * A simple [Fragment] subclass.
 */
class BeatPaymentListFragment :  BaseFragment() {

    private lateinit var binding: FragmentBeatPaymentListBinding;
    private lateinit var beatList : List<PaymentInfo>
    private lateinit var adapter: BeatPaymentListAdapter
    override fun getLayout(): Int {
        return R.layout.fragment_beat_payment_list
    }

    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentBeatPaymentListBinding
        if (RBMLubricantsApplication.globalRole == "Team" ){
            binding.paybtn.visibility = View.GONE
        }
        beatList = PaymentInfo().getDummyBeatList()
        setUpRecyclerView()
        /*binding.fab.setOnClickListener {
            moveToCreateBeat()
        }*/

        binding.paymenthistory.setOnClickListener{
            //showToastMessage("popup")
            val navDirection =  BeatPaymentListFragmentDirections.actionBeatPaymentListFragmentToPaymentHistoryList()
            Navigation.findNavController(binding.paymenthistory).navigate(navDirection)
        }
        binding.paybtn.setOnClickListener{
            //showToastMessage("pay")

            val navDirection =  BeatPaymentListFragmentDirections.actionBeatPaymentListFragmentToFragmentAddPaymentInfo()
            Navigation.findNavController(binding.paybtn).navigate(navDirection)
        }

    }


    private fun setUpRecyclerView() {


        adapter = BeatPaymentListAdapter();

        binding.rvPaymentList.adapter = adapter
        adapter.beatList = beatList
    }

    /*private fun moveToPaymentDetails(){
      val navDirection =  BeatListFragmentDirections.actionMoveToCreateBeat()
         Navigation.findNavController(binding.fab).navigate(navDirection)
     }*/


}
