package com.velectico.rbm.order.adapters

import android.R
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.velectico.rbm.beats.model.CreateOrderListDetails
import com.velectico.rbm.databinding.RowOrderHeadListBinding
import com.velectico.rbm.databinding.RowProductCartBinding
import com.velectico.rbm.order.model.OrderCart
import com.velectico.rbm.order.model.OrderHead
import com.velectico.rbm.order.views.CreateOrderFragment.Companion.orderItems
import com.velectico.rbm.order.views.CreateOrderFragment.Companion.qtyType
import com.velectico.rbm.order.views.CreateOrderFragment.Companion.schemeItems
import com.velectico.rbm.order.views.CreateOrderFragment.Companion.seletedItems
import com.velectico.rbm.utils.productItemClickListener
import kotlinx.android.synthetic.main.fragment_beat_report.view.*
import timber.log.Timber
import java.util.ArrayList

class OrderCartListAdapter(val context: Context,var listener: productItemClickListener) : RecyclerView.Adapter<OrderCartListAdapter.ViewHolder>() {

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

        var languages: MutableList<String> = ArrayList()
        if (orderCart[position].PM_Unit_For_Carton != null && orderCart[position].PM_Pcs_OR_Bucket == "pcs") {
            languages.add("Select quantity type")
            // languages.add("Bucket")
            //languages.add("Pieces")
            languages.add("Carton")
        }
        else if (orderCart[position].PM_Unit_For_Carton == null && orderCart[position].PM_Pcs_OR_Bucket == "pcs") {
            languages.add("Select quantity type")
            // languages.add("Bucket")
            languages.add("Pieces")
            //languages.add("Carton")
        }
        else{
            languages.add("Select quantity type")
            languages.add("Bucket")
        }

        // access the spinner

        if (holder.binding.spQtyType != null) {
            val adapter = context?.let {
                ArrayAdapter(
                    it,
                    android.R.layout.simple_spinner_dropdown_item, languages)
            }
            holder.binding.spQtyType.adapter = adapter

            holder.binding.spQtyType.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {
                    //Toast.makeText(applicationContext, ""+position, Toast.LENGTH_SHORT).show()

                    if (languages[position] == "Bucket"){
                        qtyType = "bucket"
                    }
                    else if (languages[position] == "Pieces"){
                        qtyType = "pcs"
                    }
                    else{
                        qtyType = "carton"
                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }

        var statList: MutableList<String> = ArrayList()
        statList.add("Select scheme")
        for (i in orderCart[position].PSM_Scheme_Details!!){
            statList.add(i.schemeName!!)
        }


        var x  = ArrayAdapter<String>(context, R.layout.simple_spinner_dropdown_item, statList);

        holder.binding.spBeatName.adapter = x


        var intger = 0
        holder.binding.cartPlusImg.setOnClickListener{
            intger += 1
            holder.binding.cartProductQuantityTv.setText(intger.toString())
            orderItems[orderCart[position].PM_ID!!] = holder.binding.cartProductQuantityTv.text.toString()
            listener.onItemClick()
            seletedItems.add(orderCart[position])
        }
        holder.binding.cartMinusImg.setOnClickListener{
            if (intger == 0){
                seletedItems.remove(orderCart[position])
                orderItems.remove(orderCart[position].PM_ID!!)
                return@setOnClickListener
            }
            intger -= 1
            listener.onItemClick()

            holder.binding.cartProductQuantityTv.setText(intger.toString())
            seletedItems.add(orderCart[position])
            orderItems[orderCart[position].PM_ID!!] = holder.binding.cartProductQuantityTv.text.toString()
        }
        Picasso.get().load(orderCart[position].PM_Image_Path).fit().into(holder.binding.listImage)


        holder.binding.spBeatName.onItemSelectedListener= object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View?, pos: Int, id: Long) {
                val x = orderCart[position].PSM_Scheme_Details
                if (pos!=0 && statList.size > pos+1){
                    val y = x!![pos].schemeId
                    schemeItems[orderCart[position].PM_ID!!] = y
                }


            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }
        x.notifyDataSetChanged()

        }
    }



}