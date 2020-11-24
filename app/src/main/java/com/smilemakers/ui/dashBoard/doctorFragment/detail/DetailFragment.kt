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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.simplemobiletools.commons.extensions.toast
import com.smilemakers.R
import com.smilemakers.ui.dashBoard.doctorFragment.DoctorFragmentVM
import com.smilemakers.ui.dashBoard.doctorFragment.DoctorVieModelFactory
import com.smilemakers.databinding.FragmentDetailBinding
import com.smilemakers.ui.dashBoard.appointmentFragment.Area
import com.smilemakers.ui.dashBoard.appointmentFragment.Treatments
import com.smilemakers.ui.dashBoard.appointmentFragment.addAppointment.TreatmentAdpater
import com.smilemakers.ui.dashBoard.patientFragment.PatientListener
import com.smilemakers.utils.hide
import com.smilemakers.utils.show
import com.smilemakers.utils.showErrorSnackBar
import kotlinx.android.synthetic.main.fragment_appointment_form.*
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.fragment_detail.progress_bar
import kotlinx.android.synthetic.main.fragment_detail.root_layout
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class DetailFragment : Fragment(), KodeinAware ,
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

        spinner_tratment = binding?.root!!.findViewById(R.id.sp_type_treatmnt)

        binding?.progressBar?.show()
        viewModel?.getData()
        viewModel?.tdata?.observe(viewLifecycleOwner, Observer {
            binding?.progressBar?.hide()

            val adapter = TreatmentAdpater(context!!, it)
            spinner_tratment.adapter = adapter

            // if (!mEvent.treatment_type.isEmpty()) {
            //   val spinnerPosition = adapter.getPosition(mEvent.treatment_type)
            //    sp_treatment.setSelection(spinnerPosition)
            //  }
            spinner_tratment.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val data: Treatments = parent!!.selectedItem as Treatments
                    viewModel?.trtmet_type = data.treatment_name
                }

            }

        })
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

}
