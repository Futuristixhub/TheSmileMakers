package com.smilemakers.ui.dashBoard.doctorFragment

import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.smilemakers.R
import com.smilemakers.ui.dashBoard.DashboardActivity
import com.smilemakers.databinding.FragmentDoctorBinding
import com.smilemakers.ui.dashBoard.patientFragment.PatientListener
import com.smilemakers.utils.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_doctor.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class DoctorFragment : Fragment(), KodeinAware ,PatientListener{

    override val kodein by kodein()

    private lateinit var viewModel: DoctorFragmentVM
    private val factory: DoctorVieModelFactory by instance()

    companion object {
        var fragment: DoctorFragment? = null
        lateinit var mActivity: DashboardActivity

        fun newInstance(): DoctorFragment? {
            if (fragment == null)
                fragment = DoctorFragment()
            val bundle = Bundle()
            fragment?.arguments = bundle
            return fragment
        }
    }

    var binding: FragmentDoctorBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_doctor, container, false)
         viewModel =
            ViewModelProviders.of(this, factory).get(DoctorFragmentVM::class.java)
        binding?.vm = viewModel
        viewModel?.authListener = this

        val bar: ActionBar? = (activity as AppCompatActivity?)!!.supportActionBar
        if (bar != null) {
            val tv = TextView(context)
            val lp: ActionBar.LayoutParams = ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT,  // Width of TextView
                ActionBar.LayoutParams.WRAP_CONTENT
            ) // Height of TextView
            tv.layoutParams = lp
            tv.setText(getString(R.string.doctor))
            tv.setTextColor(Color.WHITE)

            val typedValue = TypedValue()
            resources.getValue(R.dimen.actionBar_text, typedValue, true)
            val myFloatValue = typedValue.float

            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, myFloatValue)
            bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM)
            bar.setCustomView(tv)
        }
        return binding?.root
    }

    override fun onResume() {
        super.onResume()
        bindUI()
    }

    private fun bindUI() = Coroutines.main {
        binding?.progressBar?.show()
        viewModel.getDoctors()
        viewModel.doctors.observe(viewLifecycleOwner, Observer {
            binding?.progressBar?.hide()
            initRecyclerView(it.toDoctorItem())
        })
    }

    private fun initRecyclerView(DoctorItem: List<DoctorItem>) {

        val mAdapter = GroupAdapter<ViewHolder>().apply {
            addAll(DoctorItem)
        }

        binding?.recDoctorList?.apply {
            setHasFixedSize(true)
            adapter = mAdapter
        }

    }


    private fun List<Doctor>.toDoctorItem() : List<DoctorItem>{
        return this.map {
            DoctorItem(it,context!!)
        }
    }

    override fun onStarted() {
        TODO("Not yet implemented")
    }

    override fun onSuccess(message: String) {
        TODO("Not yet implemented")
    }

    override fun onFailure(message: String) {
        binding?.progressBar?.hide()
        context!!.showErrorSnackBar(root_layout, message)
    }

}