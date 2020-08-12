package com.velectico.rbm.products.adapters

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseActivity
import com.velectico.rbm.beats.adapters.BeatDateListAdapter
import com.velectico.rbm.beats.model.CreateOrderListDetails
import com.velectico.rbm.complaint.adapter.ComplaintListAdapter
import com.velectico.rbm.databinding.RowBeatListDatesBinding
import com.velectico.rbm.databinding.RowProductListBinding
import com.velectico.rbm.menuitems.viewmodel.MenuViewModel
import com.velectico.rbm.products.models.ProductInfo
import com.velectico.rbm.utils.MECHANIC_ROLE
import com.velectico.rbm.utils.TEMP_CURRENT_LOGGED_IN
import com.velectico.rbm.utils.productItemClickListener

//https://codelabs.developers.google.com/codelabs/kotlin-android-training-recyclerview-fundamentals/#4
//https://medium.com/@sanjeevy133/an-idiots-guide-to-android-recyclerview-and-databinding-4ebf8db0daff
//https://medium.com/androiddevelopers/android-data-binding-recyclerview-db7c40d9f0e4 @TODO

class ProductListAdapter(var setCallback: ProductListAdapter.IProdListActionCallBack) : RecyclerView.Adapter<ProductListAdapter.ViewHolder>() {
//    private val userRole = userRole
//    private lateinit var menuViewModel: MenuViewModel
//    private val mActivity = _activity
var callBack : ProductListAdapter.IProdListActionCallBack?=null
    var data = listOf<CreateOrderListDetails>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }


    inner class ViewHolder(_binding: RowProductListBinding) : RecyclerView.ViewHolder(_binding.root) {
        val binding = _binding
        init {
            callBack = setCallback;
            binding.navigateToDetails.setOnClickListener {
                callBack?.moveToProdDetails(adapterPosition, "1",binding )
            }
        }

        fun bind(productInfo: CreateOrderListDetails?) {
            binding.productInfo = productInfo
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RowProductListBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(
            binding
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
        Picasso.get().load(data[position].PM_Image_Path).fit().into(holder.binding.ivProdImageUrl)

    }



    interface IProdListActionCallBack{
        fun moveToProdDetails(position:Int,beatTaskId:String?,binding: RowProductListBinding)

    }
}