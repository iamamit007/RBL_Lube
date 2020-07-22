package com.velectico.rbm.beats.views

import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.navigation.Navigation

import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseActivity
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.adapters.BeatDateListAdapter
import com.velectico.rbm.beats.model.BeatDate
import com.velectico.rbm.beats.model.ScheduleDates
import com.velectico.rbm.databinding.FragmentDateWiseBeatListBinding
import com.velectico.rbm.databinding.RowBeatListDatesBinding
import com.velectico.rbm.menuitems.viewmodel.MenuViewModel
import com.velectico.rbm.utils.SALES_LEAD_ROLE
import com.velectico.rbm.utils.SharedPreferenceUtils

/**
 * A simple [Fragment] subclass.
 */
class DateWiseBeatListFragment : BaseFragment()  {

    private lateinit var binding: FragmentDateWiseBeatListBinding
    private lateinit var menuViewModel: MenuViewModel
    private lateinit var beatDateList : List<BeatDate>
    private lateinit var adapter: BeatDateListAdapter

    override fun getLayout(): Int {
        return R.layout.fragment_date_wise_beat_list
    }


    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentDateWiseBeatListBinding
        menuViewModel = MenuViewModel.getInstance(activity as BaseActivity)
        beatDateList = BeatDate().getDummyBeatDateList()
        /*if(TEMP_CURRENT_LOGGED_IN == SALES_LEAD_ROLE){
            binding.fab.show()
            binding.fab.setOnClickListener {
                moveToCreateBeat()
            }
        }*/

        if(menuViewModel.loginResponse.value?.userDetails?.get(0)?.uMRole.toString() == SALES_LEAD_ROLE){
            binding.fab.show()
            binding.fab.setOnClickListener {
                moveToCreateBeat()
            }
        }

    }

    override fun onResume() {
        super.onResume()
        getLeaveListFromServer()
        menuViewModel.leaveListResponse.observe(viewLifecycleOwner, Observer { listResponse ->
            listResponse?.let {
               Log.d("sssss",""+listResponse.scheduleDates)
                setUpRecyclerView(listResponse.scheduleDates)
            }
        })

    }

    private fun getLeaveListFromServer() {
      //  binding?.progressLayout?.visibility = View.VISIBLE
        menuViewModel.getBeatListAPICall(SharedPreferenceUtils.getLoggedInUserId(context as Context))
    }

    private fun setUpRecyclerView(data:List<ScheduleDates>) {
        adapter = BeatDateListAdapter(object : BeatDateListAdapter.IBeatDateListActionCallBack{
            override fun moveToBeatDetails(position: Int, beatTaskId: String?,binding: RowBeatListDatesBinding) {
                Log.e("test","onAddTask"+beatTaskId)
                if(menuViewModel.loginResponse.value?.userDetails?.get(0)?.uMRole.toString() == SALES_LEAD_ROLE){
                    val navDirection =  DateWiseBeatListFragmentDirections.actionDateWiseBeatListFragmentToBeatListFragment("")
                    Navigation.findNavController(binding.navigateToDetails).navigate(navDirection)

//                    val navDirection =  TeamDashboardDirections.actionTeamDashboardToTeamListFragment(binding.beatButton.text.toString())
//                    Navigation.findNavController(binding.performanceButton).navigate(navDirection)

                }else {
                    val navDirection =  DateWiseBeatListFragmentDirections.actionDateWiseBeatListFragmentToBeatTaskDetailsViewFragment("","")
                    Navigation.findNavController(binding.navigateToDetails).navigate(navDirection)
                }

               /* if(TEMP_CURRENT_LOGGED_IN == SALES_LEAD_ROLE){
                    val navDirection =  DateWiseBeatListFragmentDirections.actionDateWiseBeatListFragmentToBeatListFragment()
                    Navigation.findNavController(binding.navigateToDetails).navigate(navDirection)
                }else {
                    val navDirection =  DateWiseBeatListFragmentDirections.actionDateWiseBeatListFragmentToBeatTaskDetailsViewFragment()
                    Navigation.findNavController(binding.navigateToDetails).navigate(navDirection)
                }*/

            }
        },data);
        binding.rvBeatListDateWise.adapter = adapter
                adapter.beatList = beatDateList
    }

    private fun moveToCreateBeat(){
        val navDirection =  DateWiseBeatListFragmentDirections.actionDateWiseBeatListFragmentToCreateBeatFragment()
        Navigation.findNavController(binding.fab).navigate(navDirection)
    }

}
