package com.smilemakers.ui.dashBoard.patientFragment.patientAddress


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
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
import com.smilemakers.ui.dashBoard.appointmentFragment.Area
import com.smilemakers.ui.dashBoard.doctorFragment.detail.AreaAdapter
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
class PatientAddressFragment : Fragment(), KodeinAware, PatientListener {

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
        binding?.progressBar?.show()
        viewModel?.getData()
        viewModel?.adata?.observe(viewLifecycleOwner, Observer {
            binding?.progressBar?.hide()

            val adapter = AreaAdapter(context!!, it)
            spinner.adapter = adapter

            // if (!mEvent.treatment_type.isEmpty()) {
            //   val spinnerPosition = adapter.getPosition(mEvent.treatment_type)
            //    sp_treatment.setSelection(spinnerPosition)
            //  }
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val data: Area = parent!!.selectedItem as Area
                    viewModel?.location = data.area_id
                }

            }

        })

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
            } else if (resultCode == Activity.RESULT_CANCELED) { // The user canceled the operation.
            }
        }
    }

    override fun onStarted() {
        progress_bar.show()
    }

    override fun onSuccess(message: String, value: String) {
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
