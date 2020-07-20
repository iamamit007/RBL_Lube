package com.velectico.rbm.products.view

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseActivity
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.adapters.BeatDateListAdapter
import com.velectico.rbm.beats.model.BeatAssignments
import com.velectico.rbm.beats.views.BeatTaskDealerDetailsFragmentDirections
import com.velectico.rbm.beats.views.DateWiseBeatListFragmentDirections
import com.velectico.rbm.databinding.FragmentBeatTaskDealerDetailsBinding
import com.velectico.rbm.databinding.FragmentProductListBinding
import com.velectico.rbm.databinding.RowBeatListDatesBinding
import com.velectico.rbm.databinding.RowProductListBinding
import com.velectico.rbm.loginreg.viewmodel.LoginViewModel
import com.velectico.rbm.menuitems.viewmodel.MenuViewModel
import com.velectico.rbm.products.adapters.ProductListAdapter
import com.velectico.rbm.products.models.ProductInfo
import com.velectico.rbm.products.viewmodel.ProductViewModel
import com.velectico.rbm.utils.DEALER_ROLE
import com.velectico.rbm.utils.DISTRIBUTER_ROLE
import com.velectico.rbm.utils.MECHANIC_ROLE
import com.velectico.rbm.utils.TEMP_CURRENT_LOGGED_IN

/**
 * Fragement to display list of all the products
 */

//https://developer.android.com/topic/libraries/view-binding#kotlin
class ProductListFragment : BaseFragment() {

    private lateinit var binding: FragmentProductListBinding
    private lateinit var productViewModel : ProductViewModel
    private lateinit var adapter : ProductListAdapter
    private lateinit var menuViewModel: MenuViewModel

    override fun getLayout(): Int {
        return R.layout.fragment_product_list
    }

    override fun init(binding: ViewDataBinding) {
        //setHasOptionsMenu(true)
        this.binding = binding as FragmentProductListBinding
        menuViewModel = MenuViewModel.getInstance(activity as BaseActivity)
        setUpRecyclerView()
        observeViewModelData()
        getProductListFromServer()

      /*  if(TEMP_CURRENT_LOGGED_IN == MECHANIC_ROLE){
            binding.fab.hide()
        }*/

         if(menuViewModel.loginResponse.value?.userDetails?.get(0)?.uMRole.toString() == MECHANIC_ROLE || menuViewModel.loginResponse.value?.userDetails?.get(0)?.uMRole.toString() == DEALER_ROLE || menuViewModel.loginResponse.value?.userDetails?.get(0)?.uMRole.toString() == DISTRIBUTER_ROLE ){
             binding.fab.hide()
         }


        binding.fab.setOnClickListener {
            moveToOrder()
        }

        binding.fabFilter.setOnClickListener {
            moveToFilter()
        }


    }


  /*  override
    fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.filter_menu, menu)
        super.onCreateOptionsMenu(menu , inflater)
    }


    override
    fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_filter -> {
                val navDirection =  ProductListFragmentDirections.actionProductListToProductFilterFragment()
                Navigation.findNavController(binding.).navigate(navDirection)
                super.onOptionsItemSelected(item)
            }
            else -> super.onOptionsItemSelected(item)
        }
    }*/


    private fun getProductListFromServer() {
        binding?.progressLayout?.visibility = View.VISIBLE
        productViewModel.productListAPICall("test1")
    }

    private fun setUpRecyclerView(){
       /* adapter = ProductListAdapter(
            requireActivity()
        );*/

        adapter = ProductListAdapter(menuViewModel.loginResponse.value?.userDetails?.get(0)?.uMRole.toString(),requireActivity(),object : ProductListAdapter.IProdListActionCallBack{
            override fun moveToProdDetails(position: Int, beatTaskId: String?,binding: RowProductListBinding) {
                Log.e("test","onAddTask"+beatTaskId)
                val navDirection =  ProductListFragmentDirections.actionProductListToProductDetailsListFragment()
                Navigation.findNavController(binding.navigateToDetails).navigate(navDirection)
            }
        });


        binding?.rvProductList?.adapter = adapter
       // adapter?.data = ProductInfo().getDummyData()
    }
    private fun observeViewModelData() {
        productViewModel = ProductViewModel.getInstance(baseActivity)
        productViewModel.productListResponse.observe(this, Observer { listResponse ->
            listResponse?.let {
                adapter?.data = productViewModel.productListResponse.value?.prodDetails!!
            }
        })

        productViewModel.loading.observe(this, Observer { progress ->
            binding?.progressLayout?.visibility = if(progress) View.VISIBLE else View.GONE
        })

        productViewModel.errorLiveData.observe(this, Observer {
            (activity as BaseActivity).showAlertDialog(it.errorMessage ?: getString(R.string.no_data_available))
        })
    }

    private fun moveToOrder(){
        val navDirection =  ProductListFragmentDirections.actionProductListToCreateOrderFragment()
        Navigation.findNavController(binding.fab).navigate(navDirection)
    }

    private fun moveToFilter(){
        val navDirection =  ProductListFragmentDirections.actionProductListToProductFilterFragment()
        Navigation.findNavController(binding.fabFilter).navigate(navDirection)
    }
}
