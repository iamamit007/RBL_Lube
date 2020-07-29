package com.velectico.rbm.complaint.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding

import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseActivity
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.model.BeatTaskDetails
import com.velectico.rbm.beats.model.DealerDetails
import com.velectico.rbm.complaint.model.ComplainListDetails
import com.velectico.rbm.databinding.FragmentCreateComplaintsBinding
import com.velectico.rbm.databinding.FragmentCreateComplaintsUserWiseBinding
import com.velectico.rbm.menuitems.viewmodel.MenuViewModel
import com.velectico.rbm.utils.DEALER_ROLE
import com.velectico.rbm.utils.DISTRIBUTER_ROLE
import com.velectico.rbm.utils.MECHANIC_ROLE

/**
 * A simple [Fragment] subclass.
 */
class CreateComplaints : BaseFragment() {
    private lateinit var binding: FragmentCreateComplaintsBinding
    private lateinit var menuViewModel: MenuViewModel
    var taskDetail = BeatTaskDetails()
    var dlrDtl = DealerDetails()
    var complainDetail = ComplainListDetails()
    override fun getLayout(): Int {
        return R.layout.fragment_create_complaints
    }
    override fun init(binding: ViewDataBinding) {

        this.binding = binding as FragmentCreateComplaintsBinding
        menuViewModel = MenuViewModel.getInstance(activity as BaseActivity)
        if (complainDetail != null){
            complainDetail = arguments!!.get("complainDetail")  as ComplainListDetails
            binding.inputBatchno.setText(complainDetail.CR_Batch_no.toString())
            binding.inputQuantity.setText(complainDetail.CR_Qty.toString())
            binding.inputRemarks.setText(complainDetail.CR_Remarks.toString())
        }
        taskDetail = arguments!!.get("taskDetail") as BeatTaskDetails
        dlrDtl = arguments!!.get("dealerDetail") as DealerDetails


        binding.dlrNm.text = taskDetail.dealerName.toString()
        binding.tvProdNetPrice.text = dlrDtl.dealerPhone.toString()
        binding.tvProdTotalPrice.text = dlrDtl.DM_Contact_Person.toString()
        binding.tvOrdrAmt.text = "₹" +dlrDtl.orderAmt.toString()
        binding.collectionAmt22.text = "₹" +dlrDtl.collectionAmt.toString()



    }
}
