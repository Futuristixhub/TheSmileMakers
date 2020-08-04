package com.smilemakers.dashBoard.appointmentFragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

import com.smilemakers.R
import com.smilemakers.dashBoard.DashboardActivity
import com.smilemakers.databinding.FragmentAppointmentBinding

/**
 * A simple [Fragment] subclass.
 */
class AppointmentFragment : Fragment() {

    companion object{
        lateinit var mActivity: DashboardActivity
        var mFragment: AppointmentFragment? = null

        fun newInstance(mActivity: DashboardActivity): AppointmentFragment? {
            this.mActivity = mActivity
            if (mFragment == null)
                mFragment = AppointmentFragment()
           val bundle = Bundle()
            mFragment?.arguments = bundle
            return mFragment
        }
    }

    var binding:FragmentAppointmentBinding? = null
    val appointmentVM = AppointmentFragmentVM(this, mActivity)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_appointment, container, false)
        binding?.vm = appointmentVM
        return binding?.root
    }
}
