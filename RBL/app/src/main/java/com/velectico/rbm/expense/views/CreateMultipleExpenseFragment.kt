package com.velectico.rbm.expense.views

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.chivorn.smartmaterialspinner.SmartMaterialSpinner
import com.google.android.material.textfield.TextInputEditText
import com.kaopiz.kprogresshud.KProgressHUD

import com.velectico.rbm.R
import com.velectico.rbm.RBMLubricantsApplication
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.adapters.BeatAssignmentTaskListAdapter
import com.velectico.rbm.beats.model.*
import com.velectico.rbm.beats.viewmodel.BeatSharedViewModel
import com.velectico.rbm.databinding.FragmentAssignTaskForBeatBinding
import com.velectico.rbm.databinding.FragmentCreateMultipleExpenseBinding
import com.velectico.rbm.expense.adapter.MultipleExpenseAdapter
import com.velectico.rbm.expense.model.*
import com.velectico.rbm.leave.model.LeaveListModel
import com.velectico.rbm.leave.model.LeaveListRequest
import com.velectico.rbm.leave.model.LeaveListResponse
import com.velectico.rbm.leave.view.ApplyLeaveFragment
import com.velectico.rbm.leave.view.adapter.LeaveListAdapter
import com.velectico.rbm.menuitems.viewmodel.AttendanceRequestParams
import com.velectico.rbm.network.apiconstants.*
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.request.NetworkRequest
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.utils.DateUtility
import com.velectico.rbm.utils.GloblalDataRepository
import com.velectico.rbm.utils.ImageUtils
import com.velectico.rbm.utils.SharedPreferenceUtils
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import kotlinx.android.synthetic.main.fragment_beat_report.view.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Callback
import java.io.File
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class CreateMultipleExpenseFragment : BaseFragment(),DatePickerDialog.OnDateSetListener {

    private var binding: FragmentCreateMultipleExpenseBinding? = null
    private lateinit var mBeatSharedViewModel: BeatSharedViewModel
    private lateinit var beatAssignmentList: MutableList<BeatAssignments>;
    private lateinit var adapter: MultipleExpenseAdapter;
    private lateinit var mBeat: Beats
    private lateinit var mAssignments : BeatAssignments

    private var imageUtils : ImageUtils?=null
    private var imageUrl : String? = null
    private var cameraImgUri : Uri?= null
    override fun getLayout(): Int {
        return R.layout.fragment_create_multiple_expense
    }


    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentCreateMultipleExpenseBinding
        imageUtils = ImageUtils(context as Context,baseActivity,this)
      //  mBeatSharedViewModel =
       //     ViewModelProviders.of(requireActivity()).get(BeatSharedViewModel::class.java)
         beatAssignmentList = BeatAssignments().getBlankList() //mBeatSharedViewModel?.assignmentList?.value!!;
        setHasOptionsMenu(true);
        initHud()
//        getIntentData()
        setUp();
        getBeatList()
        callApi("Expense Head")
    }

    override
    fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.beat_assig_menu, menu)
        super.onCreateOptionsMenu(menu , inflater)
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
    }
    private fun showCustomDatePicker(minStartDate:String = ""){
        var now = DateUtility.dateStrToCalendar(minStartDate)
        val year = now[Calendar.YEAR]
        val dpd: DatePickerDialog = DatePickerDialog.newInstance(
            this, year,  // Initial year selection
            now[Calendar.MONTH],  // Initial month selection
            now[Calendar.DAY_OF_MONTH] // Inital day selection
        )
        val calenderMaxDate = Calendar.getInstance()
        calenderMaxDate[Calendar.YEAR] = year + 1
        dpd.minDate = now
        dpd.maxDate = calenderMaxDate
        fragmentManager?.let {
            dpd.show(it, ApplyLeaveFragment.DATE_PICKER)
        }
        val holidays = arrayOf("20-05-2020", "21-05-2020", "25-05-2020")
        val disabledDays = DateUtility.getDisabledDatesArr(holidays).toTypedArray()
        dpd.highlightedDays = disabledDays
        dpd.disabledDays = disabledDays
    }


    fun getBeatList(){
        // DealerDetailsRequestParams(
        //            SharedPreferenceUtils.getLoggedInUserId(context as Context),"109","61","0")
        showHud()
        val userId =
            SharedPreferenceUtils.getLoggedInUserId(context as Context)

        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getBitList(
            AttendanceRequestParams(userId
            )
        )
        responseCall.enqueue(createResponse as Callback<BidListResponse>)

    }

    var beatId:String? = ""
    var detailsList :List<Details> = emptyList()
    val createResponse = object : NetworkCallBack<BidListResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<BidListResponse>) {
            response.data?.status?.let { status ->
                hide()

                detailsList  = response.data.expenseDetails
                var itemList: MutableList<String> = ArrayList()
                for (i in detailsList){
                    itemList.add(i.taskName!!)
                }
                val adapter2 = context?.let {
                    ArrayAdapter(
                        it,
                        android.R.layout.simple_spinner_item, itemList)
                }
                binding?.spinnerBeatList?.adapter = adapter2
                binding?.spinnerBeatList?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(adapterView: AdapterView<*>, view: View?, position: Int, id: Long) {
                        if (detailsList[position].BSD_Dealer_ID == "0"){
                            beatId = detailsList[position].BSD_Distrib_ID
                        }else{
                            beatId = detailsList[position].BSD_Dealer_ID

                        }



                    }

                    override fun onNothingSelected(adapterView: AdapterView<*>) {}
                }

            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()


        }

    }
    var expId = 0

    private val createExpenseResponse = object : NetworkCallBack<CreateExpenseResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<CreateExpenseResponse>) {
            response.data?.status?.let { status ->

                hide()
                showToastMessage(response.data.respMessage!!)
                expId = response.data.expensId!!



            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()


        }

    }


//    private val createExpenseResponse = object : NetworkCallBack<CreateExpenseResponse>(){
//        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<CreateExpenseResponse>) {
//            response.data?.status?.let { status ->
//                expId = response.data.expenseId!!
//                hide()
//
//
//
//            }
//
//        })}

    fun callApi(type:String){

        // DealerDetailsRequestParams(
        //            SharedPreferenceUtils.getLoggedInUserId(context as Context),"109","61","0")
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.getOrdervsQualityList(
            OrderVSQualityRequestParams(type)
        )
        responseCall.enqueue(orderVSQualityResponseResponse as Callback<OrderVSQualityResponse>)

    }
    private  var dataList : List<DropdownDetails> = emptyList<DropdownDetails>()
    var statList: MutableList<String> = ArrayList()

    private val orderVSQualityResponseResponse = object : NetworkCallBack<OrderVSQualityResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<OrderVSQualityResponse>) {
            response.data?.status?.let { status ->

                hide()
                dataList  = response.data.BeatReportList
                for (i in dataList){
                    statList.add(i.Exp_Head_Name!!)
                }
                currentImage += 1
               addCalf(currentImage)


            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
            hide()


        }

    }

    override
    fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add -> {
                beatAssignmentList.add(BeatAssignments())
                adapter.beatAssignmentList = beatAssignmentList
                super.onOptionsItemSelected(item)
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

//    private fun getIntentData() {
//        val bundle = arguments
//        if (bundle != null) {
//            val args: AssignTaskForBeatFragmentArgs = AssignTaskForBeatFragmentArgs.fromBundle(bundle)
//           /* if (args != null) {
//                mBeat = args.beatInfo
//                beatAssignmentList = BeatAssignments().getBlankList();
//            }*/
//        }
//    }



    fun open(){
        // build alert dialog
        val dialogBuilder = AlertDialog.Builder(context!!)

        // set message of alert dialog
        dialogBuilder.setMessage("Do you want to open?")
            // if the dialog is cancelable
            .setCancelable(false)
            // positive button text and action
            .setPositiveButton("Camera", DialogInterface.OnClickListener {
                    dialog, id ->
                cameraImgUri = imageUtils?.getImageUri()
                imageUtils?.captureImageIntent(cameraImgUri)

            })
            // negative button text and action
            .setNegativeButton("Galary", DialogInterface.OnClickListener {
                    dialog, id -> imageUtils?.openAlbum()
            })

        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box
        alert.setTitle("Attach Expense Photos")
        // show alert dialog
        alert.show()
    }



    var currentImage = 0
    var details:MutableList<ExpDetailsRequest> = mutableListOf()

    private fun setUp() {
        binding?.btnCancel?.setOnClickListener {
            Navigation.findNavController(binding?.btnCancel as Button).popBackStack()

        }
        binding?.btnSave?.setOnClickListener {
           // for (i in 0 until currentImage - 1) {
            for (i in 0 until currentImage) {
                val view = binding?.con?.getChildAt(i)
               if (view !=null){
                   if ((view?.findViewById(R.id.et_date) as TextInputEditText) != null){
                           val et_date_val = (view?.findViewById(R.id.et_date) as TextInputEditText).text.toString()
                           val et_expense_type = (view?.findViewById(R.id.et_expense_type) as TextInputEditText).text.toString()
                           //val sp_dealerName = view.findViewById(R.id.sp_dealerName) as SmartMaterialSpinner<*>
                           val et_task = (view?.findViewById(R.id.et_task) as TextInputEditText).text?.toString()
                           val id = dataList.find { it.Exp_Head_Name == et_expense_type }
                           if (id !=null){
                               details.add(ExpDetailsRequest(id.Exp_Head_Id,et_task,"0",et_date_val))
                            }

                   }
               }

            }

            val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
            val responseCall = apiInterface.createExpense(
                ExpenseCreateRequest( SharedPreferenceUtils.getLoggedInUserId(context as Context),beatId,binding?.etName?.text.toString(),details)
            )
            responseCall.enqueue(createExpenseResponse as Callback<CreateExpenseResponse>)
        }
        binding?.ivAdd?.setOnClickListener {
            currentImage = currentImage+1
            addCalf(currentImage)
        }

    }

    var currInstance :TextInputEditText? = null

    private fun addCalf(x: Int) {
        try {
            val view =
                LayoutInflater.from(context!!)
                    .inflate(R.layout.row_multiple_expense, null)
            val et_date = view.findViewById(R.id.et_date) as TextInputEditText
            val et_expense_type = view.findViewById(R.id.et_expense_type) as TextInputEditText
            val et_task = view.findViewById(R.id.et_task) as TextInputEditText
            val iv_delete = view.findViewById(R.id.iv_delete) as ImageView
            val resonList = view.findViewById(R.id.resonList) as Spinner
            val serialNo = x
            val adapter2 = context?.let {
                ArrayAdapter(
                    it,
                    android.R.layout.simple_spinner_item,
                    statList
                )
            }
            resonList.adapter = adapter2
                 resonList.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(adapterView: AdapterView<*>, view: View?, position: Int, id: Long) {
                    et_expense_type.setText(statList[position])
                }

                override fun onNothingSelected(adapterView: AdapterView<*>) {}
            }
            et_date.setOnClickListener {
                currInstance = et_date
                showCustomDatePicker(et_date.text.toString())
            }
            binding!!.con.addView(view)
//            calf_list_container.postDelayed({
//                val item = ll_calf_layout_edit_mode_list.getChildAt(calfCount - 1)
//                (item.findViewById(R.id.et_cattle_name_number) as CustomEditText).requestFocus()
//                calf_list_container_calf.scrollToBottom()
//            }, 500)


        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            imageUtils?.FINAL_TAKE_PHOTO ->
                if (resultCode == Activity.RESULT_OK) {
                    imageUrl = imageUtils?.imgFilePath
                    if (currentImage == 1){
                      //  imageUtils?.displayImage(imageUtils?.imgFilePath,binding?.imgOne)
                    }
                   // imageUtils?.displayImage(imageUtils?.imgFilePath,binding.ivExpBill)
                }
            imageUtils?.FINAL_CHOOSE_PHOTO ->
                if (resultCode == Activity.RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= 19) {
                        imageUrl = imageUtils?.handleImageOnKitkat(data)
                        if (currentImage == 1){
                          //  imageUtils?.displayImage(imageUrl,binding?.imgOne)                        }
                        //imageUtils?.displayImage(imageUrl,binding.ivExpBill)
                    }
                }
        }
    }

//    private fun saveComplaint(){
//        if (binding.inputBatchno.text.toString()?.trim() == ""){
//            showToastMessage("Please provide a batchno")
//        }
//        else if (binding.inputQuantity.text.toString()?.trim() == ""){
//            showToastMessage("Please provide a quantity")
//        }
//        else if (binding.inputRemarks.text.toString()?.trim() == ""){
//            showToastMessage("Please provide a remarks")
//        }
//        else {
//            val userId = SharedPreferenceUtils.getLoggedInUserId(context as Context);
//            val expReq = ComplaintCreateRequest(
//                userId = userId,
//                complaintype = complnType,
//                CR_Batch_no = binding.inputBatchno.text.toString(),
//                CR_Dealer_ID = taskDetail.dealerId.toString(),
//                CR_Distrib_ID = taskDetail.distribId.toString(),
//                CR_Mechanic_ID = "0",
//                CR_Qty = binding.inputQuantity.text.toString(),
//                CR_Remarks = binding.inputRemarks.text.toString(),
//                prodName = prodName,
//                taskId = taskDetail.taskId.toString(),
//                recPhoto = if (imageUrl != null) File(imageUrl) else null
//            )
//            complaintCreateAPICall(expReq)
//            // showToastMessage("55555555555" +expReq)
//        }
//
//    }
//
//
//
//    fun complaintCreateAPICall(complainCreateRequest: ComplaintCreateRequest){
//        loading.postValue(true)
//
//        val complainCreateRequest = NetworkRequest(
//            apiName = EXPENSE_CREATE_EDIT,
//            endPoint = ENDPOINT_EXPENSE_CREATE_EDIT,
//            request = complainCreateRequest,
//            requestBody= getComplaintCreateRequestBody(complainCreateRequest)
//        )
//        networkManager.makeAsyncCall(request = complainCreateRequest, callBack = readComplaintCreateResponse)
//    }
//    private fun getComplaintCreateRequestBody(complainCreateRequest : ComplaintCreateRequest): RequestBody {
//        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
//        builder.addFormDataPart(USER_ID, complainCreateRequest.userId)
//            .addFormDataPart(COMPLAINT_TYPE, complainCreateRequest.complaintype.toString())
//            .addFormDataPart(BATCHNO, complainCreateRequest.CR_Batch_no.toString())
//            .addFormDataPart(DEALERID, complainCreateRequest.CR_Dealer_ID.toString())
//            .addFormDataPart(DISTID, complainCreateRequest.CR_Distrib_ID.toString())
//            .addFormDataPart(MECHANICID, complainCreateRequest.CR_Mechanic_ID.toString())
//            .addFormDataPart(QTY, complainCreateRequest.CR_Qty.toString())
//            .addFormDataPart(REMARKS, complainCreateRequest.CR_Remarks.toString())
//            .addFormDataPart(PRODNAME, complainCreateRequest.prodName.toString())
//            .addFormDataPart(TASKID, complainCreateRequest.taskId.toString())
//        if(complainCreateRequest.recPhoto!=null){
//            if (complainCreateRequest.recPhoto.exists()) {
//                builder.addFormDataPart(
//                    FILE_TO_UPLOAD, complainCreateRequest.recPhoto.getName(), RequestBody.create(
//                        MultipartBody.FORM, complainCreateRequest.recPhoto));
//            }
//        }
//
//        return builder.build();
//    }
//
//    private val readComplaintCreateResponse = object : NetworkCallBack<ComplaintCreateResponse>(){
//        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<ComplaintCreateResponse>) {
//            response.data?.status?.let { status ->
//                Log.e("test","status="+status)
//                if(status == 1){
//                    complainCreateResponse.value = response.data
//
//                } else{
//                    showToastMessage("Cannot create")
//                }
//            }
//            if(response.data?.status == null){
//                showToastMessage("Error getting")
//            }
//            loading.postValue(false)
//        }
//
//        override fun onFailureNetwork(data: Any?, error: NetworkError) {
//            loading.postValue(false)
//            showToastMessage("Error")
//        }
//
//    }

}

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val tempDate: Date = DateUtility.getDateFromYearMonthDay(year, monthOfYear, dayOfMonth)
        val subDateString: String = DateUtility.getStringDateFromTimestamp((tempDate.time), DateUtility.YYYY_DASH_MM_DASH_DD)
        if (currInstance != null){
            currInstance?.setText(subDateString)}
    }

}
