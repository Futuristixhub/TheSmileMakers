package com.smilemakers.dashBoard.doctorFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.smilemakers.R
import com.smilemakers.dashBoard.DashboardActivity
import com.smilemakers.databinding.FragmentDashboardBinding
import com.smilemakers.databinding.FragmentDoctorBinding

class DoctorFragment : Fragment() {

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
    val patientVM = DoctorFragmentVM(this, mActivity)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_doctor, container, false)
        binding?.vm = patientVM
        patientVM.createList(binding?.recPatientList!!)
        return binding?.root
    }
}