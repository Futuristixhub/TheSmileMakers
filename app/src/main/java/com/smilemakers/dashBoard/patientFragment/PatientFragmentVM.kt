package com.smilemakers.dashBoard.patientFragment

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smilemakers.R
import com.smilemakers.dashBoard.DashboardActivity
import com.smilemakers.dashBoard.patientFragment.addPatient.AddPatientFragment

class PatientFragmentVM(val mFragment: PatientFragment, val mActivity: DashboardActivity) : ViewModel() {

    val patientList = ArrayList<PatientPOJO>()

    init {
        newPatientList()
    }

    fun createList(recView: RecyclerView) {
        val viewManager = LinearLayoutManager(mFragment.requireContext())
        val mAdapter = PatientListAdapter(mFragment.requireContext(), patientList)
        recView.apply {
            setHasFixedSize(true)
            layoutManager = viewManager as RecyclerView.LayoutManager?
            adapter = mAdapter
        }
    }

    fun newPatientList() {
        patientList.add(PatientPOJO("Preyansh Brahmbhatt", "8866511911", "preyansh.brahmbhatt@gmail.com", "54234", "11-Nov-1993", "Bapunagar"))
        patientList.add(PatientPOJO("Ronak Patel", "9988738479", "ronak.patel@gmail.com", "56830", "21-Mar-1992", "Bapunagar"))
        patientList.add(PatientPOJO("Preyansh Brahmbhatt", "8866511911", "preyansh.brahmbhatt@gmail.com", "54234", "11-Nov-1993", "Bapunagar"))
        patientList.add(PatientPOJO("Ronak Patel", "9988738479", "ronak.patel@gmail.com", "56830", "21-Mar-1992", "Bapunagar"))
        patientList.add(PatientPOJO("Preyansh Brahmbhatt", "8866511911", "preyansh.brahmbhatt@gmail.com", "54234", "11-Nov-1993", "Bapunagar"))
        patientList.add(PatientPOJO("Ronak Patel", "9988738479", "ronak.patel@gmail.com", "56830", "21-Mar-1992", "Bapunagar"))
        patientList.add(PatientPOJO("Preyansh Brahmbhatt", "8866511911", "preyansh.brahmbhatt@gmail.com", "54234", "11-Nov-1993", "Bapunagar"))
        patientList.add(PatientPOJO("Ronak Patel", "9988738479", "ronak.patel@gmail.com", "56830", "21-Mar-1992", "Bapunagar"))
    }

    fun onAddPatientClick(view: View) {
        val transaction = mActivity.supportFragmentManager.beginTransaction()
        transaction.addToBackStack(null)
        transaction.add(R.id.fl_dash_container, AddPatientFragment.newInstance(mActivity)!!)
        transaction.commit()
    }
}