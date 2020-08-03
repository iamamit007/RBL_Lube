package com.velectico.rbm.order.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.velectico.rbm.beats.adapters.BeatDateListAdapter
import com.velectico.rbm.beats.model.OrderProductListDetails
import com.velectico.rbm.databinding.RowBeatListDatesBinding
import com.velectico.rbm.databinding.RowOrderDetailsBinding
import com.velectico.rbm.databinding.RowOrderHeadListBinding
import com.velectico.rbm.order.model.OrderCart

class OrderDetailsAdapter : RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder>() {


    var orderCart =  listOf<OrderProductListDetails>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(_binding: RowOrderDetailsBinding) : RecyclerView.ViewHolder(_binding.root) {
        val binding = _binding

        init {


        }

        fun bind(orderCart:  OrderProductListDetails?) {
            binding.orderCartInfo = orderCart
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailsAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RowOrderDetailsBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return orderCart.size
    }

    override fun onBindViewHolder(holder: OrderDetailsAdapter.ViewHolder, position: Int) {
        holder.bind(orderCart[position])
        Picasso.get().load(orderCart[position].prodImage).fit().into(holder.binding.listImage)
    }


}