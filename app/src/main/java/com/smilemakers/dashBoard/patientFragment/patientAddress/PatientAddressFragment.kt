package com.smilemakers.dashBoard.patientFragment.patientAddress


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity

import com.smilemakers.R
import com.smilemakers.dashBoard.DashboardActivity
import com.smilemakers.databinding.FragmentPatientAddressBinding

/**
 * A simple [Fragment] subclass.
 */
class PatientAddressFragment : Fragment() {

    companion object{
        var fragment: PatientAddressFragment? = null
        var mActivity: DashboardActivity? = null

        fun newInstance(mActivity: DashboardActivity) : PatientAddressFragment? {
            this.mActivity = mActivity
            if (fragment == null)
                fragment = PatientAddressFragment()
            val bundle = Bundle()
            fragment?.arguments = bundle
            return fragment
        }
    }

    var binding: FragmentPatientAddressBinding? = null
    var addressVM = PatientAddressFragmentVM(this, mActivity!!)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_patient_address,container, false)
        binding?.vm = addressVM
        return binding?.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1223) {
            if (resultCode == Activity.RESULT_OK) {
                val place = Autocomplete.getPlaceFromIntent(data!!)
                Log.i("DashBoard TAG", "Place: " + place.name + ", " + place.id)
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) { // TODO: Handle the error.
                val status: Status = Autocomplete.getStatusFromIntent(data!!)
                Log.i("DashBoard TAG", status.getStatusMessage())
            } else if (resultCode == Activity.RESULT_CANCELED) { // The user canceled the operation.
            }
        }
    }
}
