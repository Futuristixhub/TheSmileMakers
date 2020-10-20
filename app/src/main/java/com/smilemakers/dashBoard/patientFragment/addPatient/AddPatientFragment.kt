package com.smilemakers.dashBoard.patientFragment.addPatient


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

import com.smilemakers.R
import com.smilemakers.dashBoard.DashboardActivity
import com.smilemakers.databinding.FragmentAddPatientBinding

/**
 * A simple [Fragment] subclass.
 */
class AddPatientFragment : Fragment() {

    companion object {
     //   lateinit var mActivity: DashboardActivity
        var fragment: AddPatientFragment? = null

        fun newInstance() : AddPatientFragment? {
          //  this.mActivity = mActivity
            if (fragment == null)
                fragment = AddPatientFragment()
            val bundle = Bundle()
            fragment?.arguments = bundle
            return fragment
        }
    }

    var binding: FragmentAddPatientBinding? = null
    val addPatientFragmentVM = AddPatientFragmentVM()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_patient, container, false)
        binding?.vm = addPatientFragmentVM
        return binding?.root
    }


}
