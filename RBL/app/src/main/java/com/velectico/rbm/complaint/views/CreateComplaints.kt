package com.velectico.rbm.complaint.views

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.MutableLiveData
import com.kaopiz.kprogresshud.KProgressHUD
import com.squareup.picasso.Picasso

import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseActivity
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.model.*
import com.velectico.rbm.complaint.model.ComplainListDetails
import com.velectico.rbm.databinding.FragmentCreateComplaintsBinding
import com.velectico.rbm.expense.model.ComplaintCreateRequest
import com.velectico.rbm.expense.model.ComplaintCreateResponse
import com.velectico.rbm.menuitems.viewmodel.MenuViewModel
import com.velectico.rbm.network.apiconstants.*
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.manager.INetworkManager
import com.velectico.rbm.network.manager.getNetworkManager
import com.velectico.rbm.network.request.NetworkRequest
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.utils.ImageUtils
import com.velectico.rbm.utils.SharedPreferenceUtils
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Callback
import java.io.File
import java.util.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class CreateComplaints : BaseFragment() {
    private lateinit var binding: FragmentCreateComplaintsBinding
    private lateinit var menuViewModel: MenuViewModel
    var taskDetail = BeatTaskDetails()
    var dlrDtl = DealerDetails()
    var complainDetail = ComplainListDetails()
    val loading = MutableLiveData<Boolean>()
    var complainCreateResponse = MutableLiveData<ComplaintCreateResponse>()
    private val networkManager :INetworkManager
        get() {
            TODO()
        }
    private var imageUtils : ImageUtils?=null
    private var imageUrl : String? = null
    private var cameraImgUri : Uri?= null
    var prodName= ""
    var complnType= ""
    override fun getLayout(): Int {
        return R.layout.fragment_create_complaints
    }
    override fun init(binding: ViewDataBinding) {

        this.binding = binding as FragmentCreateComplaintsBinding
        imageUtils = ImageUtils(context as Context,baseActivity,this)
        menuViewModel = MenuViewModel.getInstance(activity as BaseActivity)
        complainDetail = arguments!!.get("complainDetail")  as ComplainListDetails
        showToastMessage(complainDetail.toString())
        if (complainDetail.CR_ID != null){

            binding.inputBatchno.setText(complainDetail.CR_Batch_no.toString())
            binding.inputQuantity.setText(complainDetail.CR_Qty.toString())
            binding.inputRemarks.setText(complainDetail.CR_Remarks.toString())
            binding.btnSaveComplain.visibility = View.GONE
            binding.btnCaptureImg.visibility= View.GONE
            binding.btnSelectImg.visibility= View.GONE
            Picasso.get().load(complainDetail.imagePath).fit().into(binding.ivExpBill)
        }
//        else {
//            binding.inputBatchno.setText("")
//            binding.inputQuantity.setText("")
//            binding.inputRemarks.setText("")
//        }
        taskDetail = arguments!!.get("taskDetail") as BeatTaskDetails
        dlrDtl = arguments!!.get("dealerDetail") as DealerDetails


        binding.dlrNm.text = taskDetail.dealerName.toString()
        binding.tvProdNetPrice.text = dlrDtl.dealerPhone.toString()
        binding.tvProdTotalPrice.text = dlrDtl.DM_Contact_Person.toString()
        binding.tvOrdrAmt.text = "₹" +dlrDtl.orderAmt.toString()
        binding.collectionAmt22.text = "₹" +dlrDtl.collectionAmt.toString()
        if (taskDetail.distribName != null){
            binding.gradeval.text = taskDetail.distribGrade
            binding.type.text = "Distributor"
        }
        else{
            binding.gradeval.text = taskDetail.dealerGrade
            binding.type.text = "Dealer"
        }
        binding.btnCaptureImg.setOnClickListener {
            cameraImgUri = imageUtils?.getImageUri()
            imageUtils?.captureImageIntent(cameraImgUri)
        }
        binding.btnSelectImg.setOnClickListener {
            imageUtils?.openAlbum()
        }
        binding.btnSaveComplain.setOnClickListener{
            saveComplaint()
        }
        callApi("Complain Types")
        callApi("Brand Name")
        binding.spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (complaintTypeList.size > 0 ){
                    val x = complaintTypeList[position]
                    complnType = x.Exp_Head_Id!!

                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (prodNameList.size > 0 ){
                    val x = prodNameList[position]
                    prodName = x.Exp_Head_Id!!

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
           val userId = SharedPreferenceUtils.getLoggedInUserId(context as Context);
           val expReq = ComplaintCreateRequest(
               userId = userId,
               complaintype = complnType,
               CR_Batch_no = binding.inputBatchno.text.toString(),
               CR_Dealer_ID = taskDetail.dealerId.toString(),
               CR_Distrib_ID = taskDetail.distribId.toString(),
               CR_Mechanic_ID = "0",
               CR_Qty = binding.inputQuantity.text.toString(),
               CR_Remarks = binding.inputRemarks.text.toString(),
               prodName = prodName,
               taskId = taskDetail.taskId.toString(),
               recPhoto = if (imageUrl != null) File(imageUrl) else null
           )
           complaintCreateAPICall(expReq)
           showToastMessage("55555555555" +expReq)
       }

    }

    fun complaintCreateAPICall(complainCreateRequest: ComplaintCreateRequest){
        loading.postValue(true)

        val complainCreateRequest = NetworkRequest(
            apiName = EXPENSE_CREATE_EDIT,
            endPoint = ENDPOINT_EXPENSE_CREATE_EDIT,
            request = complainCreateRequest,
            requestBody= getComplaintCreateRequestBody(complainCreateRequest)
        )
        networkManager.makeAsyncCall(request = complainCreateRequest, callBack = readComplaintCreateResponse)
    }
    private fun getComplaintCreateRequestBody(complainCreateRequest : ComplaintCreateRequest): RequestBody {
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        builder.addFormDataPart(USER_ID, complainCreateRequest.userId)
            .addFormDataPart(COMPLAINT_TYPE, complainCreateRequest.complaintype.toString())
            .addFormDataPart(BATCHNO, complainCreateRequest.CR_Batch_no.toString())
            .addFormDataPart(DEALERID, complainCreateRequest.CR_Dealer_ID.toString())
            .addFormDataPart(DISTID, complainCreateRequest.CR_Distrib_ID.toString())
            .addFormDataPart(MECHANICID, complainCreateRequest.CR_Mechanic_ID.toString())
            .addFormDataPart(QTY, complainCreateRequest.CR_Qty.toString())
            .addFormDataPart(REMARKS, complainCreateRequest.CR_Remarks.toString())
            .addFormDataPart(PRODNAME, complainCreateRequest.prodName.toString())
            .addFormDataPart(TASKID, complainCreateRequest.taskId.toString())
        if(complainCreateRequest.recPhoto!=null){
            if (complainCreateRequest.recPhoto.exists()) {
                builder.addFormDataPart(
                    FILE_TO_UPLOAD, complainCreateRequest.recPhoto.getName(), RequestBody.create(
                        MultipartBody.FORM, complainCreateRequest.recPhoto));
            }
        }

        return builder.build();
    }

    private val readComplaintCreateResponse = object : NetworkCallBack<ComplaintCreateResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<ComplaintCreateResponse>) {
            response.data?.status?.let { status ->
                Log.e("test","status="+status)
                if(status == 1){
                    complainCreateResponse.value = response.data

                } else{
                    showToastMessage("Cannot create")
                }
            }
            if(response.data?.status == null){
                showToastMessage("Error getting")
            }
            loading.postValue(false)
        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            loading.postValue(false)
            showToastMessage("Error")
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
                        android.R.layout.simple_spinner_item, statList)
                }
                binding.spinner2.adapter = adapter2


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
                        android.R.layout.simple_spinner_item, statList)
                }
                binding.spinner.adapter = adapter2


            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()
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
}
