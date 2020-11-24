package com.smilemakers.ui.dashBoard.dashBoardFragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders

import com.smilemakers.R
import com.smilemakers.ui.dashBoard.DashBoardListener
import com.smilemakers.ui.dashBoard.DashBoardViewModelFactory
import com.smilemakers.ui.dashBoard.DashboardActivity
import com.smilemakers.databinding.FragmentDashboardBinding
import com.smilemakers.data.db.entities.DashBoard
import com.smilemakers.utils.getData
import com.smilemakers.utils.hide
import com.smilemakers.utils.show
import com.smilemakers.utils.showErrorSnackBar
import kotlinx.android.synthetic.main.fragment_dashboard.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

/**
 * A simple [Fragment] subclass.
 */
class DashboardFragment : Fragment(), KodeinAware, DashBoardListener {

    override val kodein by kodein()
    private val factory: DashBoardViewModelFactory by instance()

    companion object {
        lateinit var mActivity: DashboardActivity
        var mFragment: DashboardFragment? = null

        fun newInstance(mActivity: DashboardActivity): DashboardFragment? {
            this.mActivity = mActivity
            if (mFragment == null)
                mFragment = DashboardFragment()
            val bundle = Bundle()
            mFragment?.arguments = bundle
            return mFragment
        }
    }

    var binding: FragmentDashboardBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false)
        val viewModel =
            ViewModelProviders.of(this, factory).get(DashboardFragmentVM::class.java)
        binding?.vm = viewModel
        viewModel.dashBoardListener = this

        viewModel.getDashBoardData(context!!.getData(context!!, getString(R.string.user_id)))
        return binding?.root
    }

    override fun onStarted() {
        binding?.progressBar!!.show()
    }

    override fun onSuccess(dashBoard: DashBoard) {
        binding?.progressBar!!.hide()

        binding?.txtTotalPatientsNumber!!.text = dashBoard.total_patient
     //   binding?.txtTotalAdminsNumber!!.text = dashBoard.total_admin
        binding?.txtTotalDoctorNumber!!.text = dashBoard.total_doctor
        binding?.tvBCount!!.text = dashBoard.total_bapunagar
        binding?.tvNCount!!.text = dashBoard.total_nikol
    }

    override fun onFailure(message: String) {
        binding?.progressBar!!.hide()
        context!!.showErrorSnackBar(root_layout, message)
    }


}
