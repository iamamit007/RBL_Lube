package com.velectico.rbm.reminder.view

import android.app.DatePickerDialog
import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import com.kaopiz.kprogresshud.KProgressHUD
import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseActivity
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.model.*
import com.velectico.rbm.databinding.CreateReminderFragmentBinding
import com.velectico.rbm.menuitems.viewmodel.MenuViewModel
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.reminder.model.CreateReminderRequestParams
import com.velectico.rbm.reminder.model.CreateReminderResponse
import com.velectico.rbm.utils.*
import retrofit2.Callback
import java.text.SimpleDateFormat
import java.util.*

class CreateReminder :  BaseFragment() , DatePickerDialog.OnDateSetListener {
    private lateinit var binding: CreateReminderFragmentBinding
    var dealerId = "0"
    var distribId = "0"
    private lateinit var menuViewModel: MenuViewModel

    override fun getLayout(): Int {
        return R.layout.create_reminder_fragment
    }
    override fun init(binding: ViewDataBinding) {

        this.binding = binding as CreateReminderFragmentBinding
        menuViewModel = MenuViewModel.getInstance(activity as BaseActivity)
        if(menuViewModel.loginResponse.value?.userDetails?.get(0)?.uMRole.toString() == DEALER_ROLE){
            binding.spinner111.visibility = View.GONE
            binding.dealerList11.visibility = View.GONE
            dealerId = menuViewModel.loginResponse.value?.userDetails?.get(0)?.uMID.toString()


        }
        if(menuViewModel.loginResponse.value?.userDetails?.get(0)?.uMRole.toString() == DISTRIBUTER_ROLE){
            binding.spinner111.visibility = View.GONE
            binding.dealerList11.visibility = View.GONE
            distribId = menuViewModel.loginResponse.value?.userDetails?.get(0)?.uMID.toString()


        }
        binding.etEndDate?.setOnClickListener {
            showDatePickerDialog()
        }
        binding.btnAssignTask.setOnClickListener {
            createReminder()
        }
        val languages = resources.getStringArray(R.array.array_dealDist)

        // access the spinner

        if (binding.spinner111 != null) {
            val adapter = context?.let {
                ArrayAdapter(
                    it,
                    android.R.layout.simple_spinner_item, languages)
            }
            binding.spinner111.adapter = adapter

            binding.spinner111.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {
                    showToastMessage(languages[position])

                    if (languages[position] == "Dealer"){
                        //binding.spinnerDeal.visibility = View.VISIBLE
                        //binding.spinnerDealDis.visibility = View.GONE
                        callDealApi()
                    }
                    else{
                        //binding.spinnerDeal.visibility = View.GONE
                        //binding.spinnerDealDis.visibility = View.VISIBLE
                        callDistApi()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }
        binding.dealerList11.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View?, position: Int, id: Long) {

                if (binding.spinner111.selectedItem == "Dealer"){
                    if (dealNameList.size > 0 ){
                        val x = dealNameList[position]
                        dealerId = x.UM_ID!!


                    }

                }
                else if (binding.spinner111.selectedItem == "Distributor"){
                    if (distNameList.size > 0) {
                        val x = distNameList[position]
                        distribId = x.UM_ID!!

                    }
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }

    }
    private  var distNameList : List<DistDropdownDetails> = emptyList<DistDropdownDetails>()
    private val distNameResponse = object : NetworkCallBack<DistListResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<DistListResponse>) {
            response.data?.status?.let { status ->
                //showToastMessage(response.data.DistList.toString())
                hide()
                distNameList  = response.data.DistList
                var statList: MutableList<String> = ArrayList()
                for (i in distNameList){
                    statList.add(i.UM_Name!!)
                    distribId = i.UM_ID!!
                }
                val adapter2 = context?.let {
                    ArrayAdapter(
                        it,
                        android.R.layout.simple_spinner_item, statList)
                }

                if (binding.spinner111.selectedItem == "Distributor"){
                    binding.dealerList11.adapter = adapter2
                }

            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()
        }

    }

    fun callDistApi(){
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.distDropDownList(
            DistListRequestParams(SharedPreferenceUtils.getLoggedInUserId(context as Context))
        )

        responseCall.enqueue(distNameResponse as Callback<DistListResponse>)

    }

    private  var dealNameList : List<DealDropdownDetails> = emptyList<DealDropdownDetails>()
    private val dealNameResponse = object : NetworkCallBack<DealListResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<DealListResponse>) {
            response.data?.status?.let { status ->
                //showToastMessage(response.data.DealList.toString())
                hide()
                dealNameList  = response.data.DealList
                var statList: MutableList<String> = ArrayList()
                for (i in dealNameList){
                    statList.add(i.UM_Name!!)
                    dealerId = i.UM_ID!!
                }
                val adapter2 = context?.let {
                    ArrayAdapter(
                        it,
                        android.R.layout.simple_spinner_item, statList)
                }
                if (binding.spinner111.selectedItem == "Dealer"){
                    binding.dealerList11.adapter = adapter2
                }



            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()
        }

    }

    fun callDealApi(){
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.dealDropDownList(
            DealListRequestParams(SharedPreferenceUtils.getLoggedInUserId(context as Context))
        )

        responseCall.enqueue(dealNameResponse as Callback<DealListResponse>)

    }


    var hud: KProgressHUD? = null
    fun  showHud(){
        if (hud!=null){

            hud!!.show()
        }
    }

    fun hide(){
        hud?.dismiss()

    }

    fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val dayOfMonth = calendar[Calendar.DAY_OF_MONTH]
        DatePickerDialog(requireActivity(), this, year, month, dayOfMonth).show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val tempDate: Date = DateUtility.getDateFromYearMonthDay(year, month, dayOfMonth)
        val subDateString: String =
            DateUtility.getStringDateFromTimestamp((tempDate.time), DateUtility.dd_MM_yy)
        binding.etEndDate.setText(subDateString)
    }

    fun createReminder(){
        if (binding.etEndDate.text.toString()?.trim() == ""){
            showToastMessage("Please provide Followup Date")
        }
        else if (binding.etDesc.text.toString()?.trim() == ""){
            showToastMessage("Please provide reminder Description")
        }
        else {
            val inpFormat = SimpleDateFormat(DateUtility.dd_MM_yy, Locale.US);
            val outputformat = SimpleDateFormat("yyyy-MM-dd", Locale.US);
            val date =
                DateUtils.parseDate(binding.etEndDate.text.toString(), inpFormat, outputformat)
            val param = CreateReminderRequestParams(
                SharedPreferenceUtils.getLoggedInUserId(context as Context),
                "0",
                dealerId,
                distribId,
                date,
                binding.etDesc.text.toString()

            )
            showHud()
            val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
            val responseCall = apiInterface.addReminder(param)
            responseCall.enqueue(CreateReminderResponse as Callback<CreateReminderResponse>)
        }
    }

    private val CreateReminderResponse = object : NetworkCallBack<CreateReminderResponse>(){
        override fun onSuccessNetwork(
            data: Any?,
            response: NetworkResponse<CreateReminderResponse>
        ) {
            response.data?.respMessage?.let { status ->
                Toast.makeText(activity!!, "Reminder Added", Toast.LENGTH_SHORT)
                    .show()
                hide()
                activity!!.onBackPressed()

            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()
        }

    }

}