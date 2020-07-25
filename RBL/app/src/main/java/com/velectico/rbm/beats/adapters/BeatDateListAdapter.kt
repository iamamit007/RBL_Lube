package com.velectico.rbm.beats.adapters

import android.app.Activity
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.TypedArrayUtils
import androidx.recyclerview.widget.RecyclerView
import com.velectico.rbm.R
import com.velectico.rbm.beats.model.BeatDate
import com.velectico.rbm.beats.model.ScheduleDates
import com.velectico.rbm.databinding.FragmentDateWiseBeatListBinding
import com.velectico.rbm.databinding.RowBeatListDatesBinding

class BeatDateListAdapter(var setCallback: BeatDateListAdapter.IBeatDateListActionCallBack,var data:List<ScheduleDates>) : RecyclerView.Adapter<BeatDateListAdapter.ViewHolder>() {
    var callBack : BeatDateListAdapter.IBeatDateListActionCallBack?=null
    var beatList = listOf<ScheduleDates>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    inner class ViewHolder(_binding: RowBeatListDatesBinding) : RecyclerView.ViewHolder(_binding.root) {
        val binding = _binding
        init {
            callBack = setCallback;

            if(adapterPosition ==2){
                binding.beatDateRow.setBackgroundColor(Color.RED)
            }


        }

        fun bind(beatDate: ScheduleDates?) {
            binding.beatListDateInfo = beatDate
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RowBeatListDatesBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        if(position == 2 || position == 3 || position == 4 ){
            holder.binding.beatDateRow.setBackgroundColor(Color.parseColor("#ccffdd"))
        }else if(position == 0 || position == 1){
            holder.binding.beatDateRow.setBackgroundColor(Color.parseColor("#ffffe6"))
        }else {
            holder.binding.beatDateRow.setBackgroundColor(Color.parseColor("#ccd9ff"))
            holder.binding.navigateToDetails.tag = position
        }


        holder.bind(data[position])

        holder.binding.navigateToDetails.setOnClickListener {
            callBack?.moveToBeatDetails(position, data[position].schedule_startDate,holder.binding )

        }
    }

    interface IBeatDateListActionCallBack{
        fun moveToBeatDetails(position:Int,beatTaskId:String?,binding: RowBeatListDatesBinding)

    }






}