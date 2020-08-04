package com.smilemakers.dashBoard.patientFragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

import com.smilemakers.R
import com.smilemakers.dashBoard.DashboardActivity
import com.smilemakers.databinding.FragmentPaitentBinding

/**
 * A simple [Fragment] subclass.
 */
class PatientFragment : Fragment() {

    companion object {
        var fragment: PatientFragment? = null
        lateinit var mActivity: DashboardActivity

        fun newInstance(mActivity: DashboardActivity) : PatientFragment? {
            this.mActivity = mActivity
            if (fragment == null)
                fragment = PatientFragment()
            val bundle = Bundle()
            fragment?.arguments = bundle
            return fragment
        }
    }

    var binding: FragmentPaitentBinding? = null
    val patientVM = PatientFragmentVM(this, mActivity)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_paitent, container, false)
        binding?.vm = patientVM
        patientVM.createList(binding?.recPatientList!!)
        return binding?.root
    }
}