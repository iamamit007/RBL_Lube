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
import com.velectico.rbm.beats.adapters.BeatListAdapter
import com.velectico.rbm.beats.adapters.BeatTargetAssignmentAdapter
import com.velectico.rbm.beats.model.Beats
import com.velectico.rbm.databinding.FragmentAssignTargetToBeatBinding
import com.velectico.rbm.databinding.FragmentBeatListBinding

/**
 * A simple [Fragment] subclass.
 */
class AssignTargetToBeat : BaseFragment() {

    private lateinit var binding: FragmentAssignTargetToBeatBinding;
    private lateinit var beatList : List<Beats>
    private lateinit var adapter: BeatTargetAssignmentAdapter
    override fun getLayout(): Int {
        return R.layout.fragment_assign_target_to_beat
    }

    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentAssignTargetToBeatBinding
        binding.btnSaveTask.setOnClickListener {
            moveToBeatList()
        }
        beatList = Beats().getDummyBeatList()
        setUpRecyclerView()

    }


    private fun setUpRecyclerView() {
        adapter = BeatTargetAssignmentAdapter()
        binding.rvTargetList.adapter = adapter
       // adapter.beatList = beatList
    }

    private fun moveToBeatList(){
        val navDirection =  AssignTargetToBeatDirections.actionAssignTargetToBeatToDateWiseBeatListFragment("")
        Navigation.findNavController(binding.btnSaveTask).navigate(navDirection)
    }

}
