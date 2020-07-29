package com.velectico.rbm.beats.views

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import com.google.gson.annotations.SerializedName
import com.kaopiz.kprogresshud.KProgressHUD

import com.velectico.rbm.R
import com.velectico.rbm.beats.model.*
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.utils.DateUtility
import com.velectico.rbm.utils.DateUtils
import com.velectico.rbm.utils.SharedPreferenceUtils
import kotlinx.android.synthetic.main.fragment_beat_report.view.*
import retrofit2.Callback
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class BeatReportFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    private lateinit var rootView: View
    var orderstat = ""
    var paymentStat= ""
    var complain= ""
    var prod_quality= ""
    var prod_price= ""
    var prod_pakaging= ""
    var prod_turn= ""
    var prod_pref= ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_beat_report, container, false)

        initSpinner()
        callApi("Complain vs Quality")
        callApi("Packaging")
        callApi("Packaging")
        callApi("Preference Make")
        callApi("Prices problem")
        callApi("Turnover range")

        return rootView
    }

   fun initSpinner(){

       rootView.et_followupdate?.setOnClickListener {
           showDatePickerDialog()
       }
       rootView.btn_submit.setOnClickListener {
           createBeatReport()
       }
       hud =  KProgressHUD.create(activity)
           .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
           .setLabel("Please wait")
           .setCancellable(true)
           .setAnimationSpeed(2)
           .setDimAmount(0.5f)

       var provinceList: MutableList<String> = ArrayList()
       provinceList.add("open")
       provinceList.add("close")
       provinceList.add("cancel")
       val adapter = context?.let {
           ArrayAdapter(
               it,
               android.R.layout.simple_spinner_item, provinceList)
       }
       rootView.spinnerOrderNotReceived.adapter = adapter
       rootView.spinnerOrderNotReceived.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
           override fun onItemSelected(adapterView: AdapterView<*>, view: View?, position: Int, id: Long) {
               orderstat = when {
                   provinceList[position] =="open" -> {
                       "o"
                   }
                   provinceList[position] =="close" -> {
                       "c"
                   }
                   else -> {
                       "l"
                   }
               }

           }

           override fun onNothingSelected(adapterView: AdapterView<*>) {}
       }




       var statList: MutableList<String> = ArrayList()
       statList.add("initiated")
       statList.add("confirmed")
       statList.add("no status")

       val adapter2 = context?.let {
           ArrayAdapter(
               it,
               android.R.layout.simple_spinner_item, statList)
       }
       rootView.paymentspinner.adapter = adapter2
        rootView.paymentspinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
           override fun onItemSelected(adapterView: AdapterView<*>, view: View?, position: Int, id: Long) {
               paymentStat = when {
                   statList[position] =="initiated" -> {
                       "i"
                   }
                   statList[position] =="confirmed" -> {
                       "c"
                   }
                   else -> {
                       "n"
                   }
               }
           }

           override fun onNothingSelected(adapterView: AdapterView<*>) {}
       }

       rootView.spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
           override fun onItemSelected(adapterView: AdapterView<*>, view: View?, position: Int, id: Long) {
               if (dataList.size > 0 ){
                   val x = dataList[position]
                   complain = x.Exp_Head_Id!!

               }
           }

           override fun onNothingSelected(adapterView: AdapterView<*>) {}
       }

       rootView.pkgProbReason.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
           override fun onItemSelected(adapterView: AdapterView<*>, view: View?, position: Int, id: Long) {
               if (pakagedataList.size > 0 ){
                   val x = pakagedataList[position]
                   prod_pakaging = x.Exp_Head_Id!!

               }
           }

           override fun onNothingSelected(adapterView: AdapterView<*>) {}
       }

       rootView.prefProbReason.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
           override fun onItemSelected(adapterView: AdapterView<*>, view: View?, position: Int, id: Long) {
               if (prefdataList.size > 0 ){
                   val x = prefdataList[position]
                   prod_pref = x.Exp_Head_Id!!

               }
           }

           override fun onNothingSelected(adapterView: AdapterView<*>) {}
       }

       rootView.spinnerTurnOverRange.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
           override fun onItemSelected(adapterView: AdapterView<*>, view: View?, position: Int, id: Long) {
               if (turnoverdataList.size > 0 ){
                   val x = turnoverdataList[position]
                   prod_turn = x.Exp_Head_Id!!

               }
           }

           override fun onNothingSelected(adapterView: AdapterView<*>) {}
       }
       rootView.priceProbReason.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
           override fun onItemSelected(adapterView: AdapterView<*>, view: View?, position: Int, id: Long) {
               if (pricesdataList.size > 0 ){
                   val x = pricesdataList[position]
                   prod_price = x.Exp_Head_Id!!

               }
           }

           override fun onNothingSelected(adapterView: AdapterView<*>) {}
       }


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
    fun callApi(type:String){

        // DealerDetailsRequestParams(
        //            SharedPreferenceUtils.getLoggedInUserId(context as Context),"109","61","0")
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getOrdervsQualityList(
            OrderVSQualityRequestParams(type))
        when(type){
           "Complain vs Quality"->{
               responseCall.enqueue(orderVSQualityResponseResponse as Callback<OrderVSQualityResponse>)
           }
            "Packaging"->{
                responseCall.enqueue(pakageResponseResponse as Callback<OrderVSQualityResponse>)
            }
            "Preference Make"->{
                responseCall.enqueue(prefResponseResponse as Callback<OrderVSQualityResponse>)

            }
            "Turnover range"->{
                responseCall.enqueue(turnoverResponse as Callback<OrderVSQualityResponse>)

            }
            "Prices problem"->{
                responseCall.enqueue(priceResponse as Callback<OrderVSQualityResponse>)

            }

        }
    }
    private  var dataList : List<DropdownDetails> = emptyList<DropdownDetails>()
    private val orderVSQualityResponseResponse = object : NetworkCallBack<OrderVSQualityResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<OrderVSQualityResponse>) {
            response.data?.status?.let { status ->

                hide()
             dataList  = response.data.BeatReportList
                var statList: MutableList<String> = ArrayList()
                for (i in dataList){
                    statList.add(i.Exp_Head_Name!!)
                }
                val adapter2 = context?.let {
                    ArrayAdapter(
                        it,
                        android.R.layout.simple_spinner_item, statList)
                }
                rootView.spinner2.adapter = adapter2


            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()


        }

    }

    private  var pakagedataList : List<DropdownDetails> = emptyList<DropdownDetails>()
    private val pakageResponseResponse = object : NetworkCallBack<OrderVSQualityResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<OrderVSQualityResponse>) {
            response.data?.status?.let { status ->

                hide()
                pakagedataList  = response.data.BeatReportList
                var statList: MutableList<String> = ArrayList()
                for (i in pakagedataList){
                    statList.add(i.Exp_Head_Name!!)
                }
                val adapter2 = context?.let {
                    ArrayAdapter(
                        it,
                        android.R.layout.simple_spinner_item, statList)
                }
                rootView.pkgProbReason.adapter = adapter2


            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()
        }

    }


    private  var prefdataList : List<DropdownDetails> = emptyList<DropdownDetails>()
    private val prefResponseResponse = object : NetworkCallBack<OrderVSQualityResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<OrderVSQualityResponse>) {
            response.data?.status?.let { status ->

                hide()
                prefdataList  = response.data.BeatReportList
                var statList: MutableList<String> = ArrayList()
                for (i in prefdataList){
                    statList.add(i.Exp_Head_Name!!)
                }
                val adapter2 = context?.let {
                    ArrayAdapter(
                        it,
                        android.R.layout.simple_spinner_item, statList)
                }
                rootView.prefProbReason.adapter = adapter2


            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()
        }

    }

    private  var turnoverdataList : List<DropdownDetails> = emptyList<DropdownDetails>()
    private val turnoverResponse = object : NetworkCallBack<OrderVSQualityResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<OrderVSQualityResponse>) {
            response.data?.status?.let { status ->

                hide()
                turnoverdataList  = response.data.BeatReportList
                var statList: MutableList<String> = ArrayList()
                for (i in turnoverdataList){
                    statList.add(i.Exp_Head_Name!!)
                }
                val adapter2 = context?.let {
                    ArrayAdapter(
                        it,
                        android.R.layout.simple_spinner_item, statList)
                }
                rootView.spinnerTurnOverRange.adapter = adapter2


            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()
        }

    }


    private  var pricesdataList : List<DropdownDetails> = emptyList<DropdownDetails>()
    private val priceResponse = object : NetworkCallBack<OrderVSQualityResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<OrderVSQualityResponse>) {
            response.data?.status?.let { status ->

                hide()
                pricesdataList  = response.data.BeatReportList
                var statList: MutableList<String> = ArrayList()
                for (i in pricesdataList){
                    statList.add(i.Exp_Head_Name!!)
                }
                val adapter2 = context?.let {
                    ArrayAdapter(
                        it,
                        android.R.layout.simple_spinner_item, statList)
                }
                rootView.priceProbReason.adapter = adapter2


            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()
        }

    }

    //Function to show date picker dialog box for start and end date
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
        rootView.et_followupdate?.setText(subDateString)
    }


    fun createBeatReport(){
        if (rootView.et_followupdate.text.toString().isEmpty()){
            Toast.makeText(activity,"Date is Manadatory",Toast.LENGTH_SHORT).show()
            return
        }
        val inpFormat =  SimpleDateFormat(DateUtility.dd_MM_yy, Locale.US);
        val  outputformat =  SimpleDateFormat("yyyy-MM-dd", Locale.US);
       val date =  DateUtils.parseDate(rootView.et_followupdate.text.toString(),inpFormat,outputformat)
      val param =  CreateBeatReportRequestParams(
          SharedPreferenceUtils.getLoggedInUserId(context as Context),
          "123",
          "66",
          "0",
          orderstat,
          paymentStat,
          complain,prod_price,prod_pakaging,prod_pref,prod_turn,date,""

        )
        // DealerDetailsRequestParams(
        //            SharedPreferenceUtils.getLoggedInUserId(context as Context),"109","61","0")
        showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.createBeatReport(param)
        responseCall.enqueue(cretaeBeatResponse as Callback<CreateBeatReportResponse>)

    }

    private val cretaeBeatResponse = object : NetworkCallBack<CreateBeatReportResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<CreateBeatReportResponse>) {
            response.data?.respMessage?.let { status ->
                Toast.makeText(activity!!, "${response.data?.respMessage}", Toast.LENGTH_SHORT)
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
//spinnerTurnOverRange