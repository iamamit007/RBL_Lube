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
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.adapters.BeatDateListAdapter
import com.velectico.rbm.beats.adapters.BeatListAdapter
import com.velectico.rbm.beats.model.Beats
import com.velectico.rbm.beats.model.Dealer
import com.velectico.rbm.databinding.FragmentBeatListBinding
import com.velectico.rbm.databinding.RowBeatListBinding
import com.velectico.rbm.databinding.RowBeatListDatesBinding
import com.velectico.rbm.expense.adapter.ExpenseListAdapter
import com.velectico.rbm.expense.model.ExpenseDeleteRequest
import com.velectico.rbm.utils.SharedPreferenceUtils

/**
 * Fragment to display Beat List
 */
class BeatListFragment : BaseFragment() {
    private lateinit var binding: FragmentBeatListBinding;
    private lateinit var beatList : List<Beats>
    private lateinit var adapter: BeatListAdapter
    override fun getLayout(): Int {
        return R.layout.fragment_beat_list
    }

    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentBeatListBinding
        beatList = Beats().getDummyBeatList()
        setUpRecyclerView()
        /*binding.fab.setOnClickListener {
            moveToCreateBeat()
        }*/
    }


    private fun setUpRecyclerView() {


        adapter = BeatListAdapter(object : BeatListAdapter.IBeatListActionCallBack{
            override fun moveToBeatTaskDetails(position: Int, beatTaskId: String?,binding: RowBeatListBinding) {
                Log.e("test","onAddTask"+beatTaskId)
                val navDirection =  BeatListFragmentDirections.actionBeatListFragmentToBeatTaskDetailsViewFragment()
                Navigation.findNavController(binding.navigateToTaskDetails).navigate(navDirection)
            }
        });
        binding.rvBeatList.adapter = adapter
        adapter.beatList = beatList
    }

   /* private fun moveToCreateBeat(){
     val navDirection =  BeatListFragmentDirections.actionMoveToCreateBeat()
        Navigation.findNavController(binding.fab).navigate(navDirection)
    }*/


}
