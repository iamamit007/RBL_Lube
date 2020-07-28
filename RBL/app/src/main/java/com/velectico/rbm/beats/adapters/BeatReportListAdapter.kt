package com.velectico.rbm.beats.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.velectico.rbm.beats.model.BeatReport
import com.velectico.rbm.beats.model.BeatReportListDetails
import com.velectico.rbm.databinding.RowBeatReportListBinding
import com.velectico.rbm.databinding.RowTeamPerformanceListBinding
import com.velectico.rbm.teamlist.adapter.TeamPerformanceDetailsAdapter
import com.velectico.rbm.teamlist.model.TeamPerformanceModel

class BeatReportListAdapter : RecyclerView.Adapter<BeatReportListAdapter.ViewHolder>() {


    var teamList = listOf<BeatReportListDetails>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    inner class ViewHolder(_binding: RowBeatReportListBinding) : RecyclerView.ViewHolder(_binding.root) {
        val binding = _binding
        init {

        }

        fun bind(beatReport: BeatReportListDetails?) {
            binding.beatReportListInfo = beatReport
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RowBeatReportListBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return teamList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {





        holder.bind(teamList[position])
    }

}