package com.velectico.rbm.beats.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.velectico.rbm.beats.model.BeatAssignments
import com.velectico.rbm.beats.model.Beats

//https://github.com/charlesng/SampleAppArch
class BeatSharedViewModel : ViewModel() {
    var assignmentList = MutableLiveData<MutableList<BeatAssignments>>()
    var beats = MutableLiveData<Beats>()

    fun setAssignments(item: MutableList<BeatAssignments>) {
        assignmentList.value = item
    }

    fun setBeat(item: Beats){
        beats.value = item
    }
}