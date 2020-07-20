package com.velectico.rbm.beats.adapters

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.velectico.rbm.R
import com.velectico.rbm.beats.model.Beats
import com.velectico.rbm.databinding.RowAssignTargetToBeatBinding
import com.velectico.rbm.databinding.RowProductCartBinding
import com.velectico.rbm.order.adapters.OrderCartListAdapter



class BeatTargetAssignmentAdapter : RecyclerView.Adapter<BeatTargetAssignmentAdapter.ViewHolder>() {

    inner class ViewHolder(_binding: RowAssignTargetToBeatBinding) : RecyclerView.ViewHolder(_binding.root) {
        val binding = _binding

        init {
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BeatTargetAssignmentAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RowAssignTargetToBeatBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return 10
    }



    override fun onBindViewHolder(holder: BeatTargetAssignmentAdapter.ViewHolder, position: Int) {
        //holder.bind(orderCart[position])
    }





}