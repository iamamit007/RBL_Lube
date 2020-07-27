package com.velectico.rbm.order.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.velectico.rbm.databinding.RowOrderPreviewBinding
import com.velectico.rbm.databinding.RowProductCartBinding
import com.velectico.rbm.order.model.OrderCart

class OrderPreviewListAdapter : RecyclerView.Adapter<OrderPreviewListAdapter.ViewHolder>() {


    var orderCart =  listOf<OrderCart>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(_binding: RowOrderPreviewBinding) : RecyclerView.ViewHolder(_binding.root) {
        val binding = _binding

        init {
        }

        fun bind(orderCart:  OrderCart?) {
            binding.orderPreviewInfo = orderCart
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderPreviewListAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RowOrderPreviewBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return orderCart.size
    }

    override fun onBindViewHolder(holder: OrderPreviewListAdapter.ViewHolder, position: Int) {
        holder.bind(orderCart[position])
    }



}