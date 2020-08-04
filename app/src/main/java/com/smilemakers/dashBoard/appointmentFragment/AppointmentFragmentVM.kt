package com.smilemakers.dashBoard.appointmentFragment

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.view.View
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.smilemakers.R
import com.smilemakers.dashBoard.DashboardActivity
import java.util.*

class AppointmentFragmentVM(mFragment: AppointmentFragment, val mActivity: DashboardActivity) : ViewModel(){

    val appointmentDate = ObservableField<String>("")
    val appointmentTime = ObservableField<String>("")

    val calendar = Calendar.getInstance()
    val mYear = calendar.get(Calendar.YEAR)
    val mMonth = calendar.get(Calendar.MONTH)
    val mDate = calendar.get(Calendar.DAY_OF_MONTH)
    val mHour = calendar.get(Calendar.HOUR_OF_DAY)
    val mMinute = calendar.get(Calendar.MINUTE)

    val datePickerDialog = DatePickerDialog(mActivity, R.style.DatePickerDialog, object : DatePickerDialog.OnDateSetListener {
        override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
            val date = "$dayOfMonth-$month-$year"
            val inputPatter = "dd-MM-yyyy"
            val outputPatter = "dd-MMM-yyyy"
            val inputFormat = SimpleDateFormat(inputPatter)
            val outputFormat = SimpleDateFormat(outputPatter)
            var formattedDate = inputFormat.parse(date)
            val str = outputFormat.format(formattedDate)
            appointmentDate.set("$str")
        }
    }, mYear,mMonth,mDate).apply {
        datePicker.minDate = Date().time
    }

    val timePickerDialog = TimePickerDialog(mActivity, R.style.DatePickerDialog,
        object : TimePickerDialog.OnTimeSetListener {
            override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                var time = "$hourOfDay : $minute"
                if (hourOfDay > 12)
                    time = "${hourOfDay - 12}:$minute PM"
                else time = "$hourOfDay:$minute AM"
                appointmentTime.set(time)
            }
        },mHour,mMinute,false)

    fun onAppointmentDateClick(view: View) {
        datePickerDialog.show()
    }

    fun onAppointmentTimeClick(view: View) {
        timePickerDialog.show()
    }
}