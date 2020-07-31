package com.velectico.rbm.beats.adapters

import android.app.Activity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.velectico.rbm.R
import com.velectico.rbm.beats.model.BeatAssignments
import com.velectico.rbm.beats.model.Beats
import com.velectico.rbm.beats.model.TaskDetails
import com.velectico.rbm.beats.views.BeatListFragmentDirections
import com.velectico.rbm.databinding.RowAssignmentListBinding
import com.velectico.rbm.databinding.RowBeatListBinding
import com.velectico.rbm.databinding.RowBeatListDatesBinding
import com.velectico.rbm.expense.adapter.ExpenseListAdapter
import com.velectico.rbm.utils.DateUtility
import com.velectico.rbm.utils.GloblalDataRepository
import java.util.*

class BeatListAdapter(var setCallback: BeatListAdapter.IBeatListActionCallBack,val visit:String) : RecyclerView.Adapter<BeatListAdapter.ViewHolder>() {

    var callBack : BeatListAdapter.IBeatListActionCallBack?=null
    var beatList = listOf<TaskDetails>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }
    inner class ViewHolder(_binding: RowBeatListBinding) : RecyclerView.ViewHolder(_binding.root) {
        val binding = _binding

        init {

            callBack = setCallback;

      }

       fun bind(beats: TaskDetails?) {
            binding.beats = beats
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RowBeatListBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
       return beatList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            holder.bind(beatList[position])
           holder.binding.navigateToTaskDetails.setOnClickListener {
                GloblalDataRepository.getInstance().scheduleId = beatList[position].schedule_id
                callBack?.moveToBeatTaskDetails(position, "1",holder.binding )
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    interface IBeatListActionCallBack{
        fun moveToBeatTaskDetails(position:Int,beatTaskId:String?,binding: RowBeatListBinding)

    }





}
