package com.smilemakers.dashBoard.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.smilemakers.R
import com.smilemakers.dashBoard.DashboardActivity
import com.smilemakers.databinding.FragmentProfileBinding
import com.smilemakers.login.LoginVM
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment(), KodeinAware {

    override val kodein by kodein()

    private lateinit var viewModel: ProfileVM
    private val factory: ProfileViewModelFactory by instance()

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        val  viewModel =
            ViewModelProviders.of(this, factory).get(ProfileVM::class.java)
        binding?.vm = viewModel
        return binding?.root
    }

}
