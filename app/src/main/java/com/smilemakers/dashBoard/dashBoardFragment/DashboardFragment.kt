package com.smilemakers.dashBoard.dashBoardFragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

import com.smilemakers.R
import com.smilemakers.dashBoard.DashboardActivity
import com.smilemakers.databinding.FragmentDashboardBinding

/**
 * A simple [Fragment] subclass.
 */
class DashboardFragment : Fragment() {

    companion object {
        lateinit var mActivity : DashboardActivity
        var mFragment : DashboardFragment? = null

        fun newInstance(mActivity: DashboardActivity) : DashboardFragment? {
            this.mActivity = mActivity
            if (mFragment == null)
                mFragment = DashboardFragment()
            val bundle = Bundle()
            mFragment?.arguments = bundle
            return mFragment
        }
    }

    var binding : FragmentDashboardBinding? = null
    val dashboardVM = DashboardFragmentVM(this, mActivity)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false)
        binding?.vm = dashboardVM
        return binding?.root
    }


}
