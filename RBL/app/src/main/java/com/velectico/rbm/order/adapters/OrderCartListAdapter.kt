package com.velectico.rbm.order.adapters

import android.R
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.velectico.rbm.beats.model.CreateOrderListDetails
import com.velectico.rbm.databinding.RowOrderHeadListBinding
import com.velectico.rbm.databinding.RowProductCartBinding
import com.velectico.rbm.order.model.OrderCart
import com.velectico.rbm.order.model.OrderHead
import com.velectico.rbm.order.views.CreateOrderFragment.Companion.orderItems
import com.velectico.rbm.order.views.CreateOrderFragment.Companion.seletedItems
import timber.log.Timber
import java.util.ArrayList

class OrderCartListAdapter(val context: Context) : RecyclerView.Adapter<OrderCartListAdapter.ViewHolder>() {

    var orderCart =  listOf<CreateOrderListDetails>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(_binding: RowProductCartBinding) : RecyclerView.ViewHolder(_binding.root) {
        val binding = _binding

        init {
        }

        fun bind(orderCart:  CreateOrderListDetails?) {
            binding.orderCartInfo = orderCart
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderCartListAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RowProductCartBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return orderCart.size
    }

    override fun onBindViewHolder(holder: OrderCartListAdapter.ViewHolder, position: Int) {
        holder.bind(orderCart[position])

        var statList: MutableList<String> = ArrayList()
        statList.add("Select scheme")
        for (i in orderCart[position].PSM_Scheme_Details!!){
            statList.add(i.schemeName!!)
        }


        var x  = ArrayAdapter<String>(context, R.layout.simple_spinner_item, statList);

        holder.binding.spBeatName.adapter = x
        x.notifyDataSetChanged()


        var intger = 0
        holder.binding.cartPlusImg.setOnClickListener{
            intger += 1
            holder.binding.cartProductQuantityTv.setText(intger.toString())
            orderItems[orderCart[position].PM_ID!!] = holder.binding.cartProductQuantityTv.text.toString()
            seletedItems.add(orderCart[position])
        }
        holder.binding.cartMinusImg.setOnClickListener{
            if (intger == 0){
                seletedItems.remove(orderCart[position])
                orderItems.remove(orderCart[position].PM_ID!!)
                return@setOnClickListener
            }
            intger -= 1
            holder.binding.cartProductQuantityTv.setText(intger.toString())
            seletedItems.add(orderCart[position])
            orderItems[orderCart[position].PM_ID!!] = holder.binding.cartProductQuantityTv.text.toString()
        }
        Picasso.get().load(orderCart[position].PM_Image_Path).fit().into(holder.binding.listImage)

    }


}