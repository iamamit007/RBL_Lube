package com.velectico.rbm.loginreg.view

import android.content.Intent
import android.os.Bundle
import androidx.databinding.ViewDataBinding
import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseActivity
import com.velectico.rbm.databinding.ActivityForgotPasswordBinding
import com.velectico.rbm.databinding.ActivityResetPasswordBinding

class ResetPassword : BaseActivity() {
    private lateinit var binding: ActivityResetPasswordBinding

    override fun getLayout(): Int = R.layout.activity_reset_password

    override fun init(savedInstanceState: Bundle?, binding: ViewDataBinding) {
        this.binding = binding as ActivityResetPasswordBinding
        binding.btnReset.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }
}