package com.velectico.rbm.payment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.velectico.rbm.beats.model.PaymentDetails
import com.velectico.rbm.beats.model.PaymentHistoryDetails
import com.velectico.rbm.databinding.RowBeatPaymentListBinding
import com.velectico.rbm.databinding.RowOutstandingPaymentListBinding
import com.velectico.rbm.databinding.RowPaymentListBinding
import com.velectico.rbm.payment.models.PaymentInfo
import com.velectico.rbm.payment.view.PaymentHistoryList
import com.velectico.rbm.utils.DateUtils
import java.text.SimpleDateFormat
import java.util.*

class OutstandingPaymentListAdapter : RecyclerView.Adapter<OutstandingPaymentListAdapter.ViewHolder>() {

    var beatList = listOf<PaymentDetails>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(_binding: RowOutstandingPaymentListBinding) :
        RecyclerView.ViewHolder(_binding.root) {
        val binding = _binding

        init {


        }

        fun bind(beats: PaymentDetails?) {
            binding.paymentInfo = beats
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RowOutstandingPaymentListBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return beatList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(beatList[position])
        val inpFormat =  SimpleDateFormat("yyyy-MM-dd", Locale.US);
        val  outputformat =  SimpleDateFormat("dd-MMM-yy", Locale.US);
        val stdate =  DateUtils.parseDate(beatList[position].invoiceDate,inpFormat,outputformat)
        holder.binding.tvPaymentDate.text = stdate
        holder.binding.tvInvoice.text = beatList[position].SIH_Invoice_No
        holder.binding.tvPaymentAmount.text = "₹ "+beatList[position].Paid_Amount
        holder.binding.tvDueAmount.text = "₹ "+beatList[position].Due_Amount

    }



}