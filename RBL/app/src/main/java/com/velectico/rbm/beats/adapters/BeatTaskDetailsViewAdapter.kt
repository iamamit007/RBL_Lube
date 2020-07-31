package com.velectico.rbm.beats.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.velectico.rbm.beats.model.BeatTaskDetails
import com.velectico.rbm.beats.model.Beats
import com.velectico.rbm.databinding.RowBeatTaskDetailsViewBinding
import com.velectico.rbm.utils.GloblalDataRepository


class BeatTaskDetailsViewAdapter (var setCallback: BeatTaskDetailsViewAdapter.IBeatTaskDetailsViewActionCallBack) : RecyclerView.Adapter<BeatTaskDetailsViewAdapter.ViewHolder>() {

    var callBack : BeatTaskDetailsViewAdapter.IBeatTaskDetailsViewActionCallBack?=null
    var beatList = listOf<BeatTaskDetails>()
        set(value) {
            field = value
            notifyDataSetChanged()

        }
    inner class ViewHolder(_binding: RowBeatTaskDetailsViewBinding) : RecyclerView.ViewHolder(_binding.root) {
        val binding = _binding

        init {

            callBack = setCallback;

        }

        fun bind(beats: BeatTaskDetails?) {
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
        GloblalDataRepository.getInstance().taskId = beatList[position].taskId
        GloblalDataRepository.getInstance().delalerId = beatList[position].dealerId
        GloblalDataRepository.getInstance().distribId = beatList[position].distribId
        holder.binding.navigateToDealerDetails.setOnClickListener {
            callBack?.moveToBeatTaskDetails(position, "1",holder.binding )
        }
        if (beatList[position].distribName != null){
            holder.binding.tvProdNetPrice.text = "Distributor"
            holder.binding.cutGrade.text = beatList[position].distribGrade
        }
        else{
            holder.binding.tvProdNetPrice.text = "Dealer"
            holder.binding.cutGrade.text = beatList[position].dealerGrade
        }
    }


    interface IBeatTaskDetailsViewActionCallBack{
        fun moveToBeatTaskDetails(position:Int,beatTaskId:String?,binding: RowBeatTaskDetailsViewBinding)

    }





}