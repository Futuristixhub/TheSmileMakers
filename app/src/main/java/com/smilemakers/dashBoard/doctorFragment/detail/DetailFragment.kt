package com.smilemakers.dashBoard.doctorFragment.detail

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.smilemakers.R
import com.smilemakers.dashBoard.DashboardActivity
import com.smilemakers.databinding.FragmentAddDoctorBinding
import com.smilemakers.databinding.FragmentDetailBinding

class DetailFragment : Fragment(), AdapterView.OnItemSelectedListener {
    lateinit var spinner: Spinner
    lateinit var spinner_tratment: Spinner

    companion object {
        // lateinit var mActivity: DashboardActivity
        var fragment: DetailFragment? = null

        fun newInstance(): DetailFragment? {
            //this.mActivity = mActivity
            if (fragment == null)
                fragment = DetailFragment()
            val bundle = Bundle()
            fragment?.arguments = bundle
            return fragment
        }
    }

    var binding: FragmentDetailBinding? = null
    val addPatientFragmentVM = DetailFragmentVM()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        binding?.vm = addPatientFragmentVM

        spinner = binding?.root!!.findViewById(R.id.sp_regis_area)
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.registration_area,
            R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = this

        spinner_tratment = binding?.root!!.findViewById(R.id.sp_type_treatmnt)
        val adapter_treatment = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.treatment_type,
            R.layout.simple_spinner_item
        )
        adapter_treatment.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_tratment.adapter = adapter_treatment
        spinner_tratment.onItemSelectedListener = this

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

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val text: String = parent?.getItemAtPosition(position).toString()
        //textView.text = text
    }

}
