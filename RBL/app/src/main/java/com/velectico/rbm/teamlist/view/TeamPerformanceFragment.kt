package com.velectico.rbm.teamlist.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.navigation.Navigation

import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.databinding.FragmentTeamListBinding
import com.velectico.rbm.databinding.FragmentTeamPerformanceBinding
import com.velectico.rbm.teamlist.model.TeamListModel
import kotlinx.android.synthetic.main.fragment_team_performance.*

/**
 * A simple [Fragment] subclass.
 */
class TeamPerformanceFragment : BaseFragment()  {

    private lateinit var binding: FragmentTeamPerformanceBinding

    override fun getLayout(): Int {
        return R.layout.fragment_team_performance
    }

    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentTeamPerformanceBinding

        binding.lblMonthly.setOnClickListener {
            moveToDetails()
        }

        binding.lblMonthly.setOnClickListener {
            moveToDetails()
        }

        binding.lblMonthly.setOnClickListener {
            moveToDetails()
        }

        binding.lblMonthly.setOnClickListener {
            moveToDetails()
        }


    }

    fun moveToDetails(){
        val navDirection =  TeamPerformanceFragmentDirections.actionTeamPerformanceFragmentToTeamPerformanceDetailsFragment()
        Navigation.findNavController(binding.lblMonthly).navigate(navDirection)
    }

}
