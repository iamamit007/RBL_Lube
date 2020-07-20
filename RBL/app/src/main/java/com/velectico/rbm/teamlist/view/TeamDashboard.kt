package com.velectico.rbm.teamlist.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.ViewDataBinding
import androidx.navigation.Navigation

import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseActivity
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.databinding.DefaultFragmentBinding
import com.velectico.rbm.databinding.FragmentTeamDashboardBinding
import com.velectico.rbm.menuitems.viewmodel.MenuViewModel
import com.velectico.rbm.menuitems.views.DefaultFragment
import com.velectico.rbm.menuitems.views.DefaultFragmentDirections

/**
 * A simple [Fragment] subclass.
 */
class TeamDashboard : BaseFragment(){


    private lateinit var binding: FragmentTeamDashboardBinding


    override fun getLayout(): Int = R.layout.fragment_team_dashboard

    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentTeamDashboardBinding







        binding.beatButton.setOnClickListener {
            val navDirection =  TeamDashboardDirections.actionTeamDashboardToTeamListFragment()
            Navigation.findNavController(binding.beatButton).navigate(navDirection)
        }

        binding.expenseButton.setOnClickListener {
            val navDirection =  TeamDashboardDirections.actionTeamDashboardToTeamListFragment()
            Navigation.findNavController(binding.expenseButton).navigate(navDirection)
        }

        binding.leaveButton.setOnClickListener {
            val navDirection =  TeamDashboardDirections.actionTeamDashboardToTeamListFragment()
            Navigation.findNavController(binding.leaveButton).navigate(navDirection)
        }

        binding.performanceButton.setOnClickListener {
            val navDirection =  TeamDashboardDirections.actionTeamDashboardToTeamListFragment()
            Navigation.findNavController(binding.performanceButton).navigate(navDirection)
        }




    }

}
