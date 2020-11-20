package com.smilemakers.ui.dashBoard.appointmentFragment

import android.app.Application
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Build
import android.view.View
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.smilemakers.R
import com.smilemakers.ui.dashBoard.patientFragment.PatientListener
import com.smilemakers.utils.*
import kotlinx.coroutines.Job
import java.util.*

class AppointmentFragmentVM(val repository: AppointmentRepository, application: Application) :
    ViewModel() {

    var context = application.applicationContext

    var authListener: PatientListener? = null

    val appointmentDate = ObservableField<String>("")
    val appointmentTime = ObservableField<String>("")

    @RequiresApi(Build.VERSION_CODES.N)
    val calendar = Calendar.getInstance()
    @RequiresApi(Build.VERSION_CODES.N)
    val mYear = calendar.get(Calendar.YEAR)
    @RequiresApi(Build.VERSION_CODES.N)
    val mMonth = calendar.get(Calendar.MONTH)
    @RequiresApi(Build.VERSION_CODES.N)
    val mDate = calendar.get(Calendar.DAY_OF_MONTH)
    @RequiresApi(Build.VERSION_CODES.N)
    val mHour = calendar.get(Calendar.HOUR_OF_DAY)
    @RequiresApi(Build.VERSION_CODES.N)
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

    private lateinit var job: Job
    private val _appointments = MutableLiveData<List<Appointment>>()

    fun getAppointments() {
        job = Coroutines.ioThenMain(
            {
                repository.getAppointmentData(
                    context!!.getData(context!!, context.getString(R.string.user_id)),
                    context!!.getData(context!!, context.getString(R.string.user_type))
                )
            },
            { _appointments.value = it?.data?.appointment_list }
        )
    }

    val appointment: LiveData<List<Appointment>>
        get() = _appointments

    fun onSaveClick(
        pid: String,
        ptitle: String,
        location_name: String,
        date: String,
        time: String,
        treatmenttype: String,
        doctorname: String,
        color: String
    ) {
        authListener!!.onStarted()
        Coroutines.main {
            try {

                val authResponse =
                    repository.addAppointment(
                        pid,
                        ptitle,
                        location_name,
                        date,
                        time,
                        treatmenttype,
                        doctorname,
                        color
                    )
                authResponse.data?.let {
                    if (authResponse.status == false) {
                        authListener?.onFailure(authResponse.message!!)
                    } else {
                        authListener?.onSuccess(authResponse.message!!)
                    }
                }
                authListener?.onFailure(authResponse.message!!)
            } catch (e: ApiExceptions) {
                authListener?.onFailure(e.message!!)
            } catch (e: NoInternetException) {
                authListener?.onFailure(e.message!!)
            }
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
                @RequiresApi(Build.VERSION_CODES.N)
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