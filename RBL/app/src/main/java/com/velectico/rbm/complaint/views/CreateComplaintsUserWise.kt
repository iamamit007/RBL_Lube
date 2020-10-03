package com.velectico.rbm.complaint.views

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.Navigation
import com.kaopiz.kprogresshud.KProgressHUD
import com.squareup.picasso.Picasso
import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseActivity
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.model.*
import com.velectico.rbm.complaint.model.ComplainListDetails
import com.velectico.rbm.databinding.FragmentCreateComplaintsUserWiseBinding
import com.velectico.rbm.menuitems.viewmodel.MenuViewModel
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.manager.INetworkManager
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.utils.*
import retrofit2.Callback
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class CreateComplaintsUserWise: BaseFragment() {
    private lateinit var binding: FragmentCreateComplaintsUserWiseBinding
    private lateinit var menuViewModel: MenuViewModel
    var complainDetail = ComplainListDetails()
    val loading = MutableLiveData<Boolean>()
    //var complainCreateResponse = MutableLiveData<ComplaintCreateResponse>()

    private var imageUtils : ImageUtils?=null
    private var imageUrl : String? = null
    private var cameraImgUri : Uri?= null
    var prodName= ""
    var complnType= ""
    var dealerId = "0"
    var distribId = "0"
    var mechId = "0"
    lateinit var networkManager: INetworkManager

    override fun getLayout(): Int {
        return R.layout.fragment_create_complaints_user_wise
    }


    override fun init(binding: ViewDataBinding) {

        this.binding = binding as FragmentCreateComplaintsUserWiseBinding
        initHud()
        menuViewModel = MenuViewModel.getInstance(activity as BaseActivity)
        if(menuViewModel.loginResponse.value?.userDetails?.get(0)?.uMRole.toString() == DISTRIBUTER_ROLE ||
            menuViewModel.loginResponse.value?.userDetails?.get(0)?.uMRole.toString() == MECHANIC_ROLE ||
            menuViewModel.loginResponse.value?.userDetails?.get(0)?.uMRole.toString() == DEALER_ROLE){

            binding.spinner11.visibility = View.GONE
            binding.dealerList.visibility = View.GONE
        }
        if(menuViewModel.loginResponse.value?.userDetails?.get(0)?.uMRole.toString() == DISTRIBUTER_ROLE){
            distribId = menuViewModel.loginResponse.value?.userDetails?.get(0)?.uMID.toString()
        }
        if(menuViewModel.loginResponse.value?.userDetails?.get(0)?.uMRole.toString() == DEALER_ROLE){
            dealerId = menuViewModel.loginResponse.value?.userDetails?.get(0)?.uMID.toString()
        }
        if(menuViewModel.loginResponse.value?.userDetails?.get(0)?.uMRole.toString() == MECHANIC_ROLE){
            mechId = menuViewModel.loginResponse.value?.userDetails?.get(0)?.uMID.toString()
        }
        imageUtils = ImageUtils(context as Context,baseActivity,this)
        complainDetail = arguments!!.get("complainDetail")  as ComplainListDetails
        //showToastMessage(complainDetail.toString())
        if (complainDetail.CR_ID != null){

            binding.inputBatchno.setText(complainDetail.CR_Batch_no.toString())
            binding.inputQuantity.setText(complainDetail.CR_Qty.toString())
            binding.inputRemarks.setText(complainDetail.CR_Remarks.toString())
            binding.btnLogin.visibility = View.GONE
            binding.btnCaptureImg.visibility= View.GONE
            binding.btnSelectImg.visibility= View.GONE
            Picasso.get().load(complainDetail.imagePath).fit().into(binding.ivExpBill)
        }
        binding.btnCaptureImg.setOnClickListener {

            cameraImgUri = imageUtils?.getImageUri()
            imageUtils?.captureImageIntent(cameraImgUri)
        }
        binding.btnSelectImg.setOnClickListener {
            imageUtils?.openAlbum()
        }
        binding.btnLogin.setOnClickListener{
            saveComplaint()
        }
        callApi("Complain Types")
        callApi("Brand Name")
        binding.spinner33.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (complaintTypeList.size > 0 ){
                    val x = complaintTypeList[position]
                    complnType = x.Exp_Head_Id!!

                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }
        binding.spinner22.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (prodNameList.size > 0 ){
                    val x = prodNameList[position]
                    prodName = x.Exp_Head_Id!!

                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }

        val languages = resources.getStringArray(R.array.array_dealDist)

        // access the spinner

        if (binding.spinner11 != null) {
            val adapter = context?.let {
                ArrayAdapter(
                    it,
                    android.R.layout.simple_spinner_dropdown_item, languages)
            }
            binding.spinner11.adapter = adapter

            binding.spinner11.onItemSelectedListener = object :
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
        binding.dealerList.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View?, position: Int, id: Long) {

                if (binding.spinner11.selectedItem == "Dealer"){
                    if (dealNameList.size > 0 ){
                        val x = dealNameList[position]
                        dealerId = x.UM_ID!!


                    }

                }
                else if (binding.spinner11.selectedItem == "Distributor"){
                    if (distNameList.size > 0) {
                        val x = distNameList[position]
                        distribId = x.UM_ID!!

                    }
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            imageUtils?.FINAL_TAKE_PHOTO ->
                if (resultCode == Activity.RESULT_OK) {
                    imageUrl = imageUtils?.imgFilePath
                    imageUtils?.displayImage(imageUtils?.imgFilePath,binding.ivExpBill)
                }
            imageUtils?.FINAL_CHOOSE_PHOTO ->
                if (resultCode == Activity.RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= 19) {
                        imageUrl = imageUtils?.handleImageOnKitkat(data)
                        imageUtils?.displayImage(imageUrl,binding.ivExpBill)
                    }
                }
        }
    }

    private fun saveComplaint(){
        if (binding.inputBatchno.text.toString()?.trim() == ""){
            showToastMessage("Please provide a batchno")
        }
        else if (binding.inputQuantity.text.toString()?.trim() == ""){
            showToastMessage("Please provide a quantity")
        }
        else if (binding.inputRemarks.text.toString()?.trim() == ""){
            showToastMessage("Please provide a remarks")
        }
        else {
            showHud()
//            val options: BitmapFactory.Options = BitmapFactory.Options()
//            options.inSampleSize = 50
//            val bmpSample: Bitmap = BitmapFactory.decodeFile(File(imageUrl).getPath(), options)
//
//            //Log.d("image", sizeOf(bmpPic).toString() + "")
//
//            val out = ByteArrayOutputStream()
//            bmpSample.compress(Bitmap.CompressFormat.JPEG, 1, out)
//            val byteArray: ByteArray = out.toByteArray()
//
//            Log.d("image", (byteArray.size/1024).toString())
            val userId = SharedPreferenceUtils.getLoggedInUserId(context as Context);
            someTaskComplain(File(imageUrl),complnType,userId,binding.inputBatchno.text.toString(),dealerId,distribId,
                mechId,binding.inputQuantity.text.toString(),binding.inputRemarks.text.toString(),
                prodName,"0",context!!).execute()
        }

    }

    class someTaskComplain(val file: File,
                   var complaintype:String?,
                   var userId:String?,
                   val CR_Batch_no:String?,
                   val CR_Dealer_ID:String?,
                   val CR_Distrib_ID:String?,
                   val CR_Mechanic_ID:String?,
                   val CR_Remarks:String?,
                   val CR_Qty:String?,
                   val prodNam:String?,
                   val tskId:String?,

                   val context: Context) : AsyncTask<Void, Void, String>() {
        override fun doInBackground(vararg params: Void?): String? {
            // ...G
            GloblalDataRepository.getInstance().complainTest(file,complaintype,userId,CR_Batch_no,
                CR_Dealer_ID,
                CR_Distrib_ID,
                CR_Mechanic_ID,
                CR_Remarks,
                CR_Qty,
                prodNam,
                tskId,
                context)


            return ""
        }

        override fun onPreExecute() {
            super.onPreExecute()

            // ...
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            // ...
        }
    }

    class doAsync(val handler: () -> Unit) : AsyncTask<Void, Void, Void>() {
        init {
            execute()
        }

        override fun doInBackground(vararg params: Void?): Void? {
            handler()
            return null
        }
    }



    fun callApi(type:String){
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getOrdervsQualityList(
            OrderVSQualityRequestParams(type)
        )
        when(type){
            "Complain Types"->{
                responseCall.enqueue(complainTypeResponse as Callback<OrderVSQualityResponse>)
            }
            "Brand Name"->{
                responseCall.enqueue(prodNameResponse as Callback<OrderVSQualityResponse>)
            }


        }
    }
    private  var complaintTypeList : List<DropdownDetails> = emptyList<DropdownDetails>()
    private val complainTypeResponse = object : NetworkCallBack<OrderVSQualityResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<OrderVSQualityResponse>) {
            response.data?.status?.let { status ->

                hide()
                complaintTypeList  = response.data.BeatReportList
                var statList: MutableList<String> = ArrayList()
                for (i in complaintTypeList){
                    statList.add(i.Exp_Head_Name!!)
                }
                val adapter2 = context?.let {
                    ArrayAdapter(
                        it,
                        android.R.layout.simple_spinner_dropdown_item, statList)
                }
                binding.spinner33.adapter = adapter2


            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()
        }

    }
    private  var prodNameList : List<DropdownDetails> = emptyList<DropdownDetails>()
    private val prodNameResponse = object : NetworkCallBack<OrderVSQualityResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<OrderVSQualityResponse>) {
            response.data?.status?.let { status ->

                hide()
                prodNameList  = response.data.BeatReportList
                var statList: MutableList<String> = ArrayList()
                for (i in prodNameList){
                    statList.add(i.Exp_Head_Name!!)
                }
                val adapter2 = context?.let {
                    ArrayAdapter(
                        it,
                        android.R.layout.simple_spinner_dropdown_item, statList)
                }
                binding.spinner22.adapter = adapter2


            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()
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
                        android.R.layout.simple_spinner_dropdown_item, statList)
                }

                if (binding.spinner11.selectedItem == "Distributor"){
                    binding.dealerList.adapter = adapter2
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
                        android.R.layout.simple_spinner_dropdown_item, statList)
                }
                if (binding.spinner11.selectedItem == "Dealer"){
                    binding.dealerList.adapter = adapter2
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
    fun initHud(){
        hud =  KProgressHUD.create(activity)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel("Please wait")
            .setCancellable(true)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)

        LocalBroadcastManager.getInstance(context!!).registerReceiver(mMessageReceiver,
            IntentFilter("custom-event-name")
        );
    }
    private var mMessageReceiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

            val message = intent?.getStringExtra("message")
            //Log.d("receiver $colname", "Got message: " + message)
            hide()
            showToastMessage("Image and data ara inserted successfully")
            val navDirection = CreateComplaintsUserWiseDirections.actionCreateComplaintsUserWiseToComplaintList()
            Navigation.findNavController(binding.btnLogin).navigate(navDirection)

        }

    }
}