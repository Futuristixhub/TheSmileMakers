package com.smilemakers.ui.dashBoard.doctorFragment.detail

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
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.simplemobiletools.commons.extensions.toast
import com.smilemakers.R
import com.smilemakers.ui.dashBoard.doctorFragment.DoctorFragmentVM
import com.smilemakers.ui.dashBoard.doctorFragment.DoctorVieModelFactory
import com.smilemakers.databinding.FragmentDetailBinding
import com.smilemakers.ui.dashBoard.patientFragment.PatientListener
import com.smilemakers.utils.hide
import com.smilemakers.utils.show
import com.smilemakers.utils.showErrorSnackBar
import kotlinx.android.synthetic.main.fragment_detail.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class DetailFragment : Fragment(), AdapterView.OnItemSelectedListener , KodeinAware ,
    PatientListener{

    override val kodein by kodein()
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
    private val factory: DoctorVieModelFactory by instance()
    var viewModel: DoctorFragmentVM? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        viewModel =
            ViewModelProviders.of(this, factory).get(DoctorFragmentVM::class.java)
        binding?.vm = viewModel
        viewModel?.authListener= this

        viewModel?.fname?.value = arguments!!.getString("fname")
        viewModel?.lname?.value = arguments!!.getString("lname")
        viewModel?.gender = arguments!!.getString("gender")
        viewModel?.dob?.set(arguments!!.getString("dob"))
        viewModel?.age?.value = arguments!!.getString("age")
        viewModel?.email?.value = arguments!!.getString("email")
        viewModel?.education?.value = arguments!!.getString("education")
        viewModel?.mNumber?.value = arguments!!.getString("mno")
        viewModel?.maltNumber?.value = arguments!!.getString("altmno")
        viewModel?.image = arguments!!.getString("image")

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
        spinner_tratment.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
               viewModel?.trtmet_type = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

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
        if (text.equals("Nikol")) {
            viewModel?.location = "4"
        } else {
            viewModel?.location = "5"
        }
    }

    override fun onStarted() {
        progress_bar.show()
    }

    override fun onSuccess(message: String) {
        progress_bar.hide()

        context!!.showErrorSnackBar(root_layout, message)
        context!!.toast(message)
        (context as AppCompatActivity).finish()
    }

    override fun onFailure(message: String) {
        progress_bar.hide()
        context!!.showErrorSnackBar(root_layout, message)
    }

}
