package com.velectico.rbm.beats.adapters

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.velectico.rbm.R
import com.velectico.rbm.beats.model.Beats
import com.velectico.rbm.beats.model.DealDistMechList
import com.velectico.rbm.beats.model.TaskDetails
import com.velectico.rbm.beats.views.AssignTargetToBeat.Companion.orderDetailsBeatTaskList
import com.velectico.rbm.beats.views.AssignTargetToBeat.Companion.seletedItems
import com.velectico.rbm.databinding.RowAssignTargetToBeatBinding
import com.velectico.rbm.databinding.RowProductCartBinding
import com.velectico.rbm.order.adapters.OrderCartListAdapter



class BeatTargetAssignmentAdapter(val beatList : List<DealDistMechList> ) : RecyclerView.Adapter<BeatTargetAssignmentAdapter.ViewHolder>() {

    inner class ViewHolder(_binding: RowAssignTargetToBeatBinding) : RecyclerView.ViewHolder(_binding.root) {
        val binding = _binding

        init {
        }

        fun bind(beats: DealDistMechList?) {
            binding.assignments = beats
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BeatTargetAssignmentAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RowAssignTargetToBeatBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return beatList.size
    }



    override fun onBindViewHolder(holder: BeatTargetAssignmentAdapter.ViewHolder, position: Int) {
        holder.bind(beatList[position])
        holder.binding.checkBox2.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                seletedItems.add(beatList[position])
                orderDetailsBeatTaskList.add(orderDetailsBeatTask(holder.binding.inputOrderAmt.text.toString(),holder.binding.inputPmtClctd.text.toString()))

            }else{
                try {
                    seletedItems.remove(beatList[position])
                    val x = orderDetailsBeatTaskList.find { it.orderAmount == holder.binding.inputOrderAmt.text.toString() }
                    orderDetailsBeatTaskList.remove(x)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

    }





}

data class orderDetailsBeatTask (
    val orderAmount:String?,val paymentAmount:String?

)