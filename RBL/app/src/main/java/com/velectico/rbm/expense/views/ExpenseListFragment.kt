package com.velectico.rbm.expense.views

import android.content.Context
import android.util.Log
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.navigation.NavDirections
import androidx.navigation.Navigation

import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseActivity
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.databinding.FragmentExpenseListBinding
import com.velectico.rbm.expense.adapter.ExpenseListAdapter
import com.velectico.rbm.expense.model.Expense
import com.velectico.rbm.expense.model.ExpenseDeleteRequest
import com.velectico.rbm.expense.viewmodel.ExpenseViewModel
import com.velectico.rbm.utils.SharedPreferenceUtils

/**
 * Expense List
 */
class ExpenseListFragment : BaseFragment() {
    private lateinit var binding: FragmentExpenseListBinding
    private lateinit var expenseList : List<Expense>
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
        expenseList = Expense().getEmptyBeatList()
        setUpRecyclerView()
        binding.fab.setOnClickListener {
           moveToCreateExpense()
        }
        observeViewModelData();

        getExpenseListFromServer()
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
        expenseViewModel.expenseListResponse.observe(viewLifecycleOwner, Observer { listResponse ->
            listResponse?.let {
                adapter?.expenseList = expenseViewModel.expenseListResponse.value?.expenseDetails as List<Expense>
            }
        })

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

}
