package com.velectico.rbm.payment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.velectico.rbm.beats.model.PaymentHistoryDetails
import com.velectico.rbm.databinding.RowBeatPaymentListBinding
import com.velectico.rbm.databinding.RowPaymentListBinding
import com.velectico.rbm.payment.models.PaymentInfo
import com.velectico.rbm.payment.view.PaymentHistoryList
import com.velectico.rbm.utils.DateUtils
import java.text.SimpleDateFormat
import java.util.*

class BeatPaymentListAdapter : RecyclerView.Adapter<BeatPaymentListAdapter.ViewHolder>() {

    var beatList = listOf<PaymentHistoryDetails>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(_binding: RowBeatPaymentListBinding) :
        RecyclerView.ViewHolder(_binding.root) {
        val binding = _binding

        init {


        }

        fun bind(beats: PaymentHistoryDetails?) {
            binding.paymentInfo = beats
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RowBeatPaymentListBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return beatList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(beatList[position])
        holder.binding.tvPaymentDate
        val inpFormat =  SimpleDateFormat("yyyy-MM-dd", Locale.US);
        val  outputformat =  SimpleDateFormat("dd-MMM-yy", Locale.US);
        val stdate =  DateUtils.parseDate(beatList[position].payDate,inpFormat,outputformat)
        holder.binding.tvPaymentDate.text = stdate
        holder.binding.tvInvoice.text = beatList[position].PH_Invoice_No
        holder.binding.tvPaymentAmount.text = "₹ "+beatList[position].tran_amount

    }



}