package com.velectico.rbm.teamlist.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.navigation.Navigation

import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.adapters.BeatDateListAdapter
import com.velectico.rbm.beats.model.BeatDate
import com.velectico.rbm.beats.views.DateWiseBeatListFragmentDirections
import com.velectico.rbm.databinding.FragmentDateWiseBeatListBinding
import com.velectico.rbm.databinding.FragmentTeamListBinding
import com.velectico.rbm.databinding.RowBeatListDatesBinding
import com.velectico.rbm.databinding.RowTeamListBinding
import com.velectico.rbm.teamlist.adapter.TeamListAdapter
import com.velectico.rbm.teamlist.model.TeamListModel

/**
 * A simple [Fragment] subclass.
 */
class TeamListFragment : BaseFragment()  {

    private lateinit var binding: FragmentTeamListBinding
    private lateinit var teamList : List<TeamListModel>
    private lateinit var adapter: TeamListAdapter

    override fun getLayout(): Int {
        return R.layout.fragment_team_list
    }


    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentTeamListBinding
        teamList = TeamListModel().getDummyTeamList()
        setUpRecyclerView()

    }

    private fun setUpRecyclerView() {
        adapter = TeamListAdapter(object : TeamListAdapter.ITeamListActionCallBack{
            override fun moveToBeatDetails(position: Int, beatTaskId: String?,binding: RowTeamListBinding) {
                //Log.e("test","onAddTask"+beatTaskId)
                val navDirection =  TeamListFragmentDirections.actionTeamListFragmentToTeamPerformanceFragment()
                Navigation.findNavController(binding.navigateToDetails).navigate(navDirection)
            }
        });
        binding.rvTeamList.adapter = adapter
        adapter.teamList = teamList
    }




}
