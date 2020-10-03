package com.velectico.rbm.navdrawer.views

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.core.view.get
import androidx.databinding.ViewDataBinding
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.velectico.rbm.base.views.BaseActivity
import com.velectico.rbm.menuitems.viewmodel.MenuViewModel
import com.google.android.material.navigation.NavigationView
import com.velectico.rbm.R
import com.velectico.rbm.RBMLubricantsApplication
import com.velectico.rbm.databinding.ActivityDashboardBinding
import com.velectico.rbm.loginreg.model.LoginResponse
import com.velectico.rbm.menuitems.viewmodel.AttendancResponse
import com.velectico.rbm.menuitems.viewmodel.AttendanceRequestParams
import com.velectico.rbm.network.callbacks.NetworkCallBack
import com.velectico.rbm.network.callbacks.NetworkError
import com.velectico.rbm.network.manager.ApiClient
import com.velectico.rbm.network.manager.ApiInterface
import com.velectico.rbm.network.response.NetworkResponse
import com.velectico.rbm.utils.*
import retrofit2.Callback
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by mymacbookpro on 2020-04-26
 * Dashboard of the project
 */
class DashboardActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener{

    private lateinit var binding: ActivityDashboardBinding
    lateinit var toolbar: Toolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navController: NavController
    private lateinit var navigationView: NavigationView
    private lateinit var menuViewModel: MenuViewModel
    private var loginResponse:LoginResponse? = null

    override fun getLayout(): Int  = R.layout.activity_dashboard

    override fun init(savedInstanceState: Bundle?, binding: ViewDataBinding) {
        this.binding = binding as ActivityDashboardBinding
        RBMLubricantsApplication.fromBeat = ""
        RBMLubricantsApplication.globalRole = ""
        intent?.let {
            loginResponse = it.getParcelableExtra((EXTRA_LOGIN_RESPONSE))
            Log.e("test","loginResponse-->"+loginResponse)

           // Log.e("test","loginResponse-->"+loginResponse?.status)
        }
        setUp()
    }

    private fun setUp() {
        toolbar = binding.toolbar as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        drawerLayout = binding.drawerLayout
        navigationView = binding.navigationView
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)

        menuViewModel = MenuViewModel.getInstance(this)
        //menuViewModel.readAllResources() //Another example of API Call
        menuViewModel.readAllUsers()
        menuViewModel.loginResponse.value = loginResponse

        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        NavigationUI.setupWithNavController(navigationView, navController)
        navigationView.setNavigationItemSelectedListener(this)
        navigationView.menu.removeItem(R.id.nav_order)

        if(menuViewModel.loginResponse?.value?.userDetails?.get(0)?.uMRole.toString().equals("M")){
            navigationView.menu.removeItem(R.id.nav_leave)
            navigationView.menu.removeItem(R.id.nav_order)
        }

        manageNavDrawerPrivilege(menuViewModel.loginResponse.value?.userDetails?.get(0)?.uMRole.toString())

    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(Navigation.findNavController(this, R.id.nav_host_fragment), drawerLayout)
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        menuItem.isChecked = true
        drawerLayout.closeDrawers()

        when (menuItem.itemId) {
           /* R.id.first -> navController.navigate(R.id.firstFragment)

            R.id.second -> navController.navigate(R.id.secondFragment)

            R.id.third -> navController.navigate(R.id.thirdFragment)*/
            R.id.nav_home -> navController.navigate((R.id.homeFragment))

            R.id.nav_product -> gotoorderFilter()//navController.navigate(R.id.productFilterFragment)

            R.id.nav_beat -> navController.navigate(R.id.dateWiseBeatListFragment)

            R.id.nav_leave -> navController.navigate(R.id.leaveListFragment)

            R.id.nav_expense -> navController.navigate(R.id.expenseListFragment)

            R.id.nav_myprofile -> navController.navigate(R.id.userProfileFragment)

            //R.id.nav_order -> navController.navigate(R.id.orderCreateFragment)
            //R.id.nav_order -> navController.navigate(R.id.orderListFragment)
            R.id.nav_payment -> navController.navigate(R.id.fragmentPaymentList)
            R.id.nav_complaints -> navController.navigate(R.id.complaintList)
            //R.id.nav_performance -> commingSoonToast()
            R.id.nav_scanQRCode -> navController.navigate(R.id.qrcodeScanner)
            R.id.nav_logout -> finish()
        }
        return true
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }




    public fun commingSoonToast(){
        Toast.makeText(applicationContext,"Work in-progress.Feature Will be available shortly!",Toast.LENGTH_SHORT).show()
    }

     fun gotoorderFilter(){
        RBMLubricantsApplication.filterFrom = "Product"
        navController.navigate(R.id.productFilterFragment)
    }




    /*override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        if(menuViewModel.loginResponse.value?.userDetails?.get(0)?.uMRole.toString() == SALES_LEAD_ROLE || menuViewModel.loginResponse.value?.userDetails?.get(0)?.uMRole.toString() == SALES_PERSON_ROLE){
            inflater.inflate(R.menu.attendance_btn, menu)
        }

      /*  if(TEMP_CURRENT_LOGGED_IN == SALES_LEAD_ROLE || TEMP_CURRENT_LOGGED_IN == SALES_PERSON_ROLE){
            inflater.inflate(R.menu.attendance_btn, menu)
        }*/

        return true
    }*/


    override
    fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if(item.itemId == android.R.id.home){ // use android.R.id
//            drawerLayout.openDrawer(Gravity.LEFT);
//        }
        if (item.itemId == R.id.action_attandance){
            //callAttendance()
            val sdf = SimpleDateFormat("dd/M/yyyy")
            val currentDate = sdf.format(Date())
            val prevDate = SharedPreferenceUtils.getData(applicationContext!!,"LAST_ATTENDANCE")
            val format =  SimpleDateFormat("dd/M/yyyy");
            val format2 =  SimpleDateFormat("dd/M/yyyy");
            try {
                if (prevDate == "0"){
                    callAttendance()
                }else{
                    val date = format.parse(currentDate);
                    val date2 = format2.parse(prevDate);
                    val miliSeconds = date.getTime() -date2.getTime();
                    val seconds = TimeUnit.MILLISECONDS.toSeconds(miliSeconds);
                    val minute = seconds/60;
//                if (minute >1440){
//                    callAttendance()
//                }

                    if (minute >1440){
                        callAttendance()
                    }else{
                        showToastMessage("You have given attendance already")
                    }

                }


            } catch ( e:Exception) {
                e.printStackTrace();
            }
            //  showToastMessage("attendance")
        }
        //return true
        return super.onOptionsItemSelected(item)
    }

    fun callAttendance(){
        //showHud()
        val apiInterface = ApiClient.getInstance().client.create(ApiInterface::class.java)
        val responseCall = apiInterface.doAttendance(
            AttendanceRequestParams(SharedPreferenceUtils.getLoggedInUserId(applicationContext))
        )


        responseCall.enqueue(AttendancResponse as Callback<AttendancResponse>)

    }
    private val AttendancResponse = object : NetworkCallBack<AttendancResponse>(){
        override fun onSuccessNetwork(data: Any?, response: NetworkResponse<AttendancResponse>) {
            response.data?.respMessage?.let { status ->
                val sdf = SimpleDateFormat("dd/M/yyyy")
                val currentDate = sdf.format(Date())
                SharedPreferenceUtils.saveData(applicationContext!!,"LAST_ATTENDANCE","${currentDate}")
                //hide()
                showToastMessage( response.data?.respMessage!!)


            }

        }

        override fun onFailureNetwork(data: Any?, error: NetworkError) {
           // hide()
        }

    }


    fun manageNavDrawerPrivilege(uRole:String){

        when (uRole) {
        //when (TEMP_CURRENT_LOGGED_IN) {

            SALES_LEAD_ROLE ->{

                navigationView.menu.removeItem(R.id.nav_scanQRCode)
            }

            SALES_PERSON_ROLE ->{

                navigationView.menu.removeItem(R.id.nav_scanQRCode)
            }


            DISTRIBUTER_ROLE ->{
                navigationView.menu.removeItem(R.id.nav_scanQRCode)

            }
            DEALER_ROLE ->{
                navigationView.menu.removeItem(R.id.nav_order)
                navigationView.menu.removeItem(R.id.nav_leave)
                navigationView.menu.removeItem(R.id.nav_expense)
                navigationView.menu.removeItem(R.id.nav_payment)
                navigationView.menu.removeItem(R.id.nav_myprofile)
                navigationView.menu.removeItem(R.id.nav_product)
                navigationView.menu.removeItem(R.id.nav_beat)
            }
            MECHANIC_ROLE ->{
                navigationView.menu.removeItem(R.id.nav_order)
                navigationView.menu.removeItem(R.id.nav_beat)
                navigationView.menu.removeItem(R.id.nav_expense)
                navigationView.menu.removeItem(R.id.nav_leave)
                navigationView.menu.removeItem(R.id.nav_payment)
                navigationView.menu.removeItem(R.id.nav_myprofile)

            }
            else -> { // Note the block
                print("x is neither 1 nor 2")
            }
        }
    }

}