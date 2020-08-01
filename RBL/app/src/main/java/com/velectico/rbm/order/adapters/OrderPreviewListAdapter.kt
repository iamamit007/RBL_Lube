package com.velectico.rbm.order.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.velectico.rbm.beats.model.CreateOrderListDetails
import com.velectico.rbm.databinding.RowOrderPreviewBinding
import com.velectico.rbm.databinding.RowProductCartBinding
import com.velectico.rbm.order.model.OrderCart
import com.velectico.rbm.order.views.CreateOrderFragment
import kotlinx.android.synthetic.main.row_order_preview.view.*

class OrderPreviewListAdapter(var orderItemsSelected:HashMap<String,String>) : RecyclerView.Adapter<OrderPreviewListAdapter.ViewHolder>() {


    var orderCart =  listOf<CreateOrderListDetails>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(_binding: RowOrderPreviewBinding) : RecyclerView.ViewHolder(_binding.root) {
        val binding = _binding

        init {
        }

        fun bind(orderCart:  CreateOrderListDetails?) {
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
        try {
            holder.binding.qty.setText("QTY : ${orderItemsSelected[orderCart[position].PM_ID]}")
            val x = orderItemsSelected[orderCart[position].PM_ID]!!.toDouble()
            val y = orderCart[position].PM_Net_Price!!.toDouble()
            holder.binding.cartProductQuantityTv.setText("₹ ${x*y}")
            Picasso.get().load(orderCart[position].PM_Image_Path).fit().into(holder.binding.listImage)
            val sceheId = CreateOrderFragment.schemeItems[orderCart[position].PM_ID]!!
            if (sceheId !=null){
                val cc = orderCart[position].PSM_Scheme_Details!!.find { it.schemeId==sceheId }
                holder.binding.etBeatName.setText("${cc?.schemeName}")

            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }



}