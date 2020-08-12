package com.velectico.rbm.products.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.squareup.picasso.Picasso

import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseActivity
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.model.CreateOrderListDetails
import com.velectico.rbm.beats.model.PSM_Scheme_DetailsResponse
import com.velectico.rbm.complaint.model.ComplainListDetails
import com.velectico.rbm.databinding.FragmentProductDetailsListBinding
import com.velectico.rbm.databinding.FragmentProductListBinding
import com.velectico.rbm.menuitems.viewmodel.MenuViewModel
import com.velectico.rbm.utils.MECHANIC_ROLE
import kotlinx.android.synthetic.main.fragment_product_details_list.*

/**
 * A simple [Fragment] subclass.
 */
class ProductDetailsListFragment : BaseFragment() {

    private lateinit var binding: FragmentProductDetailsListBinding
    var productDetail = CreateOrderListDetails()
    var schmDetail = PSM_Scheme_DetailsResponse()
    private lateinit var menuViewModel: MenuViewModel
    override fun getLayout(): Int {
        return R.layout.fragment_product_details_list
    }

    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentProductDetailsListBinding
        productDetail = arguments!!.get("productDetails")  as CreateOrderListDetails
        menuViewModel = MenuViewModel.getInstance(activity as BaseActivity)
        binding.tvProdName.text = productDetail.PM_Seg_Name
        binding.tvTypes.text = productDetail.PM_Type_Name
        binding.tvProdCat.text = productDetail.PM_Cat_Name
        binding.tvUOM.text = productDetail.PM_UOM_Detail
        binding.tvBrand.text = productDetail.PM_Brand_name
        binding.tvMrp.text = productDetail.PM_MRP
        binding.tvHsnNo.text = productDetail.PM_HSN
        binding.tvGstNo.text = productDetail.PM_GST_Perc +"%"
        binding.tvDiscPrice.text = "₹ " +productDetail.PM_Disc_Price
        binding.tvSplPrice.text = "₹ " +productDetail.PM_Net_Price
        binding.gst.text = productDetail.PM_Feature
        //binding.tvSchemeName.text = schmDetail.schemeName
        Picasso.get().load(productDetail.PM_Image_Path).fit().into(binding.ivProdImageUrl)
        //Picasso.get().load(schmDetail.imagePath).fit().into(binding.ivSchemeImageUrl)




        if(menuViewModel.loginResponse.value?.userDetails?.get(0)?.uMRole.toString() == MECHANIC_ROLE){
           binding.schemeLayout.visibility = View.GONE
            binding.tvGstNo.visibility = View.GONE
            binding.tvGstNo.visibility = View.GONE
            binding.tvMrp.visibility = View.GONE
            binding.tvDiscPrice.visibility = View.GONE
            binding.tvSplPrice.visibility = View.GONE
       }
        binding.descLayout.setOnClickListener {
            cardDescription.visibility = View.VISIBLE;
            featurecard.visibility = View.GONE;
            schemecard.visibility = View.GONE;

        }

        binding.featureLayout.setOnClickListener {
            cardDescription.visibility = View.GONE;
            featurecard.visibility = View.VISIBLE;
            schemecard.visibility = View.GONE;

        }

        binding.schemeLayout.setOnClickListener {
            cardDescription.visibility = View.GONE;
            featurecard.visibility = View.GONE;
            schemecard.visibility = View.VISIBLE;

        }




    }

}
