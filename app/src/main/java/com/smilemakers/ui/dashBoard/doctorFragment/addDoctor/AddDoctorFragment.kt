package com.smilemakers.ui.dashBoard.doctorFragment.addDoctor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.smilemakers.R
import com.smilemakers.ui.dashBoard.doctorFragment.DoctorFragmentVM
import com.smilemakers.ui.dashBoard.doctorFragment.DoctorVieModelFactory
import com.smilemakers.databinding.FragmentAddDoctorBinding
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class AddDoctorFragment : Fragment() , KodeinAware {

    override val kodein by kodein()
    companion object {
      //  lateinit var mActivity: DashboardActivity
        var fragment: AddDoctorFragment? = null

        fun newInstance() : AddDoctorFragment? {
         //   this.mActivity = mActivity
            if (fragment == null)
                fragment = AddDoctorFragment()
            val bundle = Bundle()
            fragment?.arguments = bundle
            return fragment
        }
    }

    var binding: FragmentAddDoctorBinding? = null
    private val factory: DoctorVieModelFactory by instance()
    var viewModel: DoctorFragmentVM? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_doctor, container, false)
        viewModel =
            ViewModelProviders.of(this, factory).get(DoctorFragmentVM::class.java)
        binding?.vm = viewModel
        return binding?.root
    }


}
