package com.simplemobiletools.calendar.pro.fragments

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.simplemobiletools.commons.views.MyViewPager
import com.smilemakers.R
import com.smilemakers.dashBoard.appointmentFragment.MyDayPagerAdapter
import com.smilemakers.dashBoard.appointmentFragment.NavigationListener
import com.smilemakers.utils.DAY_CODE
import com.smilemakers.utils.Formatter
import com.smilemakers.utils.config
import kotlinx.android.synthetic.main.fragment_days_holder.view.*
import org.joda.time.DateTime
import java.util.*

class DayFragmentsHolder : Fragment(), NavigationListener {
    private val PREFILLED_DAYS = 251

    private var viewPager: MyViewPager? = null
    private var defaultDailyPage = 0
    private var todayDayCode = ""
    private var currentDayCode = ""
    private var isGoToTodayVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentDayCode = arguments?.getString(DAY_CODE) ?: ""
        todayDayCode = Formatter.getTodayCode()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_days_holder, container, false)
   //     view.background = ColorDrawable(context!!.config.backgroundColor)
        viewPager = view.fragment_days_viewpager
        viewPager!!.id = (System.currentTimeMillis() % 100000).toInt()
        setupFragment()
        return view
    }

    private fun setupFragment() {
        val codes = getDays(currentDayCode)
        val dailyAdapter = MyDayPagerAdapter(activity!!.supportFragmentManager, codes, this)
        defaultDailyPage = codes.size / 2


        viewPager!!.apply {
            adapter = dailyAdapter
            addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {
                }

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                }

                override fun onPageSelected(position: Int) {
                    currentDayCode = codes[position]
//                    val shouldGoToTodayBeVisible = shouldGoToTodayBeVisible()
//                    if (isGoToTodayVisible != shouldGoToTodayBeVisible) {
//                        (activity as? MainActivity)?.toggleGoToTodayVisibility(shouldGoToTodayBeVisible)
//                        isGoToTodayVisible = shouldGoToTodayBeVisible
//                    }
                }
            })
            currentItem = defaultDailyPage
        }

    }

    private fun getDays(code: String): List<String> {
        val days = ArrayList<String>(PREFILLED_DAYS)
        val today = Formatter.getDateTimeFromCode(code)
        for (i in -PREFILLED_DAYS / 2..PREFILLED_DAYS / 2) {
            days.add(Formatter.getDayCodeFromDateTime(today.plusDays(i)))
        }
        return days
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

     fun goToToday() {
        currentDayCode = todayDayCode
        setupFragment()
    }

    private fun dateSelected(dateTime: DateTime, datePicker: DatePicker) {
        val month = datePicker.month + 1
        val year = datePicker.year
        val day = datePicker.dayOfMonth
        val newDateTime = dateTime.withDate(year, month, day)
        goToDateTime(newDateTime)
    }

     fun refreshEvents() {
        (viewPager?.adapter as? MyDayPagerAdapter)?.updateCalendars(viewPager?.currentItem ?: 0)
    }

     fun shouldGoToTodayBeVisible() = currentDayCode != todayDayCode

     fun getNewEventDayCode() = currentDayCode
}
