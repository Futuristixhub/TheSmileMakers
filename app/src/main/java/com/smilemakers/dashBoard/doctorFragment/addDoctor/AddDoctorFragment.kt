package com.smilemakers.dashBoard.doctorFragment.addDoctor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.smilemakers.R
import com.smilemakers.dashBoard.DashboardActivity
import com.smilemakers.dashBoard.patientFragment.addPatient.AddPatientFragment
import com.smilemakers.dashBoard.patientFragment.addPatient.AddPatientFragmentVM
import com.smilemakers.databinding.FragmentAddDoctorBinding
import com.smilemakers.databinding.FragmentAddPatientBinding
import com.smilemakers.databinding.FragmentDoctorBinding

class AddDoctorFragment : Fragment() {

    companion object {
      //  lateinit var mActivity: DashboardActivity
        var fragment: AddDoctorFragment? = null

        fun newInstance() : AddDoctorFragment? {
         //   this.mActivity = mActivity
            if (fragment == null)
                fragment = AddDoctorFragment()
            val bundle = Bundle()
            fragment?.arguments = bundle
            return fragment
        }
    }

    var binding: FragmentAddDoctorBinding? = null
    val addPatientFragmentVM = AddDoctorFragmentVM()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_doctor, container, false)
        binding?.vm = addPatientFragmentVM
        return binding?.root
    }


}
