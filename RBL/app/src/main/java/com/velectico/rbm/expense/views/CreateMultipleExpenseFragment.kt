package com.velectico.rbm.expense.views

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.*
import android.content.pm.PackageManager
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import androidx.localbroadcastmanager.content.LocalBroadcastManager
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
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Callback
import java.io.File
import java.util.*
import kotlin.collections.HashMap

/**
 * A simple [Fragment] subclass.
 */
class CreateMultipleExpenseFragment : BaseFragment(),DatePickerDialog.OnDateSetListener,FileUploadListener {

    private var binding: FragmentCreateMultipleExpenseBinding? = null
    private lateinit var mBeatSharedViewModel: BeatSharedViewModel
    private lateinit var beatAssignmentList: MutableList<BeatAssignments>;
    private lateinit var adapter: MultipleExpenseAdapter;
    private lateinit var mBeat: Beats
    private lateinit var mAssignments : BeatAssignments
    private lateinit var fileUploadListener : FileUploadListener


    private var imageUtils : ImageUtils?=null
    private var imageUrl : String? = null
    private var cameraImgUri : Uri?= null
    var colname=""

    override fun getLayout(): Int {
        return R.layout.fragment_create_multiple_expense
    }
    var curFilepos = 0

    companion object{

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
        setupPermissions()
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

        LocalBroadcastManager.getInstance(context!!).registerReceiver(mMessageReceiver,
             IntentFilter("custom-event-name")
        );
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

                if (response.data.expenseDetails !=null){
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
                if (expId == 0){
                    binding?.imgcon?.visibility = View.VISIBLE
                    expId = response.data.expensId!!
                    binding?.ivImg1?.visibility = View.VISIBLE
                    binding?.btnCancel?.visibility = View.GONE
                    binding?.btnSave?.visibility = View.GONE
                    binding?.btnFinish?.visibility = View.VISIBLE
                }




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
        fileUploadListener = this
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
        binding?.ivImg1!!.setOnClickListener {
            colname = "recPhoto1"
            open()
        }

        binding?.ivImg2!!.setOnClickListener {
            imageUtils = null
           imageUtils = ImageUtils(context as Context,baseActivity,this)
            colname = "recPhoto2"
            open()
        }

        binding?.ivImg3!!.setOnClickListener {
            imageUtils = null
            imageUtils = ImageUtils(context as Context,baseActivity,this)
            colname = "recPhoto3"
            open()
        }

        binding?.ivImg4!!.setOnClickListener {
            imageUtils = null
            imageUtils = ImageUtils(context as Context,baseActivity,this)
            colname = "recPhoto4"
            open()
        }
        binding?.ivImg5!!.setOnClickListener {
            imageUtils = null
            imageUtils = ImageUtils(context as Context,baseActivity,this)
            colname = "recPhoto5"
            open()
        }
        binding?.ivImg6!!.setOnClickListener {
            imageUtils = null
            imageUtils = ImageUtils(context as Context,baseActivity,this)
            colname = "recPhoto6"
            open()
        }
        binding?.btnFinish?.setOnClickListener {
            activity!!.onBackPressed()
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

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(activity!!,
            Manifest.permission.READ_EXTERNAL_STORAGE)
        val campermission = ContextCompat.checkSelfPermission(activity!!,
            Manifest.permission.CALL_PHONE)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity!!,
                arrayOf(Manifest.permission.CAMERA),
                2)
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(activity!!,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            1)
        ActivityCompat.requestPermissions(activity!!,
            arrayOf(Manifest.permission.CAMERA),
            2)
    }


    // Our handler for received Intents. This will be called whenever an Intent
// with an action named "custom-event-name" is broadcasted.
    private var mMessageReceiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

                       val message = intent?.getStringExtra("message")
            Log.d("receiver $colname", "Got message: " + message)
            hide()
            when (colname) {
                "recPhoto1" -> {
                    binding?.ivImg2?.visibility = View.VISIBLE
                }
                "recPhoto2" -> {
                    binding?.ivImg3?.visibility = View.VISIBLE
                }
                "recPhoto3" -> {
                    binding?.ivImg4?.visibility = View.VISIBLE
                }
                "recPhoto4" -> {
                    binding?.ivImg5?.visibility = View.VISIBLE
                }
                "recPhoto5" -> {
                    binding?.ivImg6?.visibility = View.VISIBLE
                }

            }

        }
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            // Get extra data included in the Intent

//        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            imageUtils?.FINAL_TAKE_PHOTO ->
                if (resultCode == Activity.RESULT_OK) {
                    imageUrl = imageUtils?.imgFilePath
                    if (curFilepos == 0){
                        when (colname) {
                            "recPhoto1" -> {
                                imageUtils?.displayImage(imageUtils?.imgFilePath,binding?.ivImg1)
                            }
                            "recPhoto2" -> {
                                imageUtils?.displayImage(imageUtils?.imgFilePath,binding?.ivImg2)
                            }
                            "recPhoto3" -> {
                                imageUtils?.displayImage(imageUtils?.imgFilePath,binding?.ivImg3)
                            }
                            "recPhoto4" -> {
                                imageUtils?.displayImage(imageUtils?.imgFilePath,binding?.ivImg4)
                            }
                            "recPhoto5" -> {
                                imageUtils?.displayImage(imageUtils?.imgFilePath,binding?.ivImg5)
                            }
                            "recPhoto6" -> {
                                imageUtils?.displayImage(imageUtils?.imgFilePath,binding?.ivImg6)

                            }
                        }

                        //imageUtils?.displayImage(imageUtils?.imgFilePath,binding?.ivImg1)
                        var file =  File(imageUtils?.imgFilePath)
                        showHud()
                        someTask(file,expId.toString(),SharedPreferenceUtils.getLoggedInUserId(context as Context),colname,context!!).execute()


                    }

                }
            imageUtils?.FINAL_CHOOSE_PHOTO ->
                if (resultCode == Activity.RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= 19) {
                        imageUrl = imageUtils?.handleImageOnKitkat(data)
                      //  if (currentImage == 1){
                            imageUtils?.displayImage(imageUrl,binding?.ivImg1)

                        when (colname) {
                            "recPhoto1" -> {
                                imageUtils?.displayImage(imageUrl,binding?.ivImg1)                            }
                            "recPhoto2" -> {
                                imageUtils?.displayImage(imageUrl,binding?.ivImg2)                            }
                            "recPhoto3" -> {
                                imageUtils?.displayImage(imageUrl,binding?.ivImg3)                            }
                            "recPhoto4" -> {
                                imageUtils?.displayImage(imageUrl,binding?.ivImg4)                            }
                            "recPhoto5" -> {
                                imageUtils?.displayImage(imageUrl,binding?.ivImg5)                            }
                            "recPhoto6" -> {
                                imageUtils?.displayImage(imageUrl,binding?.ivImg6)                            }
                        }

                            var file =  File(imageUrl)
                      //  }


                    val requestBody = RequestBody.create(MediaType.parse("image/*"), file);
                            val fileupload =
                            MultipartBody.Part.createFormData("fileName", file.getName(), requestBody)
                            var userId = RequestBody.create(MediaType.parse("text/plain"), "7001507620");
                            var expId = RequestBody.create(MediaType.parse("text/plain"), "97")

                        showHud()
                            mfile = file
                            someTask(file,expId.toString(),SharedPreferenceUtils.getLoggedInUserId(context as Context),colname,context!!).execute()

                           // responseCall.enqueue(createExpenseResponse as Callback<CreateExpenseResponse>)

                        }
                        //imageUtils?.displayImage(imageUrl,binding.ivExpBill)
                    }
                }
        }





    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val tempDate: Date = DateUtility.getDateFromYearMonthDay(year, monthOfYear, dayOfMonth)
        val subDateString: String = DateUtility.getStringDateFromTimestamp((tempDate.time), DateUtility.YYYY_DASH_MM_DASH_DD)
        if (currInstance != null){
            currInstance?.setText(subDateString)}
    }


    var mfile:File? = null
    class someTask(val file: File,var exp:String?,var userId:String?,val colnName:String?,val context: Context) : AsyncTask<Void, Void, String>() {
        override fun doInBackground(vararg params: Void?): String? {
            // ...G
            GloblalDataRepository.getInstance().test(file,exp,userId,colnName,context)


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

    override fun onPictureUpload(isSuccess: Boolean?, message: String?) {
        Log.d("nnnnnn","kkkkkk")
        when (colname) {
            "recPhoto1" -> {
                binding?.ivImg2?.visibility = View.VISIBLE
            }
            "recPhoto2" -> {
                binding?.ivImg3?.visibility = View.VISIBLE
            }
            "recPhoto3" -> {
                binding?.ivImg4?.visibility = View.VISIBLE
            }
            "recPhoto4" -> {
                binding?.ivImg5?.visibility = View.VISIBLE
            }
            "recPhoto5" -> {
                binding?.ivImg6?.visibility = View.VISIBLE
            }
        }
    }

}
interface  FileUploadListener {
   fun onPictureUpload( isSuccess:Boolean?,message:String?)
}