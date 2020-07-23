package com.velectico.rbm.reminder.view

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.velectico.rbm.CreateReminderViewModel
import com.velectico.rbm.R

class CreateReminder : Fragment() {

    companion object {
        fun newInstance() = CreateReminder()
    }

    private lateinit var viewModel: CreateReminderViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.create_reminder_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CreateReminderViewModel::class.java)
        // TODO: Use the ViewModel
    }

}