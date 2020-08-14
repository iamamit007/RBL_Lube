package com.velectico.rbm.expense.views

import android.content.Context
import android.content.IntentFilter
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.kaopiz.kprogresshud.KProgressHUD

import com.velectico.rbm.R
import com.velectico.rbm.RBMLubricantsApplication
import com.velectico.rbm.base.views.BaseActivity
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.databinding.FragmentExpenseListBinding
import com.velectico.rbm.expense.adapter.ExpenseListAdapter
import com.velectico.rbm.expense.model.*
import com.velectico.rbm.expense.viewmodel.ExpenseViewModel
import com.velectico.rbm.menuitems.viewmodel.AttendanceRequestParams
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.utils.GloblalDataRepository
import com.velectico.rbm.utils.SharedPreferenceUtils
import retrofit2.Callback
import java.util.ArrayList

/**
 * Expense List
 */
class ExpenseListFragment : BaseFragment() {
    private lateinit var binding: FragmentExpenseListBinding
    private lateinit var expenseList : List<ExpenseDetails>
    private lateinit var adapter: ExpenseListAdapter
    private lateinit var expenseViewModel: ExpenseViewModel


    override fun getLayout(): Int {
        return R.layout.fragment_expense_list
    }

    private fun getExpenseListFromServer() {
        binding?.progressLayout?.visibility = View.VISIBLE
        expenseViewModel.expenseListAPICall(SharedPreferenceUtils.getLoggedInUserId(context as Context))
    }

    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentExpenseListBinding

        binding.fab.setOnClickListener {
           moveToCreateExpense()
        }
        observeViewModelData();

       // getExpenseListFromServer()
        initHud()
        getBeatList()
    }


    private fun setUpRecyclerView() {
        adapter = ExpenseListAdapter(object : ExpenseListAdapter.IExpenseActionCallBack{
            override fun onDelete(position: Int, expenseId: String?) {
                Log.e("test","onDelete"+expenseId)
                expenseViewModel.expenseDeleteAPICall(ExpenseDeleteRequest(SharedPreferenceUtils.getLoggedInUserId(context as Context),expenseId))
            }

            override fun onEdit(position: Int, expenseId: String?) {
                Log.e("test","onEdit"+expenseId)
            }
        });
        binding.rvExpenseList.adapter = adapter
        adapter.expenseList = expenseList
    }

    fun moveToCreateExpense(){
       //val direction : NavDirections = ExpenseListFragmentDirections.actionMoveToCreateExpense()
       //Navigation.findNavController(binding.fab).navigate(direction)
        //Temp Redirection
        val direction : NavDirections = ExpenseListFragmentDirections.actionExpenseListFragmentToCreateMultipleExpenseFragment()
        Navigation.findNavController(binding.fab).navigate(direction)

    }

    private fun observeViewModelData() {
        expenseViewModel = ExpenseViewModel.getInstance(baseActivity)
//        expenseViewModel.expenseListResponse.observe(viewLifecycleOwner, Observer { listResponse ->
//            listResponse?.let {
//                adapter?.expenseList = expenseViewModel.expenseListResponse.value?.expenseDetails as List<Expense>
//            }
//        })

        expenseViewModel.expenseDeleteResponse.observe(viewLifecycleOwner, Observer { deleteResponse ->
            deleteResponse?.let {
                Log.e("test","---->"+deleteResponse.respMessage);
            }
        })

        expenseViewModel.loading.observe(viewLifecycleOwner, Observer { progress ->
            binding?.progressLayout?.visibility = if(progress) View.VISIBLE else View.GONE
        })

        expenseViewModel.errorLiveData.observe(viewLifecycleOwner, Observer {
            ((this as BaseFragment).activity as BaseActivity).showAlertDialog(it.errorMessage ?: getString(R.string.no_data_available))
        })
    }


    var hud: KProgressHUD? = null
    fun  showHud(){
        if (hud!=null){

            hud!!.show()
        }
    }

    fun hide(){
        hud?.dismiss()

    }
    fun initHud(){
        hud =  KProgressHUD.create(activity)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel("Please wait")
            .setCancellable(true)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)

    }
    fun getBeatList(){
        // DealerDetailsRequestParams(
        //            SharedPreferenceUtils.getLoggedInUserId(context as Context),"109","61","0")
        showHud()
        val userId = if (RBMLubricantsApplication.globalRole == "Team" ){
            GloblalDataRepository.getInstance().teamUserId
        }
        else{
            SharedPreferenceUtils.getLoggedInUserId(context as Context)
        }

        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getChuttiList(
            AttendanceRequestParams(userId
            )
        )
        responseCall.enqueue(createResponse as Callback<ExpenseResponse>)

    }


    val createResponse = object : NetworkCallBack<ExpenseResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<ExpenseResponse>) {
            Log.d("cccc",response.data.toString())
            response.data?.status?.let { status ->
                hide()

                expenseList = response.data?.expenseDetails!!
                setUpRecyclerView()
                }




        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()


        }

    }

}
