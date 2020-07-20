package com.velectico.rbm.teamlist.view

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.databinding.ViewDataBinding
import androidx.navigation.Navigation

import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.databinding.FragmentTeamDashboardBinding

/**
 * A simple [Fragment] subclass.
 */
class TeamDashboard : BaseFragment(){


    private lateinit var binding: FragmentTeamDashboardBinding


    override fun getLayout(): Int = R.layout.fragment_team_dashboard

    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentTeamDashboardBinding







        binding.beatButton.setOnClickListener {
            val navDirection =  TeamDashboardDirections.actionTeamDashboardToTeamListFragment(binding.beatButton.text.toString())
            Navigation.findNavController(binding.performanceButton).navigate(navDirection)
        }

        binding.expenseButton.setOnClickListener {
            val navDirection =  TeamDashboardDirections.actionTeamDashboardToTeamListFragment(binding.expenseButton.text.toString())
            Navigation.findNavController(binding.performanceButton).navigate(navDirection)
        }

        binding.leaveButton.setOnClickListener {
            val navDirection =  TeamDashboardDirections.actionTeamDashboardToTeamListFragment(binding.leaveButton.text.toString())
            Navigation.findNavController(binding.performanceButton).navigate(navDirection)
        }

        binding.performanceButton.setOnClickListener{

            val navDirection =  TeamDashboardDirections.actionTeamDashboardToTeamListFragment(binding.performanceButton.text.toString())
            Navigation.findNavController(binding.performanceButton).navigate(navDirection)
        }




    }

}
