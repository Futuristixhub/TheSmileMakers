package com.smilemakers.dashBoard.patientFragment.patientAddress


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity

import com.smilemakers.R
import com.smilemakers.dashBoard.DashboardActivity
import com.smilemakers.dashBoard.patientFragment.PatientFragmentVM
import com.smilemakers.dashBoard.patientFragment.PatientViewModelFactory
import com.smilemakers.databinding.FragmentPatientAddressBinding
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

/**
 * A simple [Fragment] subclass.
 */
class PatientAddressFragment : Fragment() , KodeinAware {

    override val kodein by kodein()
    lateinit var spinner: Spinner
    companion object{
        var fragment: PatientAddressFragment? = null
     //   var mActivity: DashboardActivity? = null

        fun newInstance() : PatientAddressFragment? {
         //   this.mActivity = mActivity
            if (fragment == null)
                fragment = PatientAddressFragment()
            val bundle = Bundle()
            fragment?.arguments = bundle
            return fragment
        }
    }

    var binding: FragmentPatientAddressBinding? = null
    private val factory: PatientViewModelFactory by instance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_patient_address,container, false)
        val viewModel =
            ViewModelProviders.of(this, factory).get(PatientFragmentVM::class.java)
        binding?.vm = viewModel

        return binding?.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1223) {
            if (resultCode == Activity.RESULT_OK) {
                val place = Autocomplete.getPlaceFromIntent(data!!)
                Log.i("DashBoard TAG", "Place: " + place.name + ", " + place.id)
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                val status: Status = Autocomplete.getStatusFromIntent(data!!)
                Log.i("DashBoard TAG", status.getStatusMessage())
            } else if (resultCode == Activity.RESULT_CANCELED) { // The user canceled the operation.
            }
        }
    }

}
