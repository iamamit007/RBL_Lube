package com.velectico.rbm.reminder.view

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.navigation.Navigation
import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.adapters.BeatListAdapter
import com.velectico.rbm.beats.model.Beats
import com.velectico.rbm.beats.views.BeatListFragmentDirections
import com.velectico.rbm.databinding.FragmentBeatListBinding
import com.velectico.rbm.databinding.FragmentReminderListBinding
import com.velectico.rbm.databinding.RowBeatListBinding
import com.velectico.rbm.databinding.RowReminderListBinding
import com.velectico.rbm.reminder.adapter.ReminderListAdapter
import com.velectico.rbm.reminder.model.ReminderList


class ReminderListFragment : BaseFragment() {
    private lateinit var binding: FragmentReminderListBinding;
    private lateinit var beatList : List<ReminderList>
    private lateinit var adapter: ReminderListAdapter
    override fun getLayout(): Int {
        return R.layout.fragment_reminder_list
    }

    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentReminderListBinding
        beatList = ReminderList().getDummyBeatList()
        setUpRecyclerView()
        binding.fab.setOnClickListener {
            moveToCreateReminder()
        }
    }


    private fun setUpRecyclerView() {


       adapter = ReminderListAdapter(object : ReminderListAdapter.IBeatListActionCallBack{
            override fun moveToBeatTaskDetails(position: Int, beatTaskId: String?,binding: RowReminderListBinding) {

            }
        });

        binding.rvReminderList.adapter = adapter
        adapter.beatList = beatList
    }

    private fun moveToCreateReminder(){
       val navDirection =  ReminderListFragmentDirections.actionReminderListFragmentToCreateReminder()
        Navigation.findNavController(binding.fab).navigate(navDirection)

    }


}
