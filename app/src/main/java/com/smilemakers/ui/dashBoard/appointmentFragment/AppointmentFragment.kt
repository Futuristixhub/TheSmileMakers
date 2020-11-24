package com.smilemakers.ui.dashBoard.appointmentFragment


import android.graphics.Color
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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.simplemobiletools.commons.extensions.beVisibleIf
import com.simplemobiletools.commons.views.MyViewPager
import com.smilemakers.R
import com.smilemakers.databinding.FragmentAppointmentBinding
import com.smilemakers.ui.dashBoard.DashboardActivity
import com.smilemakers.ui.dashBoard.appointmentFragment.addAppointment.Event
import com.smilemakers.ui.dashBoard.appointmentFragment.calendar.MyMonthPagerAdapter
import com.smilemakers.ui.dashBoard.appointmentFragment.calendar.NavigationListener
import com.smilemakers.utils.*
import com.smilemakers.utils.Formatter
import kotlinx.android.synthetic.main.fragment_appointment.view.*
import org.joda.time.DateTime
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 */
class AppointmentFragment : Fragment(),
    NavigationListener, KodeinAware {

    override val kodein by kodein()

    private lateinit var viewModel: AppointmentFragmentVM
    private val factory: AppointMentViemodelFactory by instance()

    private val PREFILLED_MONTHS = 251

    private var viewPager: MyViewPager? = null
    private var defaultMonthlyPage = 0
    private var todayDayCode = ""
    private var currentDayCode = ""
    private var isGoToTodayVisible = false
    var events: ArrayList<Appointment>? = null

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
        viewModel =
            ViewModelProviders.of(this, factory).get(AppointmentFragmentVM::class.java)
        binding?.vm = viewModel

        binding?.progressBar?.show()

        binding?.root!!.calendar_fab.beVisibleIf(requireContext().config.storedView != YEARLY_VIEW)
        binding?.root!!.calendar_fab.setOnClickListener {
            requireContext().launchNewEventIntent(currentDayCode)
        }

        Coroutines.io {
            var result = IcsImporter(requireContext()).importEvents(
                context!!.getString(R.string.india_holiday_file),
                2,
                0,
                false
            )
        }

        //  binding?.root!!.background = ColorDrawable(context!!.config.backgroundColor)
        viewPager = binding?.root!!.fragment_months_viewpager
        viewPager!!.id = (System.currentTimeMillis() % 100000).toInt()

        return binding?.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAppointments()
        viewModel.appointment.observe(viewLifecycleOwner, Observer {
            binding?.progressBar?.hide()
            Log.d("tag", "response....." + it)
            events = it as ArrayList<Appointment>?
            val eventsDB = context!!.eventsDB

            Coroutines.io({ eventsDB.deleteAllEvents() })

            for (e in events!!.iterator()) {

                val input = e.appointment_time.split("-")[0]
                val df: DateFormat = SimpleDateFormat("hh:mm aa")
                val outputformat: DateFormat = SimpleDateFormat("HH:mm:ss")
                var date: Date? = null
                var start: String? = null
                try {
                    date = df.parse(input.trim())
                    start = outputformat.format(date)
                    println(start)
                } catch (pe: ParseException) {
                    pe.printStackTrace()
                }

                var dt: DateTime = DateTime.parse(e.appointment_date + "T" + start)
                var date2: Date? = null
                val input1 = e.appointment_time.split("-")[1]
                var end: String? = null
                try {
                    date2 = df.parse(input1.trim())
                    end = outputformat.format(date2)
                    println(end)
                } catch (pe: ParseException) {
                    pe.printStackTrace()
                }

                var dt2: DateTime = DateTime.parse(e.appointment_date + "T" + end)

                if (!e.f_name.isEmpty()) {
                    Log.d("hhhhh", "/////....." + e.f_name)
                    Coroutines.io(
                        {
                            eventsDB.insertOrUpdate(
                                Event(
                                    null,
                                    e.appointment_id,
                                    dt.seconds(),
                                    dt2.seconds(),
                                    e.f_name,
                                    e.l_name,
                                    e.appointment_type,
                                    e.doctor_id,
                                    e.typesoftreatment,
                                    e.prescription,
                                    e.age
                                )
                            )
                        })
                }
            }

            setupFragment()
        })

    }

    private fun setupFragment() {

        val bar: ActionBar? = (activity as AppCompatActivity?)!!.supportActionBar
        if (bar != null) {
            val tv = TextView(context)
            val lp: ActionBar.LayoutParams = ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT,  // Width of TextView
                ActionBar.LayoutParams.WRAP_CONTENT
            ) // Height of TextView
            tv.layoutParams = lp
            tv.setText(getString(R.string.appointment))
            tv.setTextColor(Color.WHITE)

            val typedValue = TypedValue()
            resources.getValue(R.dimen.actionBar_text, typedValue, true)
            val myFloatValue = typedValue.float

            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, myFloatValue)
            bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM)
            bar.setCustomView(tv)
        }

        val codes = getMonths(currentDayCode)
        val monthlyAdapter =
            MyMonthPagerAdapter(
                activity!!.supportFragmentManager,
                codes,
                this, events
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
