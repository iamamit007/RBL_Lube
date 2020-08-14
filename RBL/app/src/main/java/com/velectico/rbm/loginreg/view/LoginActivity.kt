package com.velectico.rbm.loginreg.view;

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseActivity
import com.velectico.rbm.databinding.ActivityLoginBinding
import com.velectico.rbm.loginreg.model.LoginResponse
import com.velectico.rbm.loginreg.viewmodel.LoginViewModel
import com.velectico.rbm.navdrawer.views.DashboardActivity
import com.velectico.rbm.utils.EXTRA_LOGIN_RESPONSE
import com.velectico.rbm.utils.SHARED_PREFERENCE_FILE
import com.velectico.rbm.utils.SharedPreferenceUtils

//https://github.com/kotlindroider/Kotlin-Login-Sample
//http://ticons.fokkezb.nl/

class LoginActivity : BaseActivity() {
    private lateinit var binding:ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun getLayout(): Int = R.layout.activity_login

    override fun init(savedInstanceState: Bundle?, binding: ViewDataBinding) {
        this.binding = binding as ActivityLoginBinding
        setUp()
    }

    private fun setUp() {
        val animationSlideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up)
        val animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)


        binding.btnLogin.setOnClickListener { doLogin() }
//        binding.linkForgotPwd.setOnClickListener {
//            val intent = Intent(this, DashboardActivity::class.java)
//            startActivity(intent)
//        }
        observeViewModelData()
        binding.linkForgotPwd.setOnClickListener {
            val intent = Intent(this, ForgotPassword::class.java)
            startActivity(intent)
        }
    }

    private fun observeViewModelData() {
        loginViewModel = LoginViewModel.getInstance(this)
        loginViewModel.userDataResponse.observe(this, Observer { listResponse ->
            listResponse?.let {
                showToastMessage(getString(R.string.login_success))
                onLoginSuccess(it)
            }
        })

        loginViewModel.loading.observe(this, Observer { progress ->
            binding.progressLayout.visibility = if(progress) View.VISIBLE else View.GONE
        })

        loginViewModel.errorLiveData.observe(this, Observer {
            onLoginFailed()
            (this as BaseActivity).showAlertDialog(it.errorMessage ?: getString(R.string.no_data_available))
        })
    }

    private fun doLogin() {
        binding.btnLogin.isEnabled = false
        if (!validate()) {
            onLoginFailed()
            return
        }
        val mobile = binding.inputEmail.text.toString()
        val password = binding.inputPassword.text.toString()
        loginViewModel.loginAPICall(mobile, password)
    }

    override fun onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true)
    }

    private fun onLoginSuccess(response:LoginResponse?) {

        binding.btnLogin.isEnabled = true
        binding.inputEmail.setText("");
        binding.inputPassword.setText("")
        response?.userDetails?.get(0)?.uMLoginId?.let { SharedPreferenceUtils.addSessionDataToSharedPreference(it,applicationContext) };
        val intent = Intent(this, DashboardActivity::class.java)
        intent.putExtra(EXTRA_LOGIN_RESPONSE, response)
        startActivity(intent)

    }

    fun onLoginFailed() {
        binding.btnLogin.isEnabled = true
    }

    fun validate(): Boolean {
        var errMsg : String = "";
        var valid = true

        val email = binding.inputEmail!!.text.toString()
        val password = binding.inputPassword!!.text.toString()

        if (email.isEmpty() || !android.util.Patterns.PHONE.matcher(email).matches()) {
            errMsg = "Enter a valid phone no\n"
            valid = false
        }

        if (password.isEmpty() || password.length < 4 || password.length > 10) {
            errMsg += "Enter a password between 4 and 10 alphanumeric characters";
            valid = false
        }
        if(!valid){
            Toast.makeText(applicationContext,errMsg,Toast.LENGTH_LONG).show()
        }
        return valid
    }
}
