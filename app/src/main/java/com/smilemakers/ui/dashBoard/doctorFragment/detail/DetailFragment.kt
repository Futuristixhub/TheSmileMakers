package com.smilemakers.ui.dashBoard.doctorFragment.detail

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
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
import com.smilemakers.databinding.FragmentDetailBinding
import com.smilemakers.ui.dashBoard.appointmentFragment.Area
import com.smilemakers.ui.dashBoard.doctorFragment.DoctorFragmentVM
import com.smilemakers.ui.dashBoard.doctorFragment.DoctorVieModelFactory
import com.smilemakers.ui.dashBoard.patientFragment.PatientListener
import com.smilemakers.utils.hide
import com.smilemakers.utils.show
import com.smilemakers.utils.showErrorSnackBar
import kotlinx.android.synthetic.main.fragment_detail.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class DetailFragment : Fragment(), KodeinAware,
    PatientListener {

    override val kodein by kodein()
    lateinit var spinner: Spinner
    lateinit var spinner_tratment: TextView
    lateinit var trtment_name: ArrayList<String>
    lateinit var trtment_namelst: ArrayList<String>
    lateinit var trtment_idlst: ArrayList<String>
    lateinit var selected: ArrayList<Boolean>

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
        viewModel?.authListener = this

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

            trtment_name = arrayListOf<String>()
            trtment_namelst = arrayListOf<String>()
            trtment_idlst = arrayListOf<String>()
            selected = arrayListOf<Boolean>()

            for (i in it.indices) {
                trtment_name.add(it[i].treatment_name)
                selected.add(false)
            }

            spinner_tratment.text = it[0].treatment_name

            spinner_tratment.setOnClickListener {
                val builder = AlertDialog.Builder(context!!)
                builder.setTitle("Select Treatment Types")
                builder.setMultiChoiceItems(
                    trtment_name.toTypedArray(),
                    selected.toBooleanArray(),
                    { dialog, which, isChecked ->

                        selected[which] = isChecked
                        // Get the clicked item
                        if (isChecked) {
                            trtment_namelst.add(trtment_name[which])
                            trtment_idlst.add((which + 1).toString())
                        } else if (trtment_namelst.contains(trtment_name[which])) {
                            trtment_namelst.remove(trtment_name[which])
                            trtment_idlst.remove((which + 1).toString())
                        }
                    })
                builder.setPositiveButton("OK") { dialog, which ->
                    spinner_tratment.text =
                        trtment_namelst.toString().replace("[", "").replace("]", "")
                    Log.d("tag", "djjdf..." + trtment_idlst)
                    viewModel?.trtmet_type = trtment_idlst.toString().replace("[", "").replace("]", "")
                    dialog.cancel()
                }
                builder.setNeutralButton("Cancel") { dialog, which ->
                    trtment_namelst = arrayListOf()
                    trtment_idlst = arrayListOf()
                    dialog.cancel()
                }
                val dialog = builder.create()
                dialog.show()
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
