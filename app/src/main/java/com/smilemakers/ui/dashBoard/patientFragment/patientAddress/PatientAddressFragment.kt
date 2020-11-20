package com.smilemakers.ui.dashBoard.patientFragment.patientAddress


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
import com.smilemakers.ui.dashBoard.patientFragment.PatientFragmentVM
import com.smilemakers.ui.dashBoard.patientFragment.PatientListener
import com.smilemakers.ui.dashBoard.patientFragment.PatientViewModelFactory
import com.smilemakers.databinding.FragmentPatientAddressBinding
import com.smilemakers.utils.hide
import com.smilemakers.utils.show
import com.smilemakers.utils.showErrorSnackBar
import kotlinx.android.synthetic.main.fragment_patient_address.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

/**
 * A simple [Fragment] subclass.
 */
class PatientAddressFragment : Fragment(), KodeinAware, PatientListener,
    AdapterView.OnItemSelectedListener {

    override val kodein by kodein()
    lateinit var spinner: Spinner
    var viewModel: PatientFragmentVM? = null

    companion object {
        var fragment: PatientAddressFragment? = null
        //   var mActivity: DashboardActivity? = null

        fun newInstance(): PatientAddressFragment? {
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
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_patient_address,
            container,
            false
        )
        viewModel =
            ViewModelProviders.of(this, factory).get(PatientFragmentVM::class.java)
        binding?.vm = viewModel
        viewModel?.authListener = this

        viewModel?.fname?.value = arguments!!.getString("fname")
        viewModel?.lname?.value = arguments!!.getString("lname")
        viewModel?.gender = arguments!!.getString("gender")
        viewModel?.dob?.set(arguments!!.getString("dob"))
        viewModel?.age?.value = arguments!!.getString("age")
        viewModel?.refId?.value = arguments!!.getString("refid")
        viewModel?.refName?.value = arguments!!.getString("refname")
        viewModel?.mNumber?.value = arguments!!.getString("mno")
        viewModel?.altmNumber?.value = arguments!!.getString("altmno")
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

    override fun onItemSelected(parent: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        val text: String = parent?.getItemAtPosition(position).toString()
        if (text.equals("Nikol")) {
            viewModel?.location = "4"
        } else {
            viewModel?.location = "5"
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

}
