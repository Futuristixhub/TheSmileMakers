package com.smilemakers.dashBoard.appointmentFragment.calendar


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.simplemobiletools.commons.extensions.beVisibleIf
import com.simplemobiletools.commons.views.MyViewPager
import com.smilemakers.R
import com.smilemakers.dashBoard.DashboardActivity
import com.smilemakers.databinding.FragmentAppointmentBinding
import com.smilemakers.utils.*
import kotlinx.android.synthetic.main.fragment_appointment.view.*
import org.joda.time.DateTime


/**
 * A simple [Fragment] subclass.
 */
class AppointmentFragment : Fragment(),
    NavigationListener {

    private val PREFILLED_MONTHS = 251

    private var viewPager: MyViewPager? = null
    private var defaultMonthlyPage = 0
    private var todayDayCode = ""
    private var currentDayCode = ""
    private var isGoToTodayVisible = false

    companion object {
        lateinit var mActivity: DashboardActivity
        var mFragment: AppointmentFragment? = null

        fun newInstance(mActivity: DashboardActivity): AppointmentFragment? {
            Companion.mActivity = mActivity
            if (mFragment == null)
                mFragment =
                    AppointmentFragment()
            val bundle = Bundle()
            mFragment?.arguments = bundle
            return mFragment
        }
    }

    var binding: FragmentAppointmentBinding? = null
    val appointmentVM =
        AppointmentFragmentVM(
            this,
            mActivity
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
     //   currentDayCode = arguments?.getString(DAY_CODE) ?: ""
        currentDayCode = Formatter.getTodayCode()
        todayDayCode = Formatter.getTodayCode()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_appointment, container, false)
        binding?.vm = appointmentVM

        binding?.root!!.calendar_fab.beVisibleIf(requireContext().config.storedView != YEARLY_VIEW)
        binding?.root!!.calendar_fab.setOnClickListener {
            requireContext().launchNewEventIntent(currentDayCode)
        }

        Coroutines.io {
            var result=IcsImporter(requireContext()).importEvents("india.ics", 2, 0, false)
        }

      //  binding?.root!!.background = ColorDrawable(context!!.config.backgroundColor)
        viewPager = binding?.root!!.fragment_months_viewpager
        viewPager!!.id = (System.currentTimeMillis() % 100000).toInt()
        setupFragment()

        return binding?.root
    }

    private fun setupFragment() {
        val codes = getMonths(currentDayCode)
        val monthlyAdapter =
            MyMonthPagerAdapter(
                activity!!.supportFragmentManager,
                codes,
                this
            )
        defaultMonthlyPage = codes.size / 2

        viewPager!!.apply {
            adapter = monthlyAdapter
            addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {
                }

                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                }

                override fun onPageSelected(position: Int) {
                    currentDayCode = codes[position]
                }
            })
            currentItem = defaultMonthlyPage
        }
    }

    private fun getMonths(code: String): List<String> {
        val months = ArrayList<String>(PREFILLED_MONTHS)
        val today = Formatter.getDateTimeFromCode(code).withDayOfMonth(1)
        for (i in -PREFILLED_MONTHS / 2..PREFILLED_MONTHS / 2) {
            months.add(Formatter.getDayCodeFromDateTime(today.plusMonths(i)))
        }

        return months
    }

    override fun goLeft() {
        viewPager!!.currentItem = viewPager!!.currentItem - 1
    }

    override fun goRight() {
        viewPager!!.currentItem = viewPager!!.currentItem + 1
    }

    override fun goToDateTime(dateTime: DateTime) {
        currentDayCode = Formatter.getDayCodeFromDateTime(dateTime)
        setupFragment()
    }

}