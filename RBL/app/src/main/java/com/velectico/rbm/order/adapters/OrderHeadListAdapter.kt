package com.velectico.rbm.order.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.velectico.rbm.beats.adapters.BeatDateListAdapter
import com.velectico.rbm.databinding.RowExpenseListBinding
import com.velectico.rbm.databinding.RowOrderHeadListBinding
import com.velectico.rbm.expense.adapter.ExpenseListAdapter
import com.velectico.rbm.expense.model.Expense
import com.velectico.rbm.order.model.OrderHead

class OrderHeadListAdapter(var setCallback: OrderHeadListAdapter.IBeatDateListActionCallBack) : RecyclerView.Adapter<OrderHeadListAdapter.ViewHolder>() {

    var callBack : OrderHeadListAdapter.IBeatDateListActionCallBack?=null
    var orderList =  listOf<OrderHead>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(_binding: RowOrderHeadListBinding) : RecyclerView.ViewHolder(_binding.root) {
        val binding = _binding

        init {
            callBack = setCallback;
            binding.navigateToDetails.setOnClickListener {
                callBack?.moveToOrderDetails(adapterPosition, "1",binding )

            }


        }

        fun bind(orderHead:  OrderHead?) {
            binding.orderHeadInfo = orderHead
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHeadListAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RowOrderHeadListBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    override fun onBindViewHolder(holder: OrderHeadListAdapter.ViewHolder, position: Int) {
        holder.bind(orderList[position])
    }

    interface IBeatDateListActionCallBack{
        fun moveToOrderDetails(position:Int,beatTaskId:String?,binding: RowOrderHeadListBinding)

    }









}