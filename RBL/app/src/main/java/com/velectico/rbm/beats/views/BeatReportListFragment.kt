package com.velectico.rbm.beats.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding

import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.adapters.BeatReportListAdapter
import com.velectico.rbm.beats.model.BeatReport
import com.velectico.rbm.databinding.FragmentBeatReportListBinding
import com.velectico.rbm.databinding.FragmentTeamPerformanceDetailsBinding
import com.velectico.rbm.teamlist.adapter.TeamPerformanceDetailsAdapter
import com.velectico.rbm.teamlist.model.TeamPerformanceModel

/**
 * A simple [Fragment] subclass.
 */
class BeatReportListFragment : BaseFragment()  {

    private lateinit var binding: FragmentBeatReportListBinding
    private lateinit var teamList : List<BeatReport>
    private lateinit var adapter: BeatReportListAdapter

    override fun getLayout(): Int {
        return R.layout.fragment_beat_report_list
    }


    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentBeatReportListBinding
        teamList = BeatReport().getDummyBeatComList()
        setUpRecyclerView()

    }


    private fun setUpRecyclerView() {
        adapter = BeatReportListAdapter();
        binding.rvBeatComplaintList.adapter = adapter
        adapter.teamList = teamList
    }

}
