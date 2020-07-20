package com.velectico.rbm.redeem.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.velectico.rbm.databinding.RowRedeemListBinding
import com.velectico.rbm.redeem.model.RedeemInfo

class RedeemListAdapter (var setCallback: RedeemListAdapter.IBeatListActionCallBack) : RecyclerView.Adapter<RedeemListAdapter.ViewHolder>() {

    var callBack: RedeemListAdapter.IBeatListActionCallBack? = null
    var beatList = listOf<RedeemInfo>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(_binding: RowRedeemListBinding) :
        RecyclerView.ViewHolder(_binding.root) {
        val binding = _binding

        init {

            callBack = setCallback;
//            binding.navigateToTaskDetails.setOnClickListener {
//                callBack?.moveToBeatTaskDetails(adapterPosition, "1", binding)
//            }
        }

        fun bind(beats: RedeemInfo?) {
            binding.redeemlist = beats
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RowRedeemListBinding.inflate(layoutInflater, parent, false)
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
            binding: RowRedeemListBinding
        )

    }
}
