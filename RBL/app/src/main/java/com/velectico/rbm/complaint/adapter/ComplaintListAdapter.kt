package com.velectico.rbm.complaint.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.velectico.rbm.beats.adapters.BeatDateListAdapter
import com.velectico.rbm.beats.adapters.BeatListAdapter
import com.velectico.rbm.beats.model.Beats
import com.velectico.rbm.complaint.model.ComplainListDetails
import com.velectico.rbm.complaint.model.ComplaintModel
import com.velectico.rbm.databinding.RowBeatListBinding
import com.velectico.rbm.databinding.RowComplaintListBinding
import com.velectico.rbm.utils.DateUtils
import java.text.SimpleDateFormat
import java.util.*

class ComplaintListAdapter (var setCallback: ComplaintListAdapter.IComplaintListActionCallBack) : RecyclerView.Adapter<ComplaintListAdapter.ViewHolder>() {

    var callBack : ComplaintListAdapter.IComplaintListActionCallBack?=null

    var complaintList = listOf<ComplainListDetails>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    inner class ViewHolder(_binding: RowComplaintListBinding) : RecyclerView.ViewHolder(_binding.root) {
        val binding = _binding

        init {
            callBack = setCallback;
            binding.navigateToDetails.setOnClickListener {
                callBack?.moveToComplainDetails(adapterPosition, "1",binding )
            }

        }

        fun bind(cm: ComplainListDetails?) {
            binding.complaintInfo = cm
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RowComplaintListBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return complaintList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(complaintList[position])
        val inpFormat =  SimpleDateFormat("yyyy-MM-dd", Locale.US);
        val  outputformat =  SimpleDateFormat("dd-MMM-yy", Locale.US);
        val stdate =  DateUtils.parseDate(complaintList[position].CR_Date,inpFormat,outputformat)
        holder.binding.tvUnit.text = stdate
        Picasso.get().load(complaintList[position].imagePath).fit().into(holder.binding.ivComplaintImageUrl)
    }
    interface IComplaintListActionCallBack{
        fun moveToComplainDetails(position:Int,beatTaskId:String?,binding: RowComplaintListBinding)

    }






}