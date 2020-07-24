package com.velectico.rbm.beats.views

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.navigation.Navigation

import com.velectico.rbm.R
import com.velectico.rbm.RBMLubricantsApplication
import com.velectico.rbm.base.views.BaseActivity
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.adapters.BeatDateListAdapter
import com.velectico.rbm.beats.adapters.BeatListAdapter
import com.velectico.rbm.beats.model.BeatDate
import com.velectico.rbm.databinding.FragmentDateWiseBeatListBinding
import com.velectico.rbm.databinding.FragmentOrderListBinding
import com.velectico.rbm.databinding.RowBeatListBinding
import com.velectico.rbm.databinding.RowBeatListDatesBinding
import com.velectico.rbm.menuitems.viewmodel.MenuViewModel
import com.velectico.rbm.order.adapters.OrderHeadListAdapter
import com.velectico.rbm.order.model.OrderHead
import com.velectico.rbm.order.views.OrderListFragmentDirections
import com.velectico.rbm.utils.SALES_LEAD_ROLE
import com.velectico.rbm.utils.SALES_PERSON_ROLE
import com.velectico.rbm.utils.TEMP_CURRENT_LOGGED_IN

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
        setUpRecyclerView()
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
        if (RBMLubricantsApplication.globalRole == "Team" ){
            binding.fab.hide()
        }




    }

    private fun setUpRecyclerView() {
        adapter = BeatDateListAdapter(object : BeatDateListAdapter.IBeatDateListActionCallBack{
            override fun moveToBeatDetails(position: Int, beatTaskId: String?,binding: RowBeatListDatesBinding) {
                Log.e("test","onAddTask"+beatTaskId)

                if(menuViewModel.loginResponse.value?.userDetails?.get(0)?.uMRole.toString() == SALES_LEAD_ROLE){
                    val navDirection =  DateWiseBeatListFragmentDirections.actionDateWiseBeatListFragmentToBeatListFragment()
                    Navigation.findNavController(binding.navigateToDetails).navigate(navDirection)
                }else {
                    val navDirection =  DateWiseBeatListFragmentDirections.actionDateWiseBeatListFragmentToBeatTaskDetailsViewFragment()
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
        });
        binding.rvBeatListDateWise.adapter = adapter
                adapter.beatList = beatDateList
    }

    private fun moveToCreateBeat(){
        val navDirection =  DateWiseBeatListFragmentDirections.actionDateWiseBeatListFragmentToCreateBeatFragment()
        Navigation.findNavController(binding.fab).navigate(navDirection)
    }

}
