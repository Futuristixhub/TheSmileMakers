package com.smilemakers.ui.dashBoard.patientFragment


import android.graphics.Color
import android.os.Bundle
import android.util.Log
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
import com.smilemakers.databinding.FragmentPaitentBinding
import com.smilemakers.utils.Coroutines
import com.smilemakers.utils.hide
import com.smilemakers.utils.show
import com.smilemakers.utils.showErrorSnackBar
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_paitent.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


/**
 * A simple [Fragment] subclass.
 */
class PatientFragment : Fragment(), KodeinAware ,PatientListener{

    override val kodein by kodein()

    private lateinit var viewModel: PatientFragmentVM
    private val factory: PatientViewModelFactory by instance()

    companion object {
        var fragment: PatientFragment? = null
        lateinit var mActivity: DashboardActivity

        fun newInstance(): PatientFragment? {
            if (fragment == null)
                fragment = PatientFragment()
            val bundle = Bundle()
            fragment?.arguments = bundle
            return fragment
        }
    }

    var binding: FragmentPaitentBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_paitent, container, false)
         viewModel =
            ViewModelProviders.of(this, factory).get(PatientFragmentVM::class.java)
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
            tv.setText(getString(R.string.patient))
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

    override fun onPause() {
        super.onPause()
        viewModel.patients.removeObservers(viewLifecycleOwner)
    }

    private fun bindUI() = Coroutines.main {
        binding?.progressBar?.show()
        viewModel.getPatients()
        viewModel.patients.observe(viewLifecycleOwner, Observer {
            binding?.progressBar?.hide()
            Log.d("tag","this....."+it.toPatientItem().size)
            initRecyclerView(it.toPatientItem())
        })
    }

    private fun initRecyclerView(PatientItem: List<PatientItem>) {

        val mAdapter = GroupAdapter<ViewHolder>().apply {
            addAll(PatientItem)
        }

        binding?.recPatientList?.apply {
            setHasFixedSize(true)
            adapter = mAdapter
        }

    }


    private fun List<Patient>.toPatientItem() : List<PatientItem>{
        return this.map {
            PatientItem(it,context!!)
        }
    }

    override fun onStarted() {
    }

    override fun onSuccess(message: String, value: String) {
    }

    override fun onFailure(message: String) {
        binding?.progressBar?.hide()
        context!!.showErrorSnackBar(root_layout, message)
    }
}