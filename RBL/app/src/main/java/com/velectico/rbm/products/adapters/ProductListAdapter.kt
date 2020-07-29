package com.velectico.rbm.products.adapters

import android.app.Activity
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseActivity
import com.velectico.rbm.beats.adapters.BeatDateListAdapter
import com.velectico.rbm.databinding.RowBeatListDatesBinding
import com.velectico.rbm.databinding.RowProductListBinding
import com.velectico.rbm.menuitems.viewmodel.MenuViewModel
import com.velectico.rbm.products.models.ProductInfo
import com.velectico.rbm.utils.MECHANIC_ROLE
import com.velectico.rbm.utils.TEMP_CURRENT_LOGGED_IN

//https://codelabs.developers.google.com/codelabs/kotlin-android-training-recyclerview-fundamentals/#4
//https://medium.com/@sanjeevy133/an-idiots-guide-to-android-recyclerview-and-databinding-4ebf8db0daff
//https://medium.com/androiddevelopers/android-data-binding-recyclerview-db7c40d9f0e4 @TODO

class ProductListAdapter(userRole:String,_activity: Activity, var setCallback: ProductListAdapter.IProdListActionCallBack) : RecyclerView.Adapter<ProductListAdapter.ViewHolder>() {
    var callBack : ProductListAdapter.IProdListActionCallBack?=null
    private val userRole = userRole
    private lateinit var menuViewModel: MenuViewModel
    private val mActivity = _activity
    var data = listOf<ProductInfo>()
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


            if(userRole == MECHANIC_ROLE){
                binding.cbProdSel.visibility = View.GONE
            }

            /* if(menuViewModel.loginResponse.value?.userDetails?.get(0)?.uMRole.toString() == MECHANIC_ROLE){
                 binding.fab.hide()
             }*/


        }

        fun bind(productInfo: ProductInfo?) {
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
       // Log.e("test","image path---$position---->"+data[position].prodImageUrl)
        setImageUsingGlide(data[position].prodImageUrl!!, mActivity, holder.binding.ivProdImageUrl)

    }


    private fun setImageUsingGlide(photoUrl: String, activity: Activity, imageView: ImageView) {
        var x = "https://"+photoUrl;
        Glide.with(activity)  //2
            .load(x) //3
            .fitCenter() //4
            .placeholder(R.mipmap.ic_launcher) //5
            .error(R.drawable.ic_error) //6
            .fallback(R.drawable.ic_error) //7
            .into(imageView) //8
    }

    interface IProdListActionCallBack{
        fun moveToProdDetails(position:Int,beatTaskId:String?,binding: RowProductListBinding)

    }
}