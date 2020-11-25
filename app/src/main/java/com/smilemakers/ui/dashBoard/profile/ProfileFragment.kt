package com.smilemakers.ui.dashBoard.profile

import android.graphics.Color
import android.net.Uri
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
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.simplemobiletools.commons.extensions.toast
import com.smilemakers.R
import com.smilemakers.databinding.FragmentProfileBinding
import com.smilemakers.ui.dashBoard.DashboardActivity
import com.smilemakers.utils.*
import kotlinx.android.synthetic.main.fragment_profile.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import java.net.URL
import java.net.URLConnection

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment(), KodeinAware, ProfileListener {

    override val kodein by kodein()

    private lateinit var viewModel: ProfileVM
    private val factory: ProfileViewModelFactory by instance()

    companion object {
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
        viewModel =
            ViewModelProviders.of(this, factory).get(ProfileVM::class.java)
        binding?.vm = viewModel
        viewModel.profileListener = this

        val bar: ActionBar? = (activity as AppCompatActivity?)!!.supportActionBar
        if (bar != null) {
            val tv = TextView(context)
            val lp: ActionBar.LayoutParams = ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT,  // Width of TextView
                ActionBar.LayoutParams.WRAP_CONTENT
            ) // Height of TextView
            tv.layoutParams = lp
            tv.setText(getString(R.string.profile))
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
        viewModel.getData(
            context!!.getData(context!!, getString(R.string.user_id)),
            context!!.getData(context!!, getString(R.string.user_type))
        )
    }

    override fun onStarted() {
        binding?.progressBar!!.show()
    }

    override fun onSuccess(profile: Profile) {
        binding?.progressBar!!.hide()
        binding?.tvFname?.text = profile.fname
        binding?.tvLname?.text = profile.lname
        if (profile.email.isNullOrEmpty()) {
            binding?.llEmail?.visibility = View.GONE
        }
        binding?.tvEmail?.text = profile.email
        binding?.tvMobile?.text = profile.mobile
        binding?.tvLocation?.text = profile.address

        Coroutines.io {
            val image = context!!.isImageURL(profile.image!!)
            if (image) {
                Coroutines.main {
                    Glide.with(context!!).load(Uri.parse(profile.image)).into(binding?.ivImg!!)
                }
            }
        }

    }

    override fun onFailure(message: String) {
        binding?.progressBar!!.hide()
        context!!.showErrorSnackBar(root_layout, message)
    }

}
