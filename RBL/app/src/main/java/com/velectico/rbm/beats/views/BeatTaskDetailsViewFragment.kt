package com.velectico.rbm.beats.views

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.navigation.Navigation
import com.kaopiz.kprogresshud.KProgressHUD

import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseActivity
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.adapters.BeatListAdapter
import com.velectico.rbm.beats.adapters.BeatTaskDetailsViewAdapter
import com.velectico.rbm.beats.model.*
import com.velectico.rbm.databinding.*
import com.velectico.rbm.menuitems.viewmodel.MenuViewModel
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.utils.SALES_LEAD_ROLE
import com.velectico.rbm.utils.SharedPreferenceUtils
import retrofit2.Callback

/**
 * A simple [Fragment] subclass.
 */
class BeatTaskDetailsViewFragment : BaseFragment() {

    private lateinit var binding: FragmentBeatTaskDetailsViewBinding;
    private  var beatList : List<BeatTaskDetails> = emptyList()
    private lateinit var menuViewModel: MenuViewModel
    private lateinit var adapter: BeatTaskDetailsViewAdapter
    override fun getLayout(): Int {
        return R.layout.fragment_beat_task_details_view
    }

    var  scheduleId = ""
    var  userId = ""
    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentBeatTaskDetailsViewBinding
        menuViewModel = MenuViewModel.getInstance(activity as BaseActivity)
        if(menuViewModel.loginResponse.value?.userDetails?.get(0)?.uMRole.toString() != SALES_LEAD_ROLE){
           binding.beatSummary.visibility = View.GONE
        }
          scheduleId = arguments?.getString(  "scheduleId").toString()
          userId = arguments?.getString(  "userId").toString()
        val y = arguments!!.get("taskDetails")
        val x = y as TaskDetails
        binding.task = x



        setUpRecyclerView()
        callApi2()
        /*binding.fab.setOnClickListener {
            moveToCreateBeat()
        }*/
    }

    var hud: KProgressHUD? = null
    fun  showHud(){
        hud =  KProgressHUD.create(activity)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel("Please wait")
            .setCancellable(true)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)
            .show();
    }

    fun hide(){
        hud?.dismiss()
    }
    fun callApi2(){
        showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getScheduleTaskDetailsByBeat(BeatTaskDetailsRequestParams(
            SharedPreferenceUtils.getLoggedInUserId(context as Context),scheduleId))
        responseCall.enqueue(beatTaskDetailsListResponse as Callback<BeatTaskDetailsListResponse>)
    }


    private val beatTaskDetailsListResponse = object : NetworkCallBack<BeatTaskDetailsListResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<BeatTaskDetailsListResponse>) {
            hide()
            response.data?.status?.let { status ->
                Log.e("test222","BeatTaskDetailsListResponse status="+response.data)
                beatList.toMutableList().clear()
                if (response.data.Task_Details!!.isEmpty()){
                    showToastMessage("No data found")
                }else{
                    beatList = response.data.Task_Details!!.toMutableList()
                    setUpRecyclerView()
                }

            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()
        }

    }






    private fun setUpRecyclerView() {

       adapter = BeatTaskDetailsViewAdapter(object : BeatTaskDetailsViewAdapter.IBeatTaskDetailsViewActionCallBack{
            override fun moveToBeatTaskDetails(position: Int, beatTaskId: String?,binding: RowBeatTaskDetailsViewBinding) {
                Log.e("test","onAddTask"+beatTaskId)
                val navDirection =  BeatTaskDetailsViewFragmentDirections.actionBeatTaskDetailsViewFragmentToBeatTaskDealerDetailsFragment(beatList[position])
                Navigation.findNavController(binding.navigateToDealerDetails).navigate(navDirection)
            }
        });
        binding.rvBeatList.adapter = adapter
        adapter.beatList = beatList
    }

    private fun moveToCreateBeat(){
        //val navDirection =  BeatListFragmentDirections.actionMoveToCreateBeat()
        //Navigation.findNavController(binding.fab).navigate(navDirection)
    }


}
