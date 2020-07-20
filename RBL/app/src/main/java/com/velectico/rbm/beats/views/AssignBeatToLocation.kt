package com.velectico.rbm.beats.views

import android.os.Bundle
import android.view.*
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation

import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.adapters.BeatAssignmentTaskListAdapter
import com.velectico.rbm.beats.model.BeatAssignments
import com.velectico.rbm.beats.model.Beats
import com.velectico.rbm.beats.model.Dealer
import com.velectico.rbm.beats.viewmodel.BeatSharedViewModel
import com.velectico.rbm.databinding.FragmentAssignBeatToLocationBinding
import com.velectico.rbm.databinding.FragmentAssignTaskForBeatBinding
import com.velectico.rbm.databinding.FragmentBeatListBinding


class AssignBeatToLocation : BaseFragment() {

    private lateinit var binding: FragmentAssignBeatToLocationBinding



    override fun getLayout(): Int {
        return R.layout.fragment_assign_beat_to_location
    }

    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentAssignBeatToLocationBinding
        binding.btnAssignTaskToLocation.setOnClickListener {
            moveToBeatTarget()
        }

    }


    private fun moveToBeatTarget(){

        val navDirection =  AssignBeatToLocationDirections.actionAssignBeatToLocationToAssignTargetToBeat()
        Navigation.findNavController(binding.btnAssignTaskToLocation).navigate(navDirection)
    }














}
