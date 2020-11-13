package com.smilemakers.dashBoard.patientFragment.addPatient


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders

import com.smilemakers.R
import com.smilemakers.dashBoard.DashboardActivity
import com.smilemakers.dashBoard.patientFragment.PatientFragmentVM
import com.smilemakers.dashBoard.patientFragment.PatientViewModelFactory
import com.smilemakers.databinding.FragmentAddPatientBinding
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

/**
 * A simple [Fragment] subclass.
 */
class AddPatientFragment : Fragment() , KodeinAware {

    override val kodein by kodein()
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
    private val factory: PatientViewModelFactory by instance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_patient, container, false)
        val viewModel =
            ViewModelProviders.of(this, factory).get(PatientFragmentVM::class.java)
        binding?.vm = viewModel
        return binding?.root
    }


}
