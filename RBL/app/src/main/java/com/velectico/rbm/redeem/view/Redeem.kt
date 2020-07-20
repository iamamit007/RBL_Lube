package com.velectico.rbm.redeem.view

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.velectico.rbm.R
import com.velectico.rbm.RedeemViewModel
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.databinding.RedeemFragmentBinding
import com.velectico.rbm.databinding.RowRedeemListBinding
import com.velectico.rbm.redeem.adapter.RedeemListAdapter
import com.velectico.rbm.redeem.model.RedeemInfo


class Redeem : BaseFragment() {
    private lateinit var binding: RedeemFragmentBinding;
    private lateinit var beatList : List<RedeemInfo>
    private lateinit var adapter: RedeemListAdapter
    override fun getLayout(): Int {
        return R.layout.redeem_fragment
    }

    override fun init(binding: ViewDataBinding) {
        this.binding = binding as RedeemFragmentBinding
        beatList = RedeemInfo().getDummyBeatList()
        setUpRecyclerView()
        /*binding.fab.setOnClickListener {
            moveToCreateBeat()
        }*/
    }


    private fun setUpRecyclerView() {


        adapter = RedeemListAdapter(object : RedeemListAdapter.IBeatListActionCallBack{
            override fun moveToBeatTaskDetails(position: Int, beatTaskId: String?,binding: RowRedeemListBinding) {

            }
        });

        binding.rvRedeemList.adapter = adapter
        adapter.beatList = beatList
    }
}
