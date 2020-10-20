package com.smilemakers.dashBoard.doctorFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.smilemakers.R
import com.smilemakers.dashBoard.DashboardActivity
import com.smilemakers.dashBoard.profile.ProfileVM
import com.smilemakers.dashBoard.profile.ProfileViewModelFactory
import com.smilemakers.databinding.FragmentDashboardBinding
import com.smilemakers.databinding.FragmentDoctorBinding
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class DoctorFragment : Fragment() , KodeinAware {

    override val kodein by kodein()

    private lateinit var viewModel: DoctorFragmentVM
    private val factory: DoctorVieModelFactory by instance()

    companion object {
        var fragment: DoctorFragment? = null
        lateinit var mActivity: DashboardActivity

        fun newInstance(mActivity: DashboardActivity): DoctorFragment? {
            this.mActivity = mActivity
            if (fragment == null)
                fragment = DoctorFragment()
            val bundle = Bundle()
            fragment?.arguments = bundle
            return fragment
        }
    }

    var binding: FragmentDoctorBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_doctor, container, false)
        val  viewModel =
            ViewModelProviders.of(this, factory).get(DoctorFragmentVM::class.java)
        binding?.vm = viewModel
        viewModel.createList(binding?.recPatientList!!)
        return binding?.root
    }
}