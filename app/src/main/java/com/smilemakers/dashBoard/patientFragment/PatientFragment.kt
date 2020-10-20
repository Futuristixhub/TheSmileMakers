package com.smilemakers.dashBoard.patientFragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders

import com.smilemakers.R
import com.smilemakers.dashBoard.DashboardActivity
import com.smilemakers.dashBoard.profile.ProfileVM
import com.smilemakers.dashBoard.profile.ProfileViewModelFactory
import com.smilemakers.databinding.FragmentPaitentBinding
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

/**
 * A simple [Fragment] subclass.
 */
class PatientFragment : Fragment(), KodeinAware {

    override val kodein by kodein()

    private lateinit var viewModel: PatientFragmentVM
    private val factory: PatientViewModelFactory by instance()

    companion object {
        var fragment: PatientFragment? = null
        lateinit var mActivity: DashboardActivity

        fun newInstance(mActivity: DashboardActivity): PatientFragment? {
            this.mActivity = mActivity
            if (fragment == null)
                fragment = PatientFragment()
            val bundle = Bundle()
            fragment?.arguments = bundle
            return fragment
        }
    }

    var binding: FragmentPaitentBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_paitent, container, false)
        val viewModel =
            ViewModelProviders.of(this, factory).get(PatientFragmentVM::class.java)
        binding?.vm = viewModel
        viewModel.createList(binding?.recPatientList!!)
        return binding?.root
    }
}