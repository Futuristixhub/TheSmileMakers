package com.smilemakers.dashBoard.doctorFragment.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.smilemakers.R
import com.smilemakers.dashBoard.DashboardActivity
import com.smilemakers.databinding.FragmentAddDoctorBinding
import com.smilemakers.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {

    companion object {
        lateinit var mActivity: DashboardActivity
        var fragment: DetailFragment? = null

        fun newInstance(mActivity: DashboardActivity) : DetailFragment? {
            this.mActivity = mActivity
            if (fragment == null)
                fragment = DetailFragment()
            val bundle = Bundle()
            fragment?.arguments = bundle
            return fragment
        }
    }

    var binding: FragmentDetailBinding? = null
    val addPatientFragmentVM = DetailFragmentVM(this, mActivity)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        binding?.vm = addPatientFragmentVM
        return binding?.root
    }


}
