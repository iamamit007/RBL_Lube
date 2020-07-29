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
import com.velectico.rbm.base.views.BaseActivity
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.adapters.BeatListAdapter
import com.velectico.rbm.beats.model.Beats
import com.velectico.rbm.beats.views.BeatListFragmentDirections
import com.velectico.rbm.complaint.adapter.ComplaintListAdapter
import com.velectico.rbm.complaint.model.ComplainListDetails
import com.velectico.rbm.complaint.model.ComplaintModel
import com.velectico.rbm.databinding.FragmentBeatListBinding
import com.velectico.rbm.databinding.FragmentComplaintListBinding
import com.velectico.rbm.databinding.RowBeatListBinding
import com.velectico.rbm.databinding.RowComplaintListBinding
import com.velectico.rbm.menuitems.viewmodel.MenuViewModel
import com.velectico.rbm.utils.MECHANIC_ROLE
import com.velectico.rbm.utils.SALES_LEAD_ROLE
import com.velectico.rbm.utils.TEMP_CURRENT_LOGGED_IN

/**
 * A simple [Fragment] subclass.
 */
class ComplaintList : BaseFragment() {

    private lateinit var menuViewModel: MenuViewModel
    private lateinit var binding: FragmentComplaintListBinding;
    private var complaintList : List<ComplainListDetails> = emptyList()
    private lateinit var adapter: ComplaintListAdapter
    override fun getLayout(): Int {
        return R.layout.fragment_complaint_list
    }

    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentComplaintListBinding
        //complaintList = ComplaintModel().getDummyComplaintList()
        menuViewModel = MenuViewModel.getInstance(activity as BaseActivity)
        setUpRecyclerView()


//        if(menuViewModel.loginResponse.value?.userDetails?.get(0)?.uMRole.toString() == MECHANIC_ROLE){
//            binding.fab.hide()
//        }


        binding.fab.setOnClickListener {
            moveToCreateComplaint()
        }

        binding.fabFilter.setOnClickListener {
            moveToFilterComplaint()
        }
    }


    private fun setUpRecyclerView() {


        adapter = ComplaintListAdapter(object : ComplaintListAdapter.IComplaintListActionCallBack{
            override fun moveToComplainDetails(position: Int, beatTaskId: String?,binding: RowComplaintListBinding) {
                Log.e("test","onAddTask"+beatTaskId)
                val navDirection =  ComplaintListDirections.actionComplaintListToCreateComplaintsUserWise()
                Navigation.findNavController(binding.navigateToDetails).navigate(navDirection)
            }
        })
        binding.rvBeatList.adapter = adapter
        adapter.complaintList = complaintList;
    }

     private fun moveToCreateComplaint(){
      val navDirection =  ComplaintListDirections.actionComplaintListToCreateComplaintsUserWise()
         Navigation.findNavController(binding.fab).navigate(navDirection)
     }

    private fun moveToFilterComplaint(){
        val navDirection =  ComplaintListDirections.actionComplaintListToComplaintFilterFragment()
        Navigation.findNavController(binding.fabFilter).navigate(navDirection)
    }
}
