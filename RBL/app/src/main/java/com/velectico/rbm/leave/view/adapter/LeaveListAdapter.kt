package com.velectico.rbm.leave.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.velectico.rbm.R
import com.velectico.rbm.databinding.ItemLeaveListBinding
import com.velectico.rbm.leave.model.LeaveListModel
import com.velectico.rbm.utils.DateUtils
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by mymacbookpro on 2020-05-08
 * TODO: Add a class header comment!
 */
class LeaveListAdapter(private var list:List<LeaveListModel>, private val onClickListener: (View, Int , Int) -> Unit)
    : RecyclerView.Adapter<LeaveListAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<ItemLeaveListBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_leave_list,
            parent, false
        )
        return ViewHolder(
            binding
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            bind(list.get(position))
            val inpFormat =  SimpleDateFormat("yyyy-MM-dd", Locale.US);
            val outputformat =  SimpleDateFormat("dd-MMM-yy", Locale.US);
            val stdate =  DateUtils.parseDate(list.get(position).leaveFrom,inpFormat,outputformat)
            val endate =  DateUtils.parseDate(list.get(position).leaveTo,inpFormat,outputformat)
            holder.binding.leaveFromTv.text = stdate
            holder.binding.leaveToTv.text = endate
            holder.binding.ivDelete.setOnClickListener(View.OnClickListener {
                onClickListener.invoke(holder.itemView, position,1) //delete
            })
            holder.itemView.setOnClickListener {
                onClickListener.invoke(holder.itemView, position,2) //edit
            }
        }
    }

    class ViewHolder(val binding: ItemLeaveListBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(model: LeaveListModel){
            binding.leaveModel = model
            binding.executePendingBindings()
        }
    }
}