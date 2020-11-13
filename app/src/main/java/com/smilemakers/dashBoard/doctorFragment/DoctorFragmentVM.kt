package com.smilemakers.dashBoard.doctorFragment

import android.content.Intent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smilemakers.R
import com.smilemakers.dashBoard.DashboardActivity
import com.smilemakers.dashBoard.appointmentFragment.addAppointment.AppointmentFormActivity
import com.smilemakers.dashBoard.doctorFragment.addDoctor.AddDoctorActivity
import com.smilemakers.dashBoard.doctorFragment.addDoctor.AddDoctorFragment
import com.smilemakers.dashBoard.doctorFragment.detail.DetailFragment
import com.smilemakers.dashBoard.patientFragment.addPatient.AddPatientFragment
import com.smilemakers.dashBoard.profile.ProfileRepository
import com.smilemakers.utils.DAY_CODE
import com.smilemakers.utils.NEW_EVENT_START_TS
import com.smilemakers.utils.getNewEventTimestampFromCode
import com.smilemakers.utils.lazyDeferred

class DoctorFragmentVM(val repository: DoctorRepository) :
    ViewModel() {

    val patientList = ArrayList<DoctorPOJO>()

    init {
        newPatientList()
    }

    val doctors by lazyDeferred {
        repository.getDoctorData()
    }

    fun createList(recView: RecyclerView) {
        val viewManager = LinearLayoutManager(recView.context)
        val mAdapter = DoctorListAdapter(recView.context, patientList)
        recView.apply {
            setHasFixedSize(true)
           // layoutManager = viewManager as RecyclerView.LayoutManager?
            adapter = mAdapter
        }
    }

    fun newPatientList() {
        patientList.add(
            DoctorPOJO(
                "Preyansh Brahmbhatt",
                "8866511911",
                "preyansh.brahmbhatt@gmail.com",
                "bapunagar",
                "25",
                "Costmetic Dentistry "
            )
        )
        patientList.add(
            DoctorPOJO(
                "Ronak Patel",
                "9988738479",
                "ronak.patel@gmail.com",
                "nikol",
                "21",
                "Cosmetic Dentistry"
            )
        )
        patientList.add(
            DoctorPOJO(
                "Preyansh Brahmbhatt",
                "8866511911",
                "preyansh.brahmbhatt@gmail.com",
                "bapunagar",
                "25",
                "Costmetic Dentistry"
            )
        )
        patientList.add(
            DoctorPOJO(
                "Ronak Patel",
                "9988738479",
                "ronak.patel@gmail.com",
                "nikol",
                "21",
                "Cosmetic Dentistry"
            )
        )
        patientList.add(
            DoctorPOJO(
                "Preyansh Brahmbhatt",
                "8866511911",
                "preyansh.brahmbhatt@gmail.com",
                "bapunagar",
                "25",
                "Costmetic Dentistry "
            )
        )
        patientList.add(
            DoctorPOJO(
                "Ronak Patel",
                "9988738479",
                "ronak.patel@gmail.com",
                "nikol",
                "21",
                "Cosmetic Dentistry"
            )
        )
        patientList.add(
            DoctorPOJO(
                "Preyansh Brahmbhatt",
                "8866511911",
                "preyansh.brahmbhatt@gmail.com",
                "bapunagar",
                "25",
                "Costmetic Dentistry"
            )
        )
        patientList.add(
            DoctorPOJO(
                "Ronak Patel",
                "9988738479",
                "ronak.patel@gmail.com",
                "nikol",
                "21",
                "Cosmetic Dentistry"
            )
        )
    }

    fun onAddDoctorClick(view: View) {
//        val transaction = (view.context as AppCompatActivity).supportFragmentManager.beginTransaction()
//        transaction.addToBackStack(null)
//        transaction.add(R.id.fl_dash_container, AddDoctorFragment.newInstance()!!)
//        transaction.commit()

        Intent(view.context, AddDoctorActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            view.context.startActivity(this)
        }
    }


}