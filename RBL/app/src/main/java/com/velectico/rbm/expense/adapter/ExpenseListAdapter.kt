package com.velectico.rbm.expense.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.velectico.rbm.databinding.RowExpenseListBinding
import com.velectico.rbm.expense.model.Expense

class ExpenseListAdapter(var setCallback: IExpenseActionCallBack) : RecyclerView.Adapter<ExpenseListAdapter.ViewHolder>() {
     var callBack : IExpenseActionCallBack?=null
     var expenseList = listOf<Expense>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    init {
        callBack = setCallback;
    }


    inner class ViewHolder(_binding: RowExpenseListBinding) : RecyclerView.ViewHolder(_binding.root) {
        val binding = _binding

        init {
            binding.ivDelete.setOnClickListener(View.OnClickListener {
                callBack?.onDelete(adapterPosition,expenseList[adapterPosition].expenseId)
            })
          //  binding.ivEdit.setOnClickListener(View.OnClickListener {
            //    callBack?.onEdit(adapterPosition,expenseList[adapterPosition].expenseId)
            //})
        }

        fun bind(expense: Expense?) {
            binding.expenseInfo = expense
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RowExpenseListBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return expenseList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(expenseList[position])
    }

    interface IExpenseActionCallBack{
        fun onDelete(position:Int,expenseId:String?)
        fun onEdit(position:Int,expenseId:String?)
    }
}