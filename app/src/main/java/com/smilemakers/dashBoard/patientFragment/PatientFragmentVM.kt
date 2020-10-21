package com.smilemakers.dashBoard.patientFragment

import android.content.Intent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smilemakers.R
import com.smilemakers.dashBoard.DashboardActivity
import com.smilemakers.dashBoard.doctorFragment.addDoctor.AddDoctorActivity
import com.smilemakers.dashBoard.patientFragment.addPatient.AddPatientActivity
import com.smilemakers.dashBoard.patientFragment.addPatient.AddPatientFragment


class PatientFragmentVM( val repository: PatientRepository) : ViewModel() {

    val patientList = ArrayList<PatientPOJO>()

    init {
        newPatientList()
    }

    fun createList(recView: RecyclerView) {
        val viewManager = LinearLayoutManager(recView.context)
        val mAdapter = PatientListAdapter(recView.context, patientList)
        recView.apply {
            setHasFixedSize(true)
         //   layoutManager = viewManager as RecyclerView.LayoutManager?
            adapter = mAdapter
        }
    }

    fun newPatientList() {
        patientList.add(
            PatientPOJO(
                "Preyansh Brahmbhatt",
                "8866511911",
                "preyansh.brahmbhatt@gmail.com",
                "54234",
                "21",
                "Bapunagar"
            )
        )
        patientList.add(
            PatientPOJO(
                "Ronak Patel",
                "9988738479",
                "ronak.patel@gmail.com",
                "56830",
                "21",
                "Bapunagar"
            )
        )
        patientList.add(
            PatientPOJO(
                "Preyansh Brahmbhatt",
                "8866511911",
                "preyansh.brahmbhatt@gmail.com",
                "54234",
                "23",
                "Bapunagar"
            )
        )
        patientList.add(
            PatientPOJO(
                "Ronak Patel",
                "9988738479",
                "ronak.patel@gmail.com",
                "56830",
                "23",
                "Bapunagar"
            )
        )
        patientList.add(
            PatientPOJO(
                "Preyansh Brahmbhatt",
                "8866511911",
                "preyansh.brahmbhatt@gmail.com",
                "54234",
                "33",
                "Bapunagar"
            )
        )
        patientList.add(
            PatientPOJO(
                "Ronak Patel",
                "9988738479",
                "ronak.patel@gmail.com",
                "56830",
                "33",
                "Bapunagar"
            )
        )
        patientList.add(
            PatientPOJO(
                "Preyansh Brahmbhatt",
                "8866511911",
                "preyansh.brahmbhatt@gmail.com",
                "54234",
                "11",
                "Bapunagar"
            )
        )
        patientList.add(
            PatientPOJO(
                "Ronak Patel",
                "9988738479",
                "ronak.patel@gmail.com",
                "56830",
                "11",
                "Bapunagar"
            )
        )
    }

    fun onAddPatientClick(view: View) {

//        val transaction = (view.context as AppCompatActivity).supportFragmentManager.beginTransaction()
//        transaction.addToBackStack(null)
//        transaction.add(R.id.fl_dash_container, AddPatientFragment.newInstance()!!)
//        transaction.commit()

        Intent(view.context, AddPatientActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            view.context.startActivity(this)
        }
    }
}