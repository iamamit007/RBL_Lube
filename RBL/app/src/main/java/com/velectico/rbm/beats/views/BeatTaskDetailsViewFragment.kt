package com.velectico.rbm.beats.views

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.navigation.Navigation

import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseActivity
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.adapters.BeatListAdapter
import com.velectico.rbm.beats.adapters.BeatTaskDetailsViewAdapter
import com.velectico.rbm.beats.model.Beats
import com.velectico.rbm.databinding.*
import com.velectico.rbm.menuitems.viewmodel.MenuViewModel
import com.velectico.rbm.utils.SALES_LEAD_ROLE

/**
 * A simple [Fragment] subclass.
 */
class BeatTaskDetailsViewFragment : BaseFragment() {

    private lateinit var binding: FragmentBeatTaskDetailsViewBinding;
    private lateinit var beatList : List<Beats>
    private lateinit var menuViewModel: MenuViewModel
    private lateinit var adapter: BeatTaskDetailsViewAdapter
    override fun getLayout(): Int {
        return R.layout.fragment_beat_task_details_view
    }

    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentBeatTaskDetailsViewBinding
        menuViewModel = MenuViewModel.getInstance(activity as BaseActivity)
        if(menuViewModel.loginResponse.value?.userDetails?.get(0)?.uMRole.toString() != SALES_LEAD_ROLE){
           binding.beatSummary.visibility = View.GONE
        }
        beatList = Beats().getDummyBeatList()

        setUpRecyclerView()
        /*binding.fab.setOnClickListener {
            moveToCreateBeat()
        }*/
    }







    private fun setUpRecyclerView() {

       adapter = BeatTaskDetailsViewAdapter(object : BeatTaskDetailsViewAdapter.IBeatTaskDetailsViewActionCallBack{
            override fun moveToBeatTaskDetails(position: Int, beatTaskId: String?,binding: RowBeatTaskDetailsViewBinding) {
                Log.e("test","onAddTask"+beatTaskId)
                val navDirection =  BeatTaskDetailsViewFragmentDirections.actionBeatTaskDetailsViewFragmentToBeatTaskDealerDetailsFragment()
                Navigation.findNavController(binding.navigateToDealerDetails).navigate(navDirection)
            }
        });
        binding.rvBeatList.adapter = adapter
        adapter.beatList = beatList
    }

    private fun moveToCreateBeat(){
        //val navDirection =  BeatListFragmentDirections.actionMoveToCreateBeat()
        //Navigation.findNavController(binding.fab).navigate(navDirection)
    }


}
