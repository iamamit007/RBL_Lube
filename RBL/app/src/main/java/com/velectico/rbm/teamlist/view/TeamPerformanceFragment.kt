package com.velectico.rbm.teamlist.view

import android.graphics.Color
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.github.anastr.speedviewlib.components.Section
import com.github.anastr.speedviewlib.components.Style
import com.github.anastr.speedviewlib.util.doOnSections
import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseFragment
import com.velectico.rbm.databinding.FragmentTeamPerformanceBinding
import kotlinx.android.synthetic.main.fragment_team_performance.*

/**
 * A simple [Fragment] subclass.
 */
class TeamPerformanceFragment : BaseFragment()  {

    private lateinit var binding: FragmentTeamPerformanceBinding

    override fun getLayout(): Int {
        return R.layout.fragment_team_performance
    }

    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentTeamPerformanceBinding
        binding.speedView.setMinMaxSpeed(0.0F, 5.0F)
        binding.speedView.clearSections()
        binding.speedView.addSections(Section(0f, .4f, Color.rgb(255,0,0),binding.speedView.dpTOpx(80f))
            , Section(.4f, .8f, Color.rgb(255,194,0),binding.speedView.dpTOpx(80f))
            , Section(.8f, 1f, Color.rgb(0,128,0),binding.speedView.dpTOpx(80f))
           )
        binding.speedView.speedTo(2F)


    }

    fun moveToDetails(){
       // val navDirection =  TeamPerformanceFragmentDirections.actionTeamPerformanceFragmentToTeamPerformanceDetailsFragment()
       // Navigation.findNavController(binding.lblMonthly).navigate(navDirection)
    }

}
