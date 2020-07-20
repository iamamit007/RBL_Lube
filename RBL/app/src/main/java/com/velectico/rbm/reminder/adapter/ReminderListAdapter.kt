package com.velectico.rbm.reminder.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.velectico.rbm.databinding.RowReminderListBinding
import com.velectico.rbm.reminder.model.ReminderList

class ReminderListAdapter (var setCallback: ReminderListAdapter.IBeatListActionCallBack) : RecyclerView.Adapter<ReminderListAdapter.ViewHolder>() {

    var callBack: ReminderListAdapter.IBeatListActionCallBack? = null
    var beatList = listOf<ReminderList>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(_binding: RowReminderListBinding) :
        RecyclerView.ViewHolder(_binding.root) {
        val binding = _binding

        init {

            callBack = setCallback;
            binding.navigateToTaskDetails.setOnClickListener {
                callBack?.moveToBeatTaskDetails(adapterPosition, "1", binding)
            }
        }

        fun bind(beats: ReminderList?) {
            binding.reminderlist = beats
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RowReminderListBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return beatList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(beatList[position])
    }


    interface IBeatListActionCallBack {
        fun moveToBeatTaskDetails(
            position: Int,
            beatTaskId: String?,
            binding: RowReminderListBinding
        )

    }
}
