package com.velectico.rbm.beats.views

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.DatePicker
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.chivorn.smartmaterialspinner.SmartMaterialSpinner
import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.beats.model.BeatAssignments
import com.velectico.rbm.beats.model.Beats
import com.velectico.rbm.beats.viewmodel.BeatSharedViewModel
import com.velectico.rbm.databinding.FragmentCreateBeatBinding
import com.velectico.rbm.expense.viewmodel.ExpenseViewModel
import com.velectico.rbm.order.views.OrderListFragmentDirections
import com.velectico.rbm.utils.DateUtility
import java.util.*

/**
 * UI to create Beats
 */
class CreateBeatFragment : BaseFragment() , OnDateSetListener {
    private lateinit var binding: FragmentCreateBeatBinding;
    private var cuurentDatePicketParentView : com.google.android.material.textfield.TextInputEditText? = null;
    private lateinit var mBeatSharedViewModel: BeatSharedViewModel
    private lateinit var mAssignments : MutableList<BeatAssignments>;

    override fun getLayout(): Int {
        return R.layout.fragment_create_beat
    }

    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentCreateBeatBinding;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBeatSharedViewModel = BeatSharedViewModel.getInstance(baseActivity)
        mBeatSharedViewModel.setBeat(Beats())
        mBeatSharedViewModel.beats.value?.beatAssignments = BeatAssignments().getBlankList()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addListeners();
        initSpinner();
    }

    fun addListeners(){
        //et_beat_name
        this.binding.etBeatName.setOnClickListener {
            this.binding.spBeatName.performClick()
        }
        this.binding.etAssignTo.setOnClickListener {
            this.binding.spSalesperson.performClick()
        }
        this.binding.etStartDate.setOnClickListener {
            cuurentDatePicketParentView = this.binding.etStartDate;
            showDatePickerDialog();
        }
        this.binding.etEndDate.setOnClickListener {
            cuurentDatePicketParentView = this.binding.etEndDate
            showDatePickerDialog();
        }
        this.binding.btnAssignTask.setOnClickListener{
            var errMsg = mBeatSharedViewModel.beats.value?.isValidaData();
            if(errMsg!=null){
                showToastMessage(errMsg)
            }
            else{

                // val action : CreateBeatFragmentDirections.Action = CreateBeatFragmentDirections.actionCreateBeatFragmentToAssignBeatToLocation(mBeatSharedViewModel.beats.value as Beats)
                // Navigation.findNavController(binding.btnAssignTask).navigate(action);

                val navDirection =  CreateBeatFragmentDirections.actionCreateBeatFragmentToAssignBeatToLocation()
                Navigation.findNavController(binding.btnAssignTask).navigate(navDirection)




            }

        }
    }

    //callback function to set set once date is selected from datepicker
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val tempDate: Date = DateUtility.getDateFromYearMonthDay(year, month, dayOfMonth)
        val subDateString: String =
            DateUtility.getStringDateFromTimestamp((tempDate.time), DateUtility.dd_MM_yy)
        cuurentDatePicketParentView?.setText(subDateString)
        if( cuurentDatePicketParentView == binding.etStartDate){
            mBeatSharedViewModel.beats.value?.startDate = tempDate.time
        }
        else if(cuurentDatePicketParentView == binding.etEndDate){
            mBeatSharedViewModel.beats.value?.endDate = tempDate.time
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

    private fun initSpinner() {
        //https://github.com/Chivorns/SmartMaterialSpinner
        var provinceList: MutableList<String> = ArrayList()
        provinceList.add("Region")
        provinceList.add("Zone")
        provinceList.add("District")
        provinceList.add("Area")

        binding.spSalesperson.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View?, position: Int, id: Long) {
                binding.etAssignTo.setText( provinceList[position])
                mBeatSharedViewModel.beats.value?.salesPersonId = position.toString()
                mBeatSharedViewModel.beats.value?.salesPersonName = provinceList[position]
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }

        binding.spBeatName.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View?, position: Int, id: Long) {
                binding.etBeatName.setText( provinceList[position])
                mBeatSharedViewModel.beats.value?.beatId = position.toString()
                mBeatSharedViewModel.beats.value?.beatName =  provinceList[position]
            }
            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }

        binding.spBeatName.setItem(provinceList)
        //binding.spSalesperson.setItem(provinceList)
        //https://stackoverflow.com/questions/48343622/how-to-fix-parameter-specified-as-non-null-is-null-on-rotating-screen-in-a-fragm
        binding.spSalesperson.setSelection(2)
    }
}
