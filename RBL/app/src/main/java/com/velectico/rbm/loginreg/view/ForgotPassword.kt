package com.velectico.rbm.loginreg.view

import android.content.Intent
import android.os.Bundle
import androidx.databinding.ViewDataBinding
import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseActivity
import com.velectico.rbm.databinding.ActivityForgotPasswordBinding
import com.velectico.rbm.databinding.ActivityLoginBinding
import com.velectico.rbm.loginreg.viewmodel.LoginViewModel

class ForgotPassword: BaseActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding

    override fun getLayout(): Int = R.layout.activity_forgot_password

    override fun init(savedInstanceState: Bundle?, binding: ViewDataBinding) {
        this.binding = binding as ActivityForgotPasswordBinding
        binding.btnSend.setOnClickListener {
            val intent = Intent(this, ResetPassword::class.java)
            startActivity(intent)
        }

    }
}