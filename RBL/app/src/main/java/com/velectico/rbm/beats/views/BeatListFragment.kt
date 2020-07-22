package com.velectico.rbm.beats.views

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.navigation.Navigation

import com.velectico.rbm.R
import com.velectico.rbm.RBMLubricantsApplication
import com.velectico.rbm.base.model.UIError
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.adapters.BeatDateListAdapter
import com.velectico.rbm.beats.adapters.BeatListAdapter
import com.velectico.rbm.beats.model.*
import com.velectico.rbm.databinding.FragmentBeatListBinding
import com.velectico.rbm.databinding.RowBeatListBinding
import com.velectico.rbm.databinding.RowBeatListDatesBinding
import com.velectico.rbm.expense.adapter.ExpenseListAdapter
import com.velectico.rbm.expense.model.ExpenseDeleteRequest
import com.velectico.rbm.network.apiconstants.ERROR_CODE_OTHER
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.utils.SharedPreferenceUtils
import retrofit2.Callback
import retrofit2.create

/**
 * Fragment to display Beat List
 */
class BeatListFragment : BaseFragment() {
    private lateinit var binding: FragmentBeatListBinding;
    private  var beatList : List<TaskDetails> = emptyList<TaskDetails>()
    private lateinit var adapter: BeatListAdapter
    override fun getLayout(): Int {
        return R.layout.fragment_beat_list
    }

    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentBeatListBinding
      val  getstring = arguments?.getString(  "scheduleId").toString()

        callApi()
        /*binding.fab.setOnClickListener {
            moveToCreateBeat()
        }*/
    }


    private fun setUpRecyclerView() {


        adapter = BeatListAdapter(object : BeatListAdapter.IBeatListActionCallBack{
            override fun moveToBeatTaskDetails(position: Int, beatTaskId: String?,binding: RowBeatListBinding) {
                Log.e("test","onAddTask"+beatTaskId)
                val navDirection =  BeatListFragmentDirections.actionBeatListFragmentToBeatTaskDetailsViewFragment("3255632980","75")
                Navigation.findNavController(binding.navigateToTaskDetails).navigate(navDirection)
            }
        });
        binding.rvBeatList.adapter = adapter
        adapter.beatList = beatList
    }

    fun callApi(){
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
                val responseCall = apiInterface.getTaskDetailsByBeat(GetBeatDeatilsRequestParams("3255632980","75"))
        responseCall.enqueue(readLeaveListResponse as Callback<BeatWiseTakListResponse>)
    }
    private val readLeaveListResponse = object : NetworkCallBack<BeatWiseTakListResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<BeatWiseTakListResponse>) {
            response.data?.status?.let { status ->
                Log.e("test","readLeaveListResponse status="+response.data)
                beatList.toMutableList().clear()
                if (response.data.details!!.isEmpty()){
                    showToastMessage("No data found")
                }else{
                    beatList = response.data.details!!.toMutableList()
                    setUpRecyclerView()
                }


            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {

        }

    }



    /* private fun moveToCreateBeat(){
      val navDirection =  BeatListFragmentDirections.actionMoveToCreateBeat()
         Navigation.findNavController(binding.fab).navigate(navDirection)
     }*/


}
