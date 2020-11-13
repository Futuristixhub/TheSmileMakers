package com.smilemakers.dashBoard.patientFragment


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
import androidx.lifecycle.ViewModelProviders
import com.smilemakers.R
import com.smilemakers.dashBoard.DashboardActivity
import com.smilemakers.databinding.FragmentPaitentBinding
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


/**
 * A simple [Fragment] subclass.
 */
class PatientFragment : Fragment(), KodeinAware {

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
        val viewModel =
            ViewModelProviders.of(this, factory).get(PatientFragmentVM::class.java)
        binding?.vm = viewModel
        viewModel.createList(binding?.recPatientList!!)

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
}