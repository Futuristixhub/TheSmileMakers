package com.smilemakers.dashBoard.appointmentFragment

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.view.View
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.smilemakers.R
import com.smilemakers.dashBoard.DashboardActivity
import com.smilemakers.utils.showErrorSnackBar
import java.util.*

class AppointmentFragmentVM(val repository: AppointmentRepository) : ViewModel() {

    val appointmentDate = ObservableField<String>("")
    val appointmentTime = ObservableField<String>("")

    val calendar = Calendar.getInstance()
    val mYear = calendar.get(Calendar.YEAR)
    val mMonth = calendar.get(Calendar.MONTH)
    val mDate = calendar.get(Calendar.DAY_OF_MONTH)
    val mHour = calendar.get(Calendar.HOUR_OF_DAY)
    val mMinute = calendar.get(Calendar.MINUTE)

    val pname: String? = null
    val apt_date: String? = null
    val apt_time: String? = null
    val apt_loaction: String? = null
    val trmt_type: String? = null
    val dr_name: String? = null
    val presc = MutableLiveData<String>()

    val time: String? = null
    val title1: String? = null
    val title2: String? = null
    val title3: String? = null
    val title4: String? = null

    fun onSubmitClick(view: View) {
        if (isValid(view)) {
            (view.context as AppCompatActivity).finish()
        }
    }

    fun isValid(view: View): Boolean {

        if (presc.value == null || presc.value?.isEmpty()!!) {
            view.context.showErrorSnackBar(view, view.context.getString(R.string.empty_presc))
            return false
        }

        return true
    }

    fun onAppointmentDateClick(view: View) {
        val datePickerDialog = DatePickerDialog(
            view.context,
            R.style.DatePickerDialog,
            object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                    val date = "$dayOfMonth-$month-$year"
                    val inputPatter = view!!.context.getString(R.string.date_format)
                    val outputPatter = view!!.context.getString(R.string.date_format2)
                    val inputFormat = SimpleDateFormat(inputPatter)
                    val outputFormat = SimpleDateFormat(outputPatter)
                    var formattedDate = inputFormat.parse(date)
                    val str = outputFormat.format(formattedDate)
                    appointmentDate.set("$str")
                }
            },
            mYear,
            mMonth,
            mDate
        ).apply {
            datePicker.minDate = Date().time
        }
        datePickerDialog.show()
    }

    fun onAppointmentTimeClick(view: View) {
        val timePickerDialog = TimePickerDialog(
            view.context, R.style.DatePickerDialog,
            object : TimePickerDialog.OnTimeSetListener {
                override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                    var time = "$hourOfDay : $minute"
                    if (hourOfDay > 12)
                        time = "${hourOfDay - 12}:$minute PM"
                    else time = "$hourOfDay:$minute AM"
                    appointmentTime.set(time)
                }
            }, mHour, mMinute, false
        )

        timePickerDialog.show()
    }
}