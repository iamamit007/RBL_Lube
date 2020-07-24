package com.velectico.rbm.complaint.views

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.navigation.Navigation

import com.velectico.rbm.R
import com.velectico.rbm.RBMLubricantsApplication
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.complaint.adapter.ComplaintListAdapter
import com.velectico.rbm.complaint.model.ComplaintModel
import com.velectico.rbm.databinding.FragmentBeatSpecificComplaintListBinding
import com.velectico.rbm.databinding.FragmentComplaintListBinding
import com.velectico.rbm.databinding.RowComplaintListBinding

/**
 * A simple [Fragment] subclass.
 */
class BeatSpecificComplaintList : BaseFragment() {

    private lateinit var binding: FragmentBeatSpecificComplaintListBinding;
    private lateinit var complaintList : List<ComplaintModel>
    private lateinit var adapter: ComplaintListAdapter
    override fun getLayout(): Int {
        return R.layout.fragment_beat_specific_complaint_list
    }

    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentBeatSpecificComplaintListBinding
        complaintList = ComplaintModel().getDummyComplaintList()
        setUpRecyclerView()

    }


    private fun setUpRecyclerView() {



        adapter = ComplaintListAdapter(object : ComplaintListAdapter.IComplaintListActionCallBack{
            override fun moveToComplainDetails(position: Int, beatTaskId: String?,binding: RowComplaintListBinding) {
                Log.e("test","onAddTask"+beatTaskId)
                val navDirection = BeatSpecificComplaintListDirections.actionBeatSpecificComplaintListToCreateComplaints()
                Navigation.findNavController(binding.navigateToDetails).navigate(navDirection)

            }
        })
        binding.rvComplaintList.adapter = adapter
        adapter.complaintList = complaintList;
    }


}
