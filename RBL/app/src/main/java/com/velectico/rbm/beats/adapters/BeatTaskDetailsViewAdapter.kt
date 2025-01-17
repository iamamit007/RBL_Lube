package com.velectico.rbm.beats.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.velectico.rbm.beats.model.Beats
import com.velectico.rbm.databinding.RowBeatTaskDetailsViewBinding



class BeatTaskDetailsViewAdapter (var setCallback: BeatTaskDetailsViewAdapter.IBeatTaskDetailsViewActionCallBack) : RecyclerView.Adapter<BeatTaskDetailsViewAdapter.ViewHolder>() {

    var callBack : BeatTaskDetailsViewAdapter.IBeatTaskDetailsViewActionCallBack?=null
    var beatList = listOf<Beats>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    inner class ViewHolder(_binding: RowBeatTaskDetailsViewBinding) : RecyclerView.ViewHolder(_binding.root) {
        val binding = _binding

        init {

            callBack = setCallback;
            binding.navigateToDealerDetails.setOnClickListener {
                callBack?.moveToBeatTaskDetails(adapterPosition, "1",binding )
            }
        }

        fun bind(beats: Beats?) {
            binding.beats = beats
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RowBeatTaskDetailsViewBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return beatList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(beatList[position])
    }


    interface IBeatTaskDetailsViewActionCallBack{
        fun moveToBeatTaskDetails(position:Int,beatTaskId:String?,binding: RowBeatTaskDetailsViewBinding)

    }





}