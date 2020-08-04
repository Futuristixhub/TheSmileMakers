package com.smilemakers.dashBoard.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.smilemakers.R
import com.smilemakers.dashBoard.DashboardActivity
import com.smilemakers.databinding.FragmentProfileBinding

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {

    companion object{
        lateinit var mActivity: DashboardActivity
        var mFragment: ProfileFragment? = null
        fun newInstance(mActivity: DashboardActivity): ProfileFragment? {
            this.mActivity = mActivity
            if (mFragment == null)
                mFragment = ProfileFragment()
            val bundle = Bundle()
            mFragment?.arguments = bundle
            return mFragment
        }
    }

    var binding: FragmentProfileBinding? = null
    val profileVM = ProfileVM(this, mActivity)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        binding?.vm = profileVM
        return binding?.root
    }

}
