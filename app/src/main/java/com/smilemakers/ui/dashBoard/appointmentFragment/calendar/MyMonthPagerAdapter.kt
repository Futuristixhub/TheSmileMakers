package com.smilemakers.ui.dashBoard.appointmentFragment.calendar

import android.os.Bundle
import android.util.SparseArray
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.smilemakers.utils.DAY_CODE

class MyMonthPagerAdapter(fm: FragmentManager, private val mCodes: List<String>, private val mListener: NavigationListener) : FragmentStatePagerAdapter(fm) {
    private val mFragments = SparseArray<MonthFragment>()

    override fun getCount() = mCodes.size

    override fun getItem(position: Int): Fragment {
        val bundle = Bundle()
        val code = mCodes[position]
        bundle.putString(DAY_CODE, code)

        val fragment =
            MonthFragment()
        fragment.arguments = bundle
        fragment.listener = mListener

     mFragments.put(position, fragment)
        return fragment
    }

}
